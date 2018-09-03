package book.catalogue.utils;

import java.util.List;

public class Utils {

    public static boolean nullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static String getOrDefaultIfEmpty(String string, String defaultString) {
        return (string != null && string.isEmpty()) ? defaultString : string;
    }

    public static Short parseShort(String string) {
        try {
            return Short.parseShort(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
