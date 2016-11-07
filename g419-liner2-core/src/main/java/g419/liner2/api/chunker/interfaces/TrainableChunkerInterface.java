package g419.liner2.api.chunker.interfaces;

import g419.corpus.structure.Document;

public interface TrainableChunkerInterface {

	public void addTrainingData(Document document) throws Exception;
	
	public void train() throws Exception;
	
}
