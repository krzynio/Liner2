package g419.crete.core.features.factory;

import g419.corpus.structure.Annotation;
import g419.corpus.structure.AnnotationCluster;
import g419.crete.core.features.AbstractFeature;
import g419.crete.core.features.factory.item.AnnotationFirstVerbInSentenceItem;
import g419.crete.core.features.factory.item.AnnotationFollowingConjunctionByLikeItem;
import g419.crete.core.features.factory.item.AnnotationFollowingRelativePronounCaseItem;
import g419.crete.core.features.factory.item.AnnotationFollowingRelativePronounDistanceItem;
import g419.crete.core.features.factory.item.AnnotationGenderFeatureFactoryItem;
import g419.crete.core.features.factory.item.AnnotationNumberFeatureFactoryItem;
import g419.crete.core.features.factory.item.AnnotationPairFeatureCosineBaseSimilarityItem;
import g419.crete.core.features.factory.item.AnnotationPairFeatureCosineSimilarityItem;
import g419.crete.core.features.factory.item.AnnotationPairFeatureGenderAgreementItem;
import g419.crete.core.features.factory.item.AnnotationPairFeatureGenderMascTolerantAgreementItem;
import g419.crete.core.features.factory.item.AnnotationPairFeatureNumberAgreementItem;
import g419.crete.core.features.factory.item.AnnotationPairFeaturePersonAgreementItem;
import g419.crete.core.features.factory.item.AnnotationPairFeatureSameChanNameItem;
import g419.crete.core.features.factory.item.AnnotationPairFeatureSameHeadBaseItem;
import g419.crete.core.features.factory.item.AnnotationPairFeatureTokenCountDiffItem;
import g419.crete.core.features.factory.item.AnnotationPersonFeatureFactoryItem;
import g419.crete.core.features.factory.item.AnnotationPreceedingConjunctionByLikeItem;
import g419.crete.core.features.factory.item.AnnotationPreceedingCoordinateConjunctionItem;
import g419.crete.core.features.factory.item.AnnotationPreceedingRelativePronounCaseItem;
import g419.crete.core.features.factory.item.AnnotationPreceedingRelativePronounDistanceItem;
import g419.crete.core.features.factory.item.AnnotationPreceedingSubordinateConjunctionItem;
import g419.crete.core.features.factory.item.ClusterDocumentIdFeatureFactoryItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingFollowedByCoordConjItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingFollowedBySubordConjItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingInSameSentenceFactoryItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingIsReflexivePossesiveItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingMentionCaseItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingMentionDistanceItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingMentionGenderItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingMentionNumberItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingMentionPersonItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingMentionTypeItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingPositionInSentenceItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingPreceededByCoordConjItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingPreceededBySubordConjItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingPredicateDistanceItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingSentenceDistanceItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestFollowingTokenDistanceItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingFollowedByCoordConjItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingFollowedByRelativeItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingFollowedBySubordConjItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingInSameSentenceFactoryItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingIsObjectItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingIsReflexivePossesiveItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingIsSubjectItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingMentionCaseItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingMentionDistanceItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingMentionGenderItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingMentionNumberItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingMentionPersonItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingMentionTypeItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingPositionInSentenceItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingPreceededByCoordConjItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingPreceededBySubordConjItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingPredicateDistanceItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingSentenceDistanceItem;
import g419.crete.core.features.factory.item.ClusterMentionClosestPreceedingTokenDistanceItem;
import g419.crete.core.features.factory.item.ClusterMentionCountFeatureFactoryItem;
import g419.crete.core.features.factory.item.ClusterMentionGenderMatchFactoryItem;
import g419.crete.core.features.factory.item.ClusterMentionNumberMatchFactoryItem;
import g419.crete.core.features.factory.item.ClusterMentionPersonMatchFactoryItem;
import g419.crete.core.features.factory.item.ClusterMentionPreceedingEntityRecencyItem;
import g419.crete.core.features.factory.item.ClusterSentenceFrequencyFeatureFactoryItem;
import g419.crete.core.features.factory.item.ClusterTermFrequencyFeatureFactoryItem;
import g419.crete.core.features.factory.item.IFeatureFactoryItem;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Damerau;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.LongestCommonSubsequence;
import info.debatty.java.stringsimilarity.NGram;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.QGram;
import info.debatty.java.stringsimilarity.SorensenDice;

