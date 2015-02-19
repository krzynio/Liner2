package g419.crete.api.features.factory.item;

import g419.corpus.structure.Annotation;
import g419.corpus.structure.AnnotationCluster;
import g419.crete.api.features.AbstractFeature;
import g419.crete.api.features.clustermention.ClusterMentionGenderMatch;

import org.apache.commons.lang3.tuple.Pair;

public class ClusterMentionGenderMatchFactoryItem   implements IFeatureFactoryItem<Pair<Annotation, AnnotationCluster>, Float> {

	@Override
	public AbstractFeature<Pair<Annotation, AnnotationCluster>, Float> createFeature() {
		return new ClusterMentionGenderMatch();
	}

}
