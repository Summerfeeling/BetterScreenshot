package dev.summerfeeling.betterscreenshot.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SortUtils {

    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKeys(Map<K, V> toSort) {
        Map<K, V> results = new LinkedHashMap<>();
        toSort.entrySet().stream().sorted(Collections.reverseOrder(Entry.comparingByKey())).forEachOrdered(entry -> results.put(entry.getKey(), entry.getValue()));
        return results;
    }

    public static <K extends Comparable<? super K>, V> Map<K, V> reverseByKeys(Map<K, V> toSort) {
        Map<K, V> results = new LinkedHashMap<>();
        toSort.entrySet().stream().sorted(Entry.comparingByKey()).forEachOrdered(entry -> results.put(entry.getKey(), entry.getValue()));
        return results;
    }

}
