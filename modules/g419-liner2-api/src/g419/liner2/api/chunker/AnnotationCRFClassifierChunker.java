package g419.liner2.api.chunker;

import g419.corpus.structure.*;

import java.util.*;
import java.util.regex.Pattern;

/**
* Created by michal on 8/21/14.
*/
public class AnnotationCRFClassifierChunker extends Chunker {

    private List<Pattern> list;
    private String base;
    private CrfppChunker baseChunker;

    public AnnotationCRFClassifierChunker(List<Pattern> list, String base, CrfppChunker baseChunker){
        this.list = list;
        this.base = base;
        this.baseChunker = baseChunker;
    }
    @Override
    public HashMap<Sentence, AnnotationSet> chunk(Document ps) {
        Document wrapped = prepareData(ps, "classify");
        HashMap<Sentence, AnnotationSet> chunked = baseChunker.chunk(wrapped);
        HashMap<Sentence, AnnotationSet> result = new HashMap<Sentence, AnnotationSet>();
        HashMap<String, Sentence> sentences = sentencesById(ps.getSentences());
        for(AnnotationSet wrappedSet: chunked.values()){
            Sentence sent = sentences.get(wrappedSet.getSentence().getId());
            AnnotationSet newSet = new AnnotationSet(sent);            
            for(Annotation source: sent.getChunks()){
                Annotation newAnn = source.clone();
                if(source.getType().equals(base)){
                    for(Annotation wrappedAnn: wrappedSet.chunkSet()){
                        if(wrappedAnn.getText().equals(source.getText())){
                            newAnn.setType(wrappedAnn.getType());
                            break;
                        }

                    }
                }
                newSet.addChunk(newAnn);
            }
            result.put(sent, newSet);
        }
        return result;
    }

    public HashMap<String, Sentence> sentencesById(ArrayList<Sentence> sentences){
        HashMap<String, Sentence> mapped = new HashMap<String, Sentence>();
        for(Sentence sent: sentences){
            mapped.put(sent.getId(), sent);
        }
        return mapped;
    }

    public Document prepareData(Document doc, String mode){
        Paragraph whole = new Paragraph("wholeDoc");
        TokenAttributeIndex index = doc.getAttributeIndex().clone();
        index.addAttribute("context");
        for(Sentence sentence: doc.getSentences()){
            HashMap<Integer, Annotation> annotationsByStart = filterAnnotations(sentence.getChunks(), mode);
            Sentence prepared = wrapAnnotations(annotationsByStart, sentence, index);
            prepared.setAttributeIndex(index);
            whole.addSentence(prepared);
        }
        Document crfppData = new Document(doc.getName(), index);
        crfppData.addParagraph(whole);

        return crfppData;

    }

    private Sentence wrapAnnotations(HashMap<Integer, Annotation> annotationsByStarts, Sentence sentence, TokenAttributeIndex index){
        List<Token> tokens = sentence.getTokens();
        Sentence wrappedSentence = new Sentence();
        ArrayList<Token> wrappedTokens = new ArrayList<Token>();
        LinkedHashSet<Annotation> wrappedAnnotations = new LinkedHashSet<Annotation>();

        wrappedSentence.setTokens(wrappedTokens);
        wrappedSentence.setId(sentence.getId());

        for(int i=0; i < tokens.size(); i++){
            Token newToken;
            if(annotationsByStarts.containsKey(i)){
                Annotation chosen = annotationsByStarts.get(i);
                newToken = findHead(chosen, sentence).clone();
                newToken.setAttributeValue(index.getIndex("context"), "A");
                // Chwilowe rozwiazanie dla cechy orth i base
                newToken.setAttributeValue(index.getIndex("orth"), chosen.getText());
                newToken.setAttributeValue(index.getIndex("base"), chosen.getBaseText());
                // -----------------------------------
                int newIndex = wrappedTokens.size();
                newToken.setAttributeIndex(index);
                wrappedTokens.add(newToken);
                wrappedAnnotations.add(new Annotation(newIndex, newIndex, chosen.getType(), wrappedSentence));
                i = chosen.getEnd();
            }
            else{
                newToken = tokens.get(i).clone();
                newToken.setAttributeValue(index.getIndex("context"), "T");
                newToken.setAttributeIndex(index);
                wrappedTokens.add(newToken);

            }

        }

        wrappedSentence.setAnnotations(new AnnotationSet(wrappedSentence, wrappedAnnotations));
        return  wrappedSentence;

    }

    private Token findHead(Annotation ann, Sentence sentence){
        List<Token> tokens = sentence.getTokens();
        TokenAttributeIndex index = sentence.getAttributeIndex();
        for(Token tok: tokens.subList(ann.getBegin(), ann.getEnd() + 1)){
            String tokClass = tok.getAttributeValue(index.getIndex("class"));
            if(tokClass != null && tokClass.equals("subst")){
                return tok;
            }
        }
        return tokens.get(ann.getBegin());
    }

    private HashMap<Integer, Annotation> filterAnnotations(HashSet<Annotation> annotations, String mode){
        HashSet<Annotation> annotationsToWrap = new HashSet<Annotation>();
        if(mode.equals("train")){
            for(Annotation ann: annotations){
              for(Pattern patt: list){
                  if(patt.matcher(ann.getType()).find()){
                      HashSet<Annotation> toRemove = new HashSet<Annotation>();
                      boolean add = true;
                      for(Annotation chosen: annotationsToWrap){
                          if(ann.getTokens().containsAll(chosen.getTokens())){
                              toRemove.add(chosen);
                          }
                          else if(chosen.getTokens().containsAll(ann.getTokens())){
                              add = false;
                              break;
                          }
                      }
                      for(Annotation replaced: toRemove){
                          annotationsToWrap.remove(replaced);
                      }
                      if(add){
                          annotationsToWrap.add(ann);
                      }
                      break;
                  }
              }
            }
        }
        else if(mode.equals("classify")){
            for(Annotation ann: annotations){
                if(base.equals(ann.getType())){
                    annotationsToWrap.add(ann);
                }
            }
        }
        HashMap<Integer, Annotation> annotationsByStart = new HashMap<Integer, Annotation>();
        for(Annotation ann: annotationsToWrap){
            annotationsByStart.put(ann.getBegin(), ann);
        }
        return annotationsByStart;
    }
}