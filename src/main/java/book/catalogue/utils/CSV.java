package book.catalogue.utils;

import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSV {

    private static final CSVFormat DEFAULT_SEMICOLON = CSVFormat.DEFAULT.withRecordSeparator(';');

    public static byte[] toByteArrayCSV(List<Object[]> records) {
        try {
            StringBuilder builder = new StringBuilder();
            createCSV(builder, CSVFormat.DEFAULT, records);
            return builder.toString().getBytes("UTF-8");
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public static String toStringCSV(List<Object[]> records) {
        try {
            StringBuilder builder = new StringBuilder();
            createCSV(builder, DEFAULT_SEMICOLON, records);
            return builder.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private static void createCSV(Appendable out, CSVFormat format, List<Object[]> records) throws IOException {
        try (CSVPrinter printer = new CSVPrinter(out, format)) {
            printer.printRecords(records);
            printer.flush();
        }
    }

}
