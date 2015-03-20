package g419.crete.api.features.factory.item;

import g419.corpus.structure.Annotation;
import g419.corpus.structure.AnnotationCluster;
import g419.crete.api.features.AbstractFeature;
import g419.crete.api.features.clustermention.following.ClusterMentionClosestFollowingIsReflexivePossesive;

import org.apache.commons.lang3.tuple.Pair;

public class ClusterMentionClosestFollowingIsReflexivePossesiveItem implements IFeatureFactoryItem<Pair<Annotation, AnnotationCluster>, Boolean> {

	@Override
	public AbstractFeature<Pair<Annotation, AnnotationCluster>, Boolean> createFeature() {
		return new ClusterMentionClosestFollowingIsReflexivePossesive();
	}

}
