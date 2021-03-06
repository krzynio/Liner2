package g419.crete.core.resolver;

import g419.corpus.structure.Annotation;
import g419.corpus.structure.Document;
import g419.corpus.structure.Relation;
import g419.crete.core.annotation.AbstractAnnotationSelector;
import g419.crete.core.instance.MentionPairClassificationInstance;
import g419.crete.core.resolver.disambiguator.IDisambiguator;
import g419.crete.core.resolver.disambiguator.MentionPairClosestDisambiguator;
import weka.classifiers.Classifier;
import weka.core.Instance;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WekaJ48MentionPairResolver extends AbstractCreteResolver<Classifier, MentionPairClassificationInstance<Integer>, Instance, Integer>{

	private int totalPositive;
	private int totalAccepted;
	
	// TODO: Fix the initialization
	private IDisambiguator<MentionPairClassificationInstance<Integer>> disambiguator = new MentionPairClosestDisambiguator();
		
	@Override
	public Document resolveDocument(Document document, AbstractAnnotationSelector selector, AbstractAnnotationSelector singletonSelector) {
		Document d = super.resolveDocument(document, selector, singletonSelector);
		System.out.println(totalPositive);
		System.out.println(totalAccepted);
		return d;
	};
	
	
	@Override
	protected Document resolveMention(Document document, Annotation mention, List<MentionPairClassificationInstance<Integer>> instancesForMention) {
		System.out.println("Mention: " + mention);
		for(MentionPairClassificationInstance instance : instancesForMention) System.out.println(instance.getAntecedent());
		List<Integer> labels = this.classifier.classify(this.converter.convertInstances(instancesForMention));
 		List<MentionPairClassificationInstance<Integer>> correctPairs = IntStream
				.range(0, labels.size())
				.parallel()
				// TODO: fixme for positive label recognition
				// This filter is hardcoded - fix it - add more accurate criterion for PositiveLabel recognition
				.filter(index -> labels.get(index) > 0)
				.mapToObj(index -> instancesForMention.get(index))
				.collect(Collectors.toList());
		
		MentionPairClassificationInstance<Integer> chosenInstance = disambiguator.disambiguate(correctPairs);
		
		if(chosenInstance == null) return document; // Return unchanged document (mention does not have coreferential cluster)
		
		totalPositive +=correctPairs.size();
		totalAccepted++;
		
		//DEBUG
		System.out.println(mention + " <--- COREFERENCE ---> " + chosenInstance.getAntecedent());
		 
		Relation mentionRelation = chosenInstance.toRelation(Relation.COREFERENCE, Relation.COREFERENCE, document); 
		document.addRelation(mentionRelation);
		return document;
	}
	
	@Override public Class<Classifier> getModelClass() {return Classifier.class;}
	@Override public Class<MentionPairClassificationInstance<Integer>> getAbstractInstanceClass() {
		MentionPairClassificationInstance<Integer> inst = new MentionPairClassificationInstance<>();
		return (Class<MentionPairClassificationInstance<Integer>>) inst.getClass();
	}
	@Override public Class<Instance> getClassifierInstanceClass() {return Instance.class;}
	@Override public Class<Integer> getLabelClass() {return Integer.class;	}

}
