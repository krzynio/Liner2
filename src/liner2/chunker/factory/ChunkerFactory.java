package liner2.chunker.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import liner2.Main;
import liner2.chunker.Chunker;

public class ChunkerFactory {

	private static ChunkerFactory factory = null;
	
	private ArrayList<ChunkerFactoryItem> items = new ArrayList<ChunkerFactoryItem>();
	private HashMap<String, Chunker> chunkers = new HashMap<String, Chunker>(); 
	
	private ChunkerFactory(){
//		this.items.add(new ChunkerFactoryItemHeuristics());
//		this.items.add(new ChunkerFactoryItemGazetteers());
//		this.items.add(new ChunkerFactoryItemLingpipeTrain());
//		this.items.add(new ChunkerFactoryItemNLTKTrain());
//		this.items.add(new ChunkerFactoryItemNLTKLoad());
		this.items.add(new ChunkerFactoryItemCRFPPTrain());
		this.items.add(new ChunkerFactoryItemCRFPPLoad());
//		this.items.add(new ChunkerFactoryItemRescore());
	}
	
	/**
	 * Get current ChunkerFactory. If the factory does not exist then create it.
	 * @return
	 */
	private static ChunkerFactory get(){
		if ( ChunkerFactory.factory == null )
			ChunkerFactory.factory = new ChunkerFactory();
		return ChunkerFactory.factory;
	}
	
	/**
	 * Get human-readable description of chunker commands.
	 * @return
	 */
	public static String getDescription(){
		StringBuilder sb = new StringBuilder();
		for (ChunkerFactoryItem item : ChunkerFactory.get().items)
			sb.append("  " + item.getPattern() + "\n");
		return sb.toString();
	}
	
	/**
	 * Creates a chunker according to the description
	 * @param description
	 * @return
	 * @throws Exception 
	 */
	public static Chunker createChunker(String description) throws Exception{
		Main.log("-> Setting up chunker: " + description);
		
		if (true)
			for (ChunkerFactoryItem item : ChunkerFactory.get().items)
				if ( item.getPattern().matcher(description).find() )
					return item.getChunker(description);
				
		return null;
	}
	
	/**
	 * Creates a hash of chunkers according to the description
	 * @param description
	 * @return
	 * @throws Exception 
	 */
	public static void loadChunkers(ArrayList<String> descriptions)
		throws Exception {
		for (String desc : descriptions) {
			int pos = desc.indexOf(':');
			if (pos == -1)
				throw new Exception("Invalid chunker name.");
			String chunkerName = desc.substring(0, pos);
			String chunkerDesc = desc.substring(pos+1);
			Chunker chunker = ChunkerFactory.createChunker(chunkerDesc);
			if (chunker != null)
				ChunkerFactory.get().chunkers.put(chunkerName, chunker);
			else
				throw new Error(String.format("Chunker description '%s' not recognized", 
						chunkerDesc));
		}
	}
	
	
	/**
	 * Validate a chunker description.
	 * @param description
	 * @return
	 */
	public boolean parse(String description){
		for (ChunkerFactoryItem item : this.items)
			if (item.getPattern().matcher(description).find())
				return true;
		return false;
	}
	
	/**
	 * Create chunker pipe according to given description. The chunker names
	 * must be provided in the list of chunker description passed to the
	 * constructor.
	 * 
	 * Example: c1 --- get single chunker named `c1`
	 * 
	 * @param description
	 * @return
	 */
	public static Chunker getChunkerPipe(String description){
		/**
		 * TODO
		 * teraz zakładamy, że opis chunkera składa się tylko z pojedynczej nazwy chunkera.
		 */
		if ( ChunkerFactory.get().chunkers.containsKey(description))
			return ChunkerFactory.get().chunkers.get(description);
		else
			throw new Error(String.format("Chunker '%s' not defined", description));
	}
}
