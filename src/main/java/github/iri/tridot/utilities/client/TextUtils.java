package github.iri.tridot.utilities.client;

import com.skoow.quadlib.utilities.struct.*;
import net.minecraft.network.chat.*;
import org.apache.commons.text.similarity.*;

import java.util.*;

public class TextUtils {
    public static float similarity(String a, String b) {
        CosineSimilarity cosine = new CosineSimilarity();
        Map<CharSequence, Integer> text1 = tokenizeAndCount(cleanText(a));
        Map<CharSequence, Integer> text2 = tokenizeAndCount(cleanText(b));
        return cosine.cosineSimilarity(text1,text2).floatValue();
    }
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
