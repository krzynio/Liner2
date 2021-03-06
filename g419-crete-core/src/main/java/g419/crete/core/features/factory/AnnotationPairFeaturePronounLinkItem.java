package g419.crete.core.features.factory;

import g419.corpus.structure.Annotation;
import g419.crete.core.features.AbstractFeature;
import g419.crete.core.features.annotations.pair.AnnotationPairFeaturePronounLink;
import g419.crete.core.features.annotations.pair.AnnotationPairFeatureSemanticLinkAgP;
import g419.crete.core.features.factory.item.IFeatureFactoryItem;
import org.apache.commons.lang3.tuple.Pair;

public class AnnotationPairFeaturePronounLinkItem implements IFeatureFactoryItem<Pair<Annotation, Annotation>, Float> {
	
	@Override
	public AbstractFeature<Pair<Annotation, Annotation>, Float>  createFeature() {
		AnnotationPairFeatureSemanticLinkAgPItem agpItem = new AnnotationPairFeatureSemanticLinkAgPItem();
		return new AnnotationPairFeaturePronounLink((AnnotationPairFeatureSemanticLinkAgP) agpItem.createFeature());
	}


}
