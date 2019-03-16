package COP4620.util;

public class ArrayUtil {
    @SafeVarargs
    public static <T> T[] asArray(T... t) {
        return (T[]) t;
    }
}
