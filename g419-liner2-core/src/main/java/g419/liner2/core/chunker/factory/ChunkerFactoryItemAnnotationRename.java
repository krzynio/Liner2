package g419.liner2.core.chunker.factory;


import g419.corpus.ConsolePrinter;
import g419.lib.cli.ParameterException;
import g419.liner2.core.chunker.AnnotationRenameChunker;
import g419.liner2.core.chunker.Chunker;
import org.ini4j.Ini;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ChunkerFactoryItemAnnotationRename extends ChunkerFactoryItem {

	public ChunkerFactoryItemAnnotationRename() {
		super("annotation-rename");
	}

	@Override
	public Chunker getChunker(Ini.Section description, ChunkerManager cm) throws Exception {
        String chunkername = description.get("base-chunker");
        String file = description.get("file");
        Map<String, String> rename = this.loadAnnotationRename(file);
        ConsolePrinter.log("--> AnnotationRenameChunker on  " + chunkername);

        Chunker baseChunker = cm.getChunkerByName(chunkername);
        if (baseChunker == null){
            throw new ParameterException("Undefined base chunker: " + chunkername);
        }
        return new AnnotationRenameChunker(baseChunker, rename);

	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
    private Map<String, String> loadAnnotationRename(String file) throws IOException {
        Map<String, String> rename = new HashMap<String, String>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while(line != null){
            if(!(line.isEmpty() || line.startsWith("#"))){
                String[] parts = line.split(":");
                if ( parts.length == 2 ){
                	rename.put(parts[0].trim(), parts[1].trim());
                }
            }
            line = reader.readLine();
        }
        return rename;
    }
}
