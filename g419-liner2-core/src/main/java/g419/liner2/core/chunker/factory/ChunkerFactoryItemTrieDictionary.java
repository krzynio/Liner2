package g419.liner2.core.chunker.factory;

import g419.liner2.core.chunker.Chunker;
import g419.liner2.core.chunker.TrieDictionaryChunker;
import g419.liner2.core.tools.TrieDictNode;
import org.apache.log4j.Logger;
import org.ini4j.Ini;


// TODO: Auto-generated Javadoc
/**
 * The Class ChunkerFactoryItemTrieDictionary.
 *
 * @author Michał Marcińczuk
 */
public class ChunkerFactoryItemTrieDictionary extends ChunkerFactoryItem {

	/**
	 * Instantiates a new chunker factory item trie dictionary.
	 */
	public ChunkerFactoryItemTrieDictionary() {
		super("trie-dict");
	}

	/* (non-Javadoc)
	 * @see g419.liner2.core.chunker.factory.ChunkerFactoryItem#getChunker(org.ini4j.Profile.Section, g419.liner2.core.chunker.factory.ChunkerManager)
	 */
	@Override
	public Chunker getChunker(Ini.Section description, ChunkerManager cm) throws Exception {
        TrieDictNode dict = null;
        
        String dictionaryPath = description.get("dictionary");
		String annotationName = description.get("annotation");
		String wordSource = description.get("word");
        if ( dictionaryPath == null ){
        	dict = new TrieDictNode(false);
        	Logger.getLogger(this.getClass()).error("Brak parametru 'dictionary' w opisie chunkera rule-road");
        } else{
        	dict = TrieDictNode.loadPlain(dictionaryPath);
        }
        
        return new TrieDictionaryChunker(dict, annotationName, wordSource);
	}

}