import java.util.HashMap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;



public class FeatureFactory{
	private static final String PREFIX_SEPARATOR = "_";
	
	private FeatureFactory(){
		prefixToClass = new HashMap<String, Class<?>>();
		prefixToClass.put("annotation", Annotation.class);
		prefixToClass.put("annotationpair", ((Pair<Annotation, Annotation>) new ImmutablePair<Annotation, Annotation>(null, null)).getClass());
		prefixToClass.put("mention", Annotation.class);
		prefixToClass.put("cluster", AnnotationCluster.class);
		prefixToClass.put("clustermention", ((Pair<Annotation, AnnotationCluster>) new ImmutablePair<Annotation, AnnotationCluster>(null, null)).getClass());
		
		features = new HashMap<Pair<Class<?>,String>, IFeatureFactoryItem<?,?>>();
		// ----------------------- MENTION FEATURES ---------------------------
		register(new AnnotationGenderFeatureFactoryItem());
		register(new AnnotationNumberFeatureFactoryItem());
		register(new AnnotationPersonFeatureFactoryItem());
		
		// TODO: position in sentence, #verbs in sentence, position in verbs
		register(new AnnotationFirstVerbInSentenceItem());
		
		
		// TODO: Following		
		register(new AnnotationPreceedingConjunctionByLikeItem());
		// TODO: Following
		register(new AnnotationPreceedingCoordinateConjunctionItem());
				
		register(new AnnotationPreceedingRelativePronounCaseItem());
		register(new AnnotationFollowingRelativePronounCaseItem());
		
		register(new AnnotationPreceedingRelativePronounDistanceItem());
		register(new AnnotationFollowingRelativePronounDistanceItem());
		
		//TODO: following
		register(new AnnotationPreceedingSubordinateConjunctionItem(1));
		register(new AnnotationPreceedingSubordinateConjunctionItem(2));
		register(new AnnotationPreceedingSubordinateConjunctionItem(3));
		register(new AnnotationPreceedingSubordinateConjunctionItem(4));
		register(new AnnotationPreceedingSubordinateConjunctionItem(5));
		
		// TODO: preceding
		
		register(new AnnotationFollowingConjunctionByLikeItem(1));
		register(new AnnotationFollowingConjunctionByLikeItem(2));
		register(new AnnotationFollowingConjunctionByLikeItem(3));
		register(new AnnotationFollowingConjunctionByLikeItem(4));
		register(new AnnotationFollowingConjunctionByLikeItem(5));
		
		// ----------------------- MENTION PAIR FEATURES ---------------------------
		register(new AnnotationPairFeatureCosineBaseSimilarityItem());
		register(new AnnotationPairFeatureCosineSimilarityItem());
		register(new AnnotationPairFeatureGenderAgreementItem());
		register(new AnnotationPairFeatureGenderMascTolerantAgreementItem());
		register(new AnnotationPairFeatureNumberAgreementItem());
		register(new AnnotationPairFeaturePersonAgreementItem());
		register(new AnnotationPairFeatureSameChanNameItem());
		register(new AnnotationPairFeatureSameHeadBaseItem());
		register(new AnnotationPairFeatureTokenCountDiffItem());
		register(new AnnotationPairFeatureSemanticLinkAgPItem());
		register(new AnnotationPairFeaturePronounLinkItem());
		
		register(new AnnotationPairFeatureSameBeginWordItem());
		register(new AnnotationPairFeatureSameBeginWordBaseItem());
		register(new AnnotationPairFeatureSameEndWordItem());
		register(new AnnotationPairFeatureSameEndWordBaseItem());
		register(new AnnotationPairFeatureSameMiddleWordItem());
		register(new AnnotationPairFeatureSameMiddleWordBaseItem());
		register(new AnnotationPairFeatureAcroItem());
		register(new AnnotationPairFeatureFirstLengthItem());
		register(new AnnotationPairFeatureSecondLengthItem());
		// --------------------- STRING DISTANCES -----------------------
		register(new AnnotationPairFeatureStringDistanceItem(new NormalizedLevenshtein(), "normalized_levenshtein", false));
		register(new AnnotationPairFeatureStringDistanceItem(new Damerau(), "damerau", false));
		register(new AnnotationPairFeatureStringDistanceItem(new JaroWinkler(), "jaro_winkler", false));
		register(new AnnotationPairFeatureStringDistanceItem(new LongestCommonSubsequence(), "lcs", false));
//		register(new AnnotationPairFeatureStringDistanceItem(new MetricLCS(), "lcs_metric", false));
		register(new AnnotationPairFeatureStringDistanceItem(new NGram(1), "ngram1", false));
		register(new AnnotationPairFeatureStringDistanceItem(new NGram(2), "ngram2", false));
		register(new AnnotationPairFeatureStringDistanceItem(new NGram(3), "ngram3", false));
		register(new AnnotationPairFeatureStringDistanceItem(new QGram(1), "qgram1", false));
		register(new AnnotationPairFeatureStringDistanceItem(new QGram(2), "qgram2", false));
		register(new AnnotationPairFeatureStringDistanceItem(new QGram(3), "qgram3", false));
		register(new AnnotationPairFeatureStringDistanceItem(new Cosine(), "cosine", false));
		register(new AnnotationPairFeatureStringDistanceItem(new Jaccard(1), "jaccard1", false));
		register(new AnnotationPairFeatureStringDistanceItem(new Jaccard(2), "jaccard2", false));
		register(new AnnotationPairFeatureStringDistanceItem(new Jaccard(3), "jaccard3", false));
		register(new AnnotationPairFeatureStringDistanceItem(new SorensenDice(1), "sorensen1", false));
		register(new AnnotationPairFeatureStringDistanceItem(new SorensenDice(2), "sorensen2", false));
		register(new AnnotationPairFeatureStringDistanceItem(new SorensenDice(3), "sorensen3", false));
		
		register(new AnnotationPairFeatureChannelItem(false));
		register(new AnnotationPairFeatureChannelItem(true));
		
		register(new AnnotationPairFeatureTextItem());
		register(new AnnotationPairFeatureDocumentIdItem());
		
		// ----------------------- CLUSTER FEATURES ----------------------------
		register(new ClusterDocumentIdFeatureFactoryItem());
		register(new ClusterMentionCountFeatureFactoryItem());
		register(new ClusterTermFrequencyFeatureFactoryItem());
		register(new ClusterSentenceFrequencyFeatureFactoryItem());
		
		// ----------------------- MENTION_CLUSTER PAIR FEATURES -------
			// ------ Preceding ----
		register(new ClusterMentionClosestPreceedingTokenDistanceItem());
		register(new ClusterMentionClosestPreceedingSentenceDistanceItem());
		register(new ClusterMentionClosestPreceedingInSameSentenceFactoryItem());
		register(new ClusterMentionClosestPreceedingMentionGenderItem());
		register(new ClusterMentionClosestPreceedingMentionNumberItem());
		register(new ClusterMentionClosestPreceedingMentionPersonItem());
		register(new ClusterMentionClosestPreceedingPositionInSentenceItem());
		
		register(new ClusterMentionClosestPreceedingFollowedByCoordConjItem());
		register(new ClusterMentionClosestPreceedingPreceededByCoordConjItem());
		
		register(new ClusterMentionClosestPreceedingFollowedBySubordConjItem(1));
		register(new ClusterMentionClosestPreceedingFollowedBySubordConjItem(2));
		register(new ClusterMentionClosestPreceedingFollowedBySubordConjItem(3));
		register(new ClusterMentionClosestPreceedingFollowedBySubordConjItem(4));
		register(new ClusterMentionClosestPreceedingFollowedBySubordConjItem(5));
		
		register(new ClusterMentionClosestPreceedingPreceededBySubordConjItem(1));
		register(new ClusterMentionClosestPreceedingPreceededBySubordConjItem(2));
		register(new ClusterMentionClosestPreceedingPreceededBySubordConjItem(3));
		register(new ClusterMentionClosestPreceedingPreceededBySubordConjItem(4));
		register(new ClusterMentionClosestPreceedingPreceededBySubordConjItem(5));
		
		register(new ClusterMentionClosestPreceedingFollowedByRelativeItem());
		register(new ClusterMentionClosestPreceedingIsReflexivePossesiveItem());
		register(new ClusterMentionClosestPreceedingMentionCaseItem());
		register(new ClusterMentionClosestPreceedingMentionDistanceItem());
		register(new ClusterMentionClosestPreceedingMentionTypeItem());
		register(new ClusterMentionClosestPreceedingPredicateDistanceItem());
		
		register(new ClusterMentionClosestPreceedingIsSubjectItem());
		register(new ClusterMentionClosestPreceedingIsObjectItem());
		
		register(new ClusterMentionPreceedingEntityRecencyItem(1));
		register(new ClusterMentionPreceedingEntityRecencyItem(2));
		register(new ClusterMentionPreceedingEntityRecencyItem(3));
		register(new ClusterMentionPreceedingEntityRecencyItem(4));
		register(new ClusterMentionPreceedingEntityRecencyItem(5));
		
			// ------ Following----		
		register(new ClusterMentionClosestFollowingTokenDistanceItem());
		register(new ClusterMentionClosestFollowingSentenceDistanceItem());
		register(new ClusterMentionClosestFollowingInSameSentenceFactoryItem());
		register(new ClusterMentionClosestFollowingMentionGenderItem());
		register(new ClusterMentionClosestFollowingMentionNumberItem());
		register(new ClusterMentionClosestFollowingMentionPersonItem());
		register(new ClusterMentionClosestFollowingPositionInSentenceItem());
		
		register(new ClusterMentionClosestFollowingFollowedByCoordConjItem());
		register(new ClusterMentionClosestFollowingPreceededByCoordConjItem());
		
		register(new ClusterMentionClosestFollowingFollowedBySubordConjItem(1));
		register(new ClusterMentionClosestFollowingFollowedBySubordConjItem(2));
		register(new ClusterMentionClosestFollowingFollowedBySubordConjItem(3));
		register(new ClusterMentionClosestFollowingFollowedBySubordConjItem(4));
		register(new ClusterMentionClosestFollowingFollowedBySubordConjItem(5));
		
		register(new ClusterMentionClosestFollowingPreceededBySubordConjItem(1));
		register(new ClusterMentionClosestFollowingPreceededBySubordConjItem(2));
		register(new ClusterMentionClosestFollowingPreceededBySubordConjItem(3));
		register(new ClusterMentionClosestFollowingPreceededBySubordConjItem(4));
		register(new ClusterMentionClosestFollowingPreceededBySubordConjItem(5));

//		register(new ClusterMentionClosestFollowingFollowedByRelativeItem());
		register(new ClusterMentionClosestFollowingIsReflexivePossesiveItem());
		register(new ClusterMentionClosestFollowingMentionCaseItem());
		register(new ClusterMentionClosestFollowingMentionDistanceItem());
		register(new ClusterMentionClosestFollowingMentionTypeItem());
		register(new ClusterMentionClosestFollowingPredicateDistanceItem());
		
			// ------ Rest ----
		register(new ClusterMentionGenderMatchFactoryItem());
		register(new ClusterMentionNumberMatchFactoryItem());
		register(new ClusterMentionPersonMatchFactoryItem());
	}
	
