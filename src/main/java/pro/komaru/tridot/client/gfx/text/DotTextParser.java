package pro.komaru.tridot.client.gfx.text;

import net.minecraft.client.gui.*;
import net.minecraft.network.chat.*;
import pro.komaru.tridot.util.*;

import java.util.*;

public class DotTextParser {

    public static List<Component> parse(String input, int wrapWidth, Font font) {
        List<Component> lines = new ArrayList<>();
        Style currentStyle = Style.EMPTY;
        StringBuilder word = new StringBuilder();
        MutableComponent line = Component.empty();
        int width = 0;
        for (int i = 0; i <= input.length(); i++) {
            char chr = i < input.length() ? input.charAt(i) : '\n';

            if (chr == ' ' || chr == '\n') {
                if (!word.isEmpty()) {
                    Component wordComponent = Component.literal(word.toString()).withStyle(currentStyle);
                    int wordWidth = font.width(wordComponent);

                    if (width + wordWidth > wrapWidth) {
                        lines.add(line);
                        line = Component.empty();
                        width = 0;
                    }

                    line.append(wordComponent).append(" ");
                    width += wordWidth + font.width(" ");
                    word.setLength(0);
                }

                if (chr == '\n') {
                    lines.add(line);
                    line = Component.empty();
                    width = 0;
                }

            } else if (chr == '&' && i + 1 < input.length()) {
                i++;
                char code = input.charAt(i);
                currentStyle = switch (code) {
                    case 'i' -> currentStyle.withItalic(true);
                    case 'b' -> currentStyle.withBold(true);
                    case 's' -> currentStyle.withStrikethrough(true);
                    case 'u' -> currentStyle.withUnderlined(true);
                    case 'k' -> currentStyle.withObfuscated(true);
                    default -> {
                        word.append('&').append(code);
                        yield currentStyle;
                    }
                };

            } else if (chr == '/' && i + 1 < input.length() && input.charAt(i + 1) == '&') {
                currentStyle = Style.EMPTY;
                i++;
            } else if (chr == '#' && i + 7 <= input.length()) {
                String hex = input.substring(i + 1, i + 7);
                currentStyle = DotStyle.of().color(Col.fromHex(hex));
                i += 6;

            } else {
                word.append(chr);
            }
        }

        if (!line.getString().isEmpty()) lines.add(line);
        return lines;
    }
}