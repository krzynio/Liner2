package g419.crete.core.features.factory.item;

import g419.corpus.structure.Annotation;
import g419.corpus.structure.AnnotationCluster;
import g419.crete.core.features.AbstractFeature;
import g419.crete.core.features.clustermention.preceeding.ClusterMentionClosestPreceedingPreceededByCoordConj;
import org.apache.commons.lang3.tuple.Pair;

public class ClusterMentionClosestPreceedingPreceededByCoordConjItem implements IFeatureFactoryItem<Pair<Annotation, AnnotationCluster>, Boolean> {

	@Override
	public AbstractFeature<Pair<Annotation, AnnotationCluster>, Boolean> createFeature() {
		return new ClusterMentionClosestPreceedingPreceededByCoordConj();
	}

}