	private static class FactoryHolder {
        private static final FeatureFactory FACTORY = new FeatureFactory();
    }
	public static FeatureFactory getFactory(){
		return FactoryHolder.FACTORY;
	}
	
	private HashMap<String, Class<?>> prefixToClass;
	private HashMap<Pair<Class<?>, String>, IFeatureFactoryItem<?, ?>> features;
	
	
	public <T extends Object> void register(IFeatureFactoryItem<T, ?> feature){
		String name = feature.createFeature().getName();
		features.put(new ImmutablePair<Class<?>, String>(getPrefixClass(extractPrefix(name)), name), feature);
	}
		
	@SuppressWarnings("unchecked")
	public <T extends Object> AbstractFeature<T, ?> getFeature(Class<T> cls, String name){
		if( features.get(new ImmutablePair<Class<T>, String>(cls, name)) == null) System.out.println("No such feature: "+ name);
		return ((IFeatureFactoryItem<T, ?>) features.get(new ImmutablePair<Class<T>, String>(cls, name))).createFeature();
	}
	
	@SuppressWarnings("unchecked")
	public AbstractFeature<?, ?> getFeature(String name){
		String prefix = extractPrefix(name);
		Class<?> cls = getPrefixClass(prefix);
		return getFeature(cls, name);
	}
	
	private String extractPrefix(String name){
		String prefix = name.split(PREFIX_SEPARATOR)[0];
		return prefix;
	}
	
	public Class<?> getFeatureClass(String feature){
		return getPrefixClass(extractPrefix(feature));
	}
	
	public Class<?> getPrefixClass(String prefix){
		return prefixToClass.get(prefix);
	}
	
}