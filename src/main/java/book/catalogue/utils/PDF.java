package book.catalogue.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

public class PDF {

    private String title;
    private List<String> text;
    private int charactersPerLine;
    private int linesPerPage;
    private float ty;
    private float tx = 40f;
    private float fontSize = 12f;

    public PDF(String title, List<?> objects) {
        this.title = title;
        this.text = objects.stream().map(Object::toString).collect(Collectors.toList());
    }

    public byte[] generate() {
        try (PDDocument pdf = new PDDocument()) {
            pdf.getDocumentInformation().setTitle(title);
            PDFont font = PDType0Font.load(pdf, new File("src/main/resources/RobotoMono-Regular.ttf"));
            ty = PDRectangle.A4.getHeight() - tx - font.getFontDescriptor().getCapHeight() / 1000 * fontSize * 1.5f;
            charactersPerLine = (int) ((PDRectangle.A4.getWidth() - tx * 2) / (font.getSpaceWidth() / 1000 * fontSize));
            linesPerPage = (int) ((ty - tx * 2) / (font.getFontDescriptor().getCapHeight() / 1000 * fontSize) / 2 - 0.5f);
            for (List<String> lines : getContent()) {
                createPage(pdf, font, lines);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            pdf.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private void createPage(PDDocument pdf, PDFont font, List<String> lines) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        pdf.addPage(page);
        try (PDPageContentStream contentStream = new PDPageContentStream(pdf, page)) {
            contentStream.beginText();
            contentStream.setFont(font, fontSize * 1.5f);
            contentStream.setLeading(fontSize * 1.5f * 1.5f);
            contentStream.newLineAtOffset(tx, ty);
            contentStream.showText(title);
            contentStream.newLine();
            contentStream.setFont(font, fontSize);
            contentStream.setLeading(fontSize * 1.5f);
            for (String line : lines) {
                contentStream.showText(line);
                contentStream.newLine();
            }
            contentStream.endText();
        }
    }

    private List<List<String>> getContent() {
        List<String> lines = new ArrayList<>();
        for (String str : text) {
            if (str.length() > charactersPerLine) {
                lines.addAll(toLines(str));
            } else {
                lines.add(str);
            }
        }
        int numberOfLines = lines.size();
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < numberOfLines; i += linesPerPage) {
            result.add(lines.subList(i, Math.min(i + linesPerPage, numberOfLines)));
        }
        return result;
    }

    private List<String> toLines(String str) {
        StringBuilder builder = new StringBuilder(charactersPerLine);
        String[] elements = str.split("\\s+");
        List<String> result = new ArrayList<>();
        for (int i = 0; i < elements.length; ++i) {
            if (builder.length() == 0 && elements[i].length() > charactersPerLine) {
                int fullLines = elements[i].length() / charactersPerLine;
                for (int j = 0; j < fullLines; ++j) {
                    result.add(elements[i].substring(charactersPerLine * j, charactersPerLine * (j + 1)));
                }
                builder.append(elements[i], charactersPerLine * fullLines, elements[i].length());
            } else if (builder.length() == 0) {
                builder.append(elements[i]);
            } else if (builder.length() + elements[i].length() < charactersPerLine) {
                builder.append(' ').append(elements[i]);
            } else {
                result.add(builder.toString());
                builder.setLength(0);
                --i;
            }
        }
        if (builder.length() > 0) {
            result.add(builder.toString());
        }
        return result;
    }

}
