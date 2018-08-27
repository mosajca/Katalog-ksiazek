package book.catalogue.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

public class CSV {

    public static String toStringCSV(List<Object[]> records) {
        StringBuilder builder = new StringBuilder();
        try (CSVPrinter printer = new CSVPrinter(builder, CSVFormat.DEFAULT)) {
            printer.printRecords(records);
            printer.flush();
            return builder.toString();
        } catch (IOException e) {
            return "";
        }
    }

    public static List<String[]> fromStringCSV(String csv) {
        try (CSVParser parser = CSVParser.parse(csv, CSVFormat.DEFAULT)) {
            return StreamSupport.stream(parser.spliterator(), false)
                    .map(record -> StreamSupport.stream(record.spliterator(), false).toArray(String[]::new))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

}
