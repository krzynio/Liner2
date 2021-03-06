package g419.crete.core.features.clusters;

import g419.corpus.structure.*;
import g419.crete.core.features.AbstractFeature;

import java.util.HashSet;
import java.util.Set;

public class ClusterFeatureSentenceFrequency extends AbstractFeature<AnnotationCluster, Float>{

	@Override
	public void generateFeature(AnnotationCluster input) {
		int clusterSentences = 0;
		int totalSentences = 0;
		
		Set<Sentence> clusterSentenceSet = new HashSet<Sentence>();
		
		for(Annotation annotation : input.getAnnotations())
			clusterSentenceSet.add(annotation.getSentence());
		clusterSentences = clusterSentenceSet.size();
		
		Document doc = input.getDocument();
		for(Paragraph paragraph : doc.getParagraphs())
			totalSentences += paragraph.getSentences().size();
				
		this.value = ((float)clusterSentences) / ((float) totalSentences);
	}

	@Override
	public String getName() {
		return "cluster_sentence_frequency";
	}

	@Override
	public Class<AnnotationCluster> getInputTypeClass() {
		return AnnotationCluster.class;
	}

	@Override
	public Class<Float> getReturnTypeClass() {
		return Float.class;
	}

}
