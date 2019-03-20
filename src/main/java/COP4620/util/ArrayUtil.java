package COP4620.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayUtil {
    @SafeVarargs
    public static <T> T[] asArray(T... t) {
        return (T[]) t;
    }

    public static <T> T[] addArrays(T[] base, T... add) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, base);
        Collections.addAll(list, add);
        return (T[]) list.toArray();
    }
}
