package g419.corpus.io.writer;

import g419.corpus.TerminateException;
import g419.corpus.format.Iob;
import g419.corpus.structure.*;
import io.vavr.control.Option;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;


public class IobStreamWriter extends AbstractDocumentWriter {

    private final BufferedWriter ow;
    private boolean init = false;

    public IobStreamWriter(final OutputStream os) {
        ow = new BufferedWriter(new OutputStreamWriter(os));
    }

    protected void init(final TokenAttributeIndex attributeIndex) {
        if (init) {
            return;
        }
        try {
            final String line = Iob.IOB_HEADER_PREFIX + " " + attributeIndex.getAttributes().stream().collect(Collectors.joining(" "));
            ow.write(line, 0, line.length());
            ow.newLine();
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        init = true;
    }

    @Override
    public void flush() {
        try {
            ow.flush();
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() {
        try {
            ow.close();
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void write(final String line) {
        try {
            ow.write(line, 0, line.length());
            ow.newLine();
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void writeDocument(final Document document) {
        if (!init) {
            init(document.getAttributeIndex());
        }
        write(Iob.IOB_FILE_PREFIX + " " + Option.of(document.getName()).getOrElse(""));
        document.getParagraphs().forEach(this::writeParagraph);
    }

    private void writeParagraph(final Paragraph paragraph) {
        paragraph.getSentences().forEach(this::writeSentence);
    }

    private void writeSentence(final Sentence sentence) {
        try {
            final List<Token> tokens = sentence.getTokens();
            for (int i = 0; i < tokens.size(); i++) {
                writeToken(i, tokens.get(i), sentence);
            }
            ow.newLine();
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void writeToken(final int idx, final Token token, final Sentence sentence)
            throws IOException {
        final StringJoiner line = new StringJoiner(Iob.IOB_COLUMN_SEPARATOR);
        for (int i = 0; i < sentence.getAttributeIndex().getLength(); i++) {
            try {
                line.add(Option.of(token.getAttributeValue(i))
                        .map(v -> v.replaceAll(Iob.IOB_COLUMN_SEPARATOR, ""))
                        .getOrElse("NULL"));
            } catch (final IndexOutOfBoundsException e) {
                throw new TerminateException(String.format("Token attribute with index %d not found in [%s]", i, token.getAttributesAsString()));
            }
        }
        line.add(sentence.getTokenClassLabel(idx, null));
        ow.write(line.toString(), 0, line.length());
        ow.newLine();
    }
}
