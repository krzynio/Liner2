package g419.corpus.io.writer;

import com.google.common.collect.Lists;
import g419.corpus.DocumentElementIdFixer;
import g419.corpus.io.writer.tei.*;
import g419.corpus.structure.Document;
import g419.corpus.structure.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class TeiWriter extends AbstractDocumentWriter {

    final List<TeiFileWriter> writers = Lists.newArrayList();
    private Logger logger = LoggerFactory.getLogger(getClass());

    public TeiWriter(final OutputStream text, final OutputStream metadata, final OutputStream annSegmentation, final OutputStream annMorphosyntax,
                     final OutputStream annProps, final OutputStream annNamed, final OutputStream annMentions, final OutputStream annChunks, final OutputStream annAnnotations,
                     final OutputStream annCoreference, final OutputStream annRelations, final String documentName) throws XMLStreamException {
        List<Pattern> mentionPatterns = Lists.newArrayList(Pattern.compile("anafora_wyznacznik"));
        mentionPatterns.add(Pattern.compile("^(landmark|spatial_indicator|trajector|spatial_object|region|path).*", Pattern.CASE_INSENSITIVE));
        TeiPointerManager pointers = new TeiPointerManager();
        writers.add(new TeiFileTextWriter(text, "text.xml", pointers, documentName));
        writers.add(new TeiFileMetadataWriter(metadata, "text.xml", pointers));
        writers.add(new TeiFileSegmentationWriter(annSegmentation, "ann_segmentation.xml", pointers));
        writers.add(new TeiFileMorphosyntaxWriter(annMorphosyntax, "ann_morphosyntax.xml", pointers));
        writers.add(new TeiFilePropsWriter(annProps, "ann_props.xml", pointers));
        writers.add(new TeiFileAnnotationsWriter(annNamed, "ann_named.xml", pointers, Arrays.asList(Pattern.compile(".*nam"))));
        writers.add(new TeiFileAnnotationsWriter(annMentions, "ann_mentions.xml", pointers, mentionPatterns));
        writers.add(new TeiFileAnnotationsWriter(annChunks, "ann_chunks.xml", pointers, Arrays.asList(Pattern.compile("^chunk_.*"))));
        writers.add(new TeiFileAnnotationsWriter(annAnnotations, "ann_annotations.xml", pointers));
        writers.add(new TeiFileRelationClusterWriter(annCoreference, "ann_coreference.xml", pointers, Relation.COREFERENCE));
        writers.add(new TeiFileRelationWriter(annRelations, "ann_relations.xml", pointers));
    }

    @Override
    public void flush() {}

    @Override
    public void writeDocument(Document document) {
        DocumentElementIdFixer idFixer = new DocumentElementIdFixer();
        idFixer.fixIds(document);
        for (TeiFileWriter writer : writers) {
            try {
                writer.writeDocument(document);
            } catch (XMLStreamException e) {
                logger.error("Failed to write Tei file", e);
            }
        }
    }

    @Override
    public void close() {
        for (TeiFileWriter writer : writers) {
            try {
                writer.close();
            } catch (XMLStreamException ex) {
                logger.error("Failed to close Tei file", ex);
            } catch (IOException ex) {
                logger.error("Failed to close Tei file", ex);
            }
        }
    }
}