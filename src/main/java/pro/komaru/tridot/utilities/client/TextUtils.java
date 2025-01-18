package pro.komaru.tridot.utilities.client;

import pro.komaru.tridot.utilities.struct.Structs;
import net.minecraft.network.chat.*;

import java.util.*;

public class TextUtils {
    private static String cleanText(String text) {
        return text.replaceAll("[^\\p{L}\\p{N}\\s]+", "").toLowerCase();
    }
    private static Map<CharSequence, Integer> tokenizeAndCount(String text) {
        Map<CharSequence, Integer> freqMap = new HashMap<>();
        for (String word : text.split("\\s+")) {
            freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
        }
        return freqMap;
    }
    public static Component formatf(String template, Object... values) {
        return Component.literal(format(template,values));
    }
    public static String format(String template, Object... values) {
        return format(template, Structs.map(values));
    }
    public static String format(String template, Map<String, Object> values) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String placeholder = "$" + entry.getKey();
            template = template.replace(placeholder, entry.getValue().toString());
        }
        return template;
    }
}
