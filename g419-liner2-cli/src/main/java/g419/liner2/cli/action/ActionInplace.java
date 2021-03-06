package g419.liner2.cli.action;

import g419.corpus.io.reader.AbstractDocumentReader;
import g419.corpus.io.reader.ReaderFactory;
import g419.corpus.io.writer.AbstractDocumentWriter;
import g419.corpus.io.writer.WriterFactory;
import g419.corpus.structure.Document;
import g419.lib.cli.Action;
import g419.lib.cli.CommonOptions;
import g419.lib.cli.ParameterException;
import g419.liner2.core.LinerOptions;
import g419.liner2.core.chunker.Chunker;
import g419.liner2.core.chunker.factory.ChunkerManager;
import g419.liner2.core.features.TokenFeatureGenerator;
import org.apache.commons.cli.CommandLine;

import java.io.File;

/**
 * Chunking in pipe mode.
 * @author Maciej Janicki, Michał Marcińczuk
 *
 */
public class ActionInplace extends Action{

    private String input_file = null;
    private String input_format = null;

	public ActionInplace(){
		super("inplace");
        this.setDescription("process documents in place");

        this.options.addOption(CommonOptions.getInputFileFormatOption());
        this.options.addOption(CommonOptions.getInputFileNameOption());
        this.options.addOption(CommonOptions.getOutputFileFormatOption());
        this.options.addOption(CommonOptions.getOutputFileNameOption());
        this.options.addOption(CommonOptions.getFeaturesOption());
        this.options.addOption(CommonOptions.getModelFileOption());
	}

	@Override
	public void parseOptions(final CommandLine line) throws Exception {
        this.input_file = line.getOptionValue(CommonOptions.OPTION_INPUT_FILE);
        this.input_format = line.getOptionValue(CommonOptions.OPTION_INPUT_FORMAT, "ccl");
        LinerOptions.getGlobal().parseModelIni(line.getOptionValue(CommonOptions.OPTION_MODEL));
	}
	
	/**
	 * Module entry function.
	 */
	public void run() throws Exception{
	
        if ( !LinerOptions.isGlobalOption(LinerOptions.OPTION_USED_CHUNKER) ){
			throw new ParameterException("Parameter 'chunker' in 'main' section of model not set");
		}		
	
        AbstractDocumentReader reader = ReaderFactory.get().getStreamReader(this.input_file, this.input_format);
        TokenFeatureGenerator gen = null;
        
        if (!LinerOptions.getGlobal().features.isEmpty()){
            gen = new TokenFeatureGenerator(LinerOptions.getGlobal().features);            
        }
		
		/* Create all defined chunkers. */
        ChunkerManager cm = new ChunkerManager(LinerOptions.getGlobal());
        cm.loadChunkers();

        Chunker chunker = cm.getChunkerByName(LinerOptions.getGlobal().getOptionUse());

        String outputTmpIndex = this.input_file + ".tmp";
        AbstractDocumentWriter writer = WriterFactory.get().getStreamWriter(outputTmpIndex, this.input_format);
        
		Document document = null;
		while ( (document = reader.nextDocument()) != null ){
			System.out.println(document.getUri());
			if ( gen != null ){
				gen.generateFeatures(document);
			}
			chunker.chunkInPlace(document);
			writer.writeDocument(document);
		}

		reader.close();
		writer.close();
		
		File tmp = new File(outputTmpIndex);
		if ( tmp.exists() ){
			tmp.delete();
		}
	}
		
}
