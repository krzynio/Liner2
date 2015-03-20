package g419.crete.cli.action;

import g419.corpus.io.reader.AbstractDocumentReader;
import g419.corpus.io.reader.ReaderFactory;
import g419.corpus.structure.Annotation;
import g419.corpus.structure.AnnotationTokenListComparator;
import g419.corpus.structure.Document;
import g419.crete.api.CreteOptions;
import g419.crete.api.annotation.AbstractAnnotationSelector;
import g419.crete.api.annotation.AnnotationSelectorFactory;
import g419.crete.api.evaluation.ParentEvaluator;
import g419.crete.api.evaluation.ParentEvaluator.*;
import g419.lib.cli.CommonOptions;
import g419.lib.cli.action.Action;
import g419.liner2.api.features.TokenFeatureGenerator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;


public class ActionEvalParent extends Action {


	public static final String BASIC_SELECTOR = "selector";
	public static final String OVERRIDE_SELECTOR = "override_selector";
	
	
	private String input_file = null;
    private String input_format = null;
    
	public ActionEvalParent() {
		super("eval-parent");
		this.options.addOption(CommonOptions.getInputFileFormatOption());
        this.options.addOption(CommonOptions.getInputFileNameOption());
        this.options.addOption(CommonOptions.getFeaturesOption());
        this.options.addOption(CommonOptions.getFeaturesOption());
        this.options.addOption(CommonOptions.getModelFileOption());
	}

	@Override
	public void parseOptions(String[] args) throws Exception {
		CommandLine line = new GnuParser().parse(this.options, args);
		parseDefault(line);
        this.input_file = line.getOptionValue(CommonOptions.OPTION_INPUT_FILE);
        this.input_format = line.getOptionValue(CommonOptions.OPTION_INPUT_FORMAT, "ccl");
        CreteOptions.getOptions().parseModelIni(line.getOptionValue(CommonOptions.OPTION_MODEL));
	}

	private String[] getBatchFiles() throws IOException{
		String[] files = new String[2];
		BufferedReader ir = new BufferedReader(new InputStreamReader(new FileInputStream(this.input_file)));
		files[0] = ir.readLine();
		files[1] = ir.readLine();
		return files;
	}
	
	@Override
	public void run() throws Exception {
		
		String[] goldSysFiles = getBatchFiles();
		
		String goldInputFile = goldSysFiles[0].split(";")[0];
		String goldInputFormat = goldSysFiles[0].split(";")[1];
		
		String sysInputFile =  goldSysFiles[1].split(";")[0];
		String sysInputFormat =  goldSysFiles[1].split(";")[1];
		
		
		boolean sysTEI = sysInputFormat.contains("tei");
		RelationUnitCriterion identifyingUnitsCriterion = new NamedEntityCriterion();
		RelationUnitCriterion referencingUnitsCriterion =  new ZeroCriterion();
		//!sysTEI
		Comparator<Annotation> matcher = new AnnotationTokenListComparator(!sysTEI); // for ccl -> true, for tei -> false
		ParentEvaluator evaluator = new ParentEvaluator(identifyingUnitsCriterion, referencingUnitsCriterion, matcher);
		
		AbstractDocumentReader goldReader = ReaderFactory.get().getStreamReader(goldInputFile, goldInputFormat);
		AbstractDocumentReader sysReader = ReaderFactory.get().getStreamReader(sysInputFile, sysInputFormat);
		Document referenceDocument = goldReader.nextDocument();
		Document systemResponseDocument = sysReader.nextDocument();
		
		LinkedHashMap<String, String> features = new LinkedHashMap<String, String>();
		features.put("orth", "orth");
		features.put("ctag", "ctag");
		features.put("base", "base");
		features.put("pos", "pos");
		TokenFeatureGenerator gen = new TokenFeatureGenerator(features);
		
		AbstractAnnotationSelector selector = AnnotationSelectorFactory.getFactory().getInitializedSelector(CreteOptions.getOptions().getProperties().getProperty(BASIC_SELECTOR));
        AbstractAnnotationSelector overrideSelector = AnnotationSelectorFactory.getFactory().getInitializedSelector(CreteOptions.getOptions().getProperties().getProperty(OVERRIDE_SELECTOR));
        
		// TODO: porównywanie z uwzględnieniem relacji dla "wyznacznik_null_verb"
		
		while(referenceDocument != null && systemResponseDocument != null){
//			String[] refParts = referenceDocument.getName().split("/");
//			String[] sysParts = systemResponseDocument.getName().split("/");
//			if(!sysParts[sysParts.length - 1].equals(refParts[refParts.length - 1].substring(0, refParts[refParts.length - 1].length()-4))){
//				System.out.println(referenceDocument.getName() + " vs. " + systemResponseDocument.getName());
//				return;
//			}
			
			gen.generateFeatures(referenceDocument);
			gen.generateFeatures(systemResponseDocument);
			
			referenceDocument = rewireRelations(referenceDocument, selector, overrideSelector);
			
			evaluator.evaluate(systemResponseDocument, referenceDocument);
			referenceDocument = goldReader.nextDocument();
			systemResponseDocument = sysReader.nextDocument();
		}
		System.out.println("");
		evaluator.printTotal();

	}
	
	private Document rewireRelations(Document document, AbstractAnnotationSelector relationalAnnotations, AbstractAnnotationSelector nonrelationalAnnotations){
		List<Annotation> relAnnotations = relationalAnnotations.selectAnnotations(document);
		List<Annotation> targetAnnotations = nonrelationalAnnotations.selectAnnotations(document);
		
		for(Annotation rAnn : relAnnotations){
			for(Annotation potentialTarget : rAnn.getSentence().getChunks()){
				if(potentialTarget.getTokens().equals(rAnn.getTokens()) && targetAnnotations.contains(potentialTarget)){
					document.rewireSingleRelations(rAnn, potentialTarget);
				}
			}
		}
		
		return document;
	}
}

