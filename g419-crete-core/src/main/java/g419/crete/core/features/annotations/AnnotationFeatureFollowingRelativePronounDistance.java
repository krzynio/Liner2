package g419.crete.core.features.annotations;

import g419.corpus.structure.Annotation;
import g419.corpus.structure.Token;
import g419.corpus.structure.TokenAttributeIndex;
import g419.crete.core.features.AbstractFeature;

import java.util.List;

public class AnnotationFeatureFollowingRelativePronounDistance extends AbstractFeature<Annotation, Integer> {

	final static String KTORY_BASE = "który"; 
	
	@Override
	public void generateFeature(Annotation input) {
		this.value = 100;
		TokenAttributeIndex ai = input.getSentence().getAttributeIndex();
		List<Token> tokens = input.getSentence().getTokens();
		int totalTokens = tokens.size();
		
		for(int i = input.getEnd(); i <  totalTokens; i++){
			Token token = tokens.get(i);
			String base = ai.getAttributeValue(token, "base");
			if(KTORY_BASE.equalsIgnoreCase(base))
				this.value = Math.min(this.value, tokens.indexOf(token) - input.getEnd());
		}
		
	}

	@Override
	public String getName() {
		return "annotation_following_relative_pronoun_distance";
	}

	@Override
	public Class<Annotation> getInputTypeClass() {
		return Annotation.class;
	}

	@Override
	public Class<Integer> getReturnTypeClass() {
		return Integer.class;
	}

}
