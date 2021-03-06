package g419.liner2.core.chunker.factory;

import g419.corpus.ConsolePrinter;
import g419.corpus.io.reader.AbstractDocumentReader;
import g419.corpus.io.reader.ReaderFactory;
import g419.corpus.structure.CrfTemplate;
import g419.corpus.structure.Document;
import g419.liner2.core.LinerOptions;
import g419.liner2.core.chunker.AnnotationCRFClassifierChunker;
import g419.liner2.core.chunker.Chunker;
import g419.liner2.core.chunker.CrfppChunker;
import g419.liner2.core.features.TokenFeatureGenerator;
import g419.liner2.core.lib.LibLoaderCrfpp;
import g419.liner2.core.tools.TemplateFactory;
import org.ini4j.Ini;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by michal on 9/2/14.
 */
public class ChunkerFactoryItemAnnotationCRFClassifier extends ChunkerFactoryItem {

    public ChunkerFactoryItemAnnotationCRFClassifier() {
        super("CRFclassifier");
    }
    @Override
    public Chunker getChunker(Ini.Section description, ChunkerManager cm) throws Exception {
        try {
            LibLoaderCrfpp.load();
        } catch (UnsatisfiedLinkError e) {
            System.exit(1);
        }
        String mode = description.get("mode");

        if(mode.equals("train")){
            return train(description, cm);
        } else if(mode.equals("load")){
            return load(description, cm);
        } else{
            throw new Exception("Unrecognized mode for CRFPP annotation classifier: " + mode + "(Valid: train/load)");
        }
    }

    private List<String> parseAnnotationFeatures(String filePath) throws IOException {
        List<String> features = new ArrayList<String>();
        if(filePath != null) {
            File featuresFile = new File(filePath);
            if (!featuresFile.exists()) {
                throw new FileNotFoundException("Error while parsing features:" + filePath + " is not an existing file!");
            }
            String iniPath = featuresFile.getAbsoluteFile().getParentFile().getAbsolutePath();
            BufferedReader br = new BufferedReader(new FileReader(featuresFile));
            StringBuffer sb = new StringBuffer();
            String feature = br.readLine();
            while (feature != null) {
                if (!feature.isEmpty() && !feature.startsWith("#")) {
                    feature = feature.trim().replace("{INI_PATH}", iniPath);
                    features.add(feature);
                }
                feature = br.readLine();
            }
        }
        return features;
    }

    private Chunker load(Ini.Section description, ChunkerManager cm) throws Exception {


        String store = description.get("store");

        ConsolePrinter.log("--> CRFPP Chunker deserialize from " + store);
        CrfppChunker baseChunker = new CrfppChunker(loadUsedFeatures(description.get("crf-features")), null);
        baseChunker.deserialize(store);
        CrfTemplate template = createTemplate(description.get("template"), description.get("context"));
        baseChunker.setTemplate(template);
        TokenFeatureGenerator gen = new TokenFeatureGenerator(cm.opts.features);
        AnnotationCRFClassifierChunker chunker = new AnnotationCRFClassifierChunker(null, description.get("base-annotation"), baseChunker, gen, parseAnnotationFeatures(description.get("annotation-features")), description.get("context"));

        return chunker;
    }

    private Chunker train(Ini.Section description, ChunkerManager cm) throws Exception {
        ConsolePrinter.log("--> CRFPP annotation classifier train");

        String inputFile = description.get("training-data");
        String inputFormat;

        String modelFilename = description.get("store");
        TokenFeatureGenerator gen = new TokenFeatureGenerator(cm.opts.features);

        ArrayList<Document> trainData = new ArrayList<Document>();
        if(inputFile.equals("{CV_TRAIN}")){
            trainData = cm.trainingData;
        }
        else{
            inputFormat = description.get("format");
            AbstractDocumentReader reader = ReaderFactory.get().getStreamReader(inputFile, inputFormat);
            Document document = reader.nextDocument();
            while ( document != null ){
                gen.generateFeatures(document);
                trainData.add(document);
                document = reader.nextDocument();
            }
        }
        List<Pattern> list = LinerOptions.getGlobal().parseTypes(description.get("types"));

        CrfppChunker baseChunker = new CrfppChunker(Integer.parseInt(description.get("threads")), list, loadUsedFeatures(description.get("crf-features")), null);
        baseChunker.setTrainingDataFilename(description.get("store-training-data"));
        baseChunker.setModelFilename(modelFilename);
        ConsolePrinter.log("--> Training on file=" + inputFile);

        CrfTemplate template = createTemplate(description.get("template"), description.get("context"));
        baseChunker.setTemplate(template);
        AnnotationCRFClassifierChunker chunker = new AnnotationCRFClassifierChunker(list, description.get("base-annotation"), baseChunker, gen, parseAnnotationFeatures(description.get("annotation-features")), description.get("context"));

        for(Document document: trainData){
            gen.generateFeatures(document);
            Document wrapped = chunker.prepareData(document, "train");
            baseChunker.addTrainingData(wrapped);
            if(template.getAttributeIndex() == null){
                template.setAttributeIndex(wrapped.getAttributeIndex());
            }
        }
        baseChunker.train();

        return chunker;

    }

    private CrfTemplate createTemplate(String templateData, String context) throws Exception {
        CrfTemplate template = TemplateFactory.parseTemplate(templateData);
        template.addFeature("context:" + context);
        for(String feature: new ArrayList<String>(template.getFeatureNames())){
            if(!(feature.contains("/") || feature.equals("context"))){
                String[] windowDesc = template.getFeatures().get(feature);
                for(int i=1; i < windowDesc.length; i++){
                    template.addFeature(feature + ":" + windowDesc[i] + "/context:0");
                }
            }
        }
        return  template;
    }

    private List<String> loadUsedFeatures(String file) throws IOException {
        ArrayList<String> usedFeatures = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while(line != null){
            if(!(line.isEmpty() || line.startsWith("#"))){
                usedFeatures.add(line.trim());
            }
            line = reader.readLine();
        }
        return usedFeatures;
    }
}
