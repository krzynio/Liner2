package g419.crete.core.instance.generator;

import g419.corpus.structure.*;
import g419.crete.core.annotation.AbstractAnnotationSelector;
import g419.crete.core.instance.ClusterRankingInstance;
import g419.crete.core.instance.ClusterRankingTrainingDiffInstance;

import java.util.ArrayList;
import java.util.List;

public class ClusterRankingTrainingInstanceGenerator extends AbstractCreteInstanceGenerator<ClusterRankingTrainingDiffInstance, Integer>{
	

	@Override
	public List<ClusterRankingTrainingDiffInstance> generateInstances(Document document, AbstractAnnotationSelector selector, AbstractAnnotationSelector singletonSelector) {
		ArrayList<ClusterRankingTrainingDiffInstance> trainingInstances = new ArrayList<ClusterRankingTrainingDiffInstance>();
		List<Annotation> mentions = selector.selectAnnotations(document);
		List<Annotation> singletons = singletonSelector.selectAnnotations(document);
		for(Annotation mention : mentions)
			trainingInstances.addAll(generateInstancesForMention(document, mention, mentions, singletons));
		
		return trainingInstances;
	}

	@Override
	public List<ClusterRankingTrainingDiffInstance> generateInstancesForMention(Document document, Annotation mention, List<Annotation> mentions, List<Annotation> singletons){
		ArrayList<ClusterRankingTrainingDiffInstance> mentionInstances = new ArrayList<ClusterRankingTrainingDiffInstance>();
		ArrayList<ClusterRankingInstance> intermediateInstances = new ArrayList<ClusterRankingInstance>();

		for(AnnotationCluster cluster : AnnotationClusterSet.fromRelationSetWithSingletons(document, Relation.COREFERENCE, Relation.COREFERENCE, document.getRelations(Relation.COREFERENCE), singletons).getClusters()){
			AnnotationCluster preceedingCluster = cluster.getPreceedingCluster(mention, mentions);
			if(preceedingCluster.getAnnotations().size() > 0){
				Integer label = cluster.getAnnotations().contains(mention) ? 2 : 1;
				intermediateInstances.add(new ClusterRankingInstance(mention, cluster, label, null));
			}
		}
		
		for(ClusterRankingInstance instance1 : intermediateInstances)
			for(ClusterRankingInstance instance2 : intermediateInstances)
				if(instance1.getLabel() != instance2.getLabel())
					mentionInstances.add(new ClusterRankingTrainingDiffInstance(mention, instance1.getCluster().getHolder(), instance2.getCluster().getHolder(), instance1.getLabel() - instance2.getLabel(), featureNames));
		
		return mentionInstances;
	}

}
