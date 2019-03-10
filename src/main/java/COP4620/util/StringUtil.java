package COP4620.util;

public class StringUtil {
    public static int lastIndexLessThan(String string, String search, int maxIndex) {
        int index = string.lastIndexOf(search);
        if (index < maxIndex) {
            return index;
        }
        return lastIndexLessThan(string.substring(0, index), search, maxIndex);
    }
}
