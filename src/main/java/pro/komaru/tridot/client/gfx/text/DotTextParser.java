package pro.komaru.tridot.client.gfx.text;

import net.minecraft.client.gui.*;
import net.minecraft.network.chat.*;
import pro.komaru.tridot.util.*;

import java.util.*;

public class DotTextParser {

    public static List<Component> parse(String input, int wrapWidth, Font font) {
        List<Component> lines = new ArrayList<>();
        List<TextSegment> segments = parseTags(input);
        MutableComponent currentLine = Component.empty();
        int currentLineWidth = 0;

        for (TextSegment segment : segments) {
            String text = segment.text();
            Style style = segment.style();
            StringBuilder currentWord = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                currentWord.append(c);

                boolean isNewline = (c == '\n');
                boolean isSpace = (c == ' ');
                boolean isCJK = Character.isIdeographic(c);

                if (isNewline || isSpace || isCJK || i == text.length() - 1) {
                    if (isNewline) {
                        if (!currentWord.isEmpty()) {
                            currentLine.append(Component.literal(currentWord.substring(0, currentWord.length() - 1)).withStyle(style));
                        }

                        lines.add(currentLine);
                        currentLine = Component.empty();
                        currentLineWidth = 0;
                        currentWord.setLength(0);
                        continue;
                    }

                    MutableComponent wordComp = Component.literal(currentWord.toString()).withStyle(style);
                    int wordWidth = font.width(wordComp);
                    if (currentLineWidth + wordWidth > wrapWidth) {
                        if (currentLineWidth > 0) {
                            lines.add(currentLine);
                            currentLine = Component.empty();
                            currentLineWidth = 0;
                        }
                    }

                    currentLine.append(wordComp);
                    currentLineWidth += wordWidth;
                    currentWord.setLength(0);
                }
            }
        }

        if (currentLineWidth > 0 || !currentLine.getSiblings().isEmpty()) {
            lines.add(currentLine);
        }

        if (lines.isEmpty()) {
            lines.add(Component.empty());
        }

        return lines;
    }

    private record TextSegment(String text, Style style) {}

    private static List<TextSegment> parseTags(String input) {
        List<TextSegment> segments = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        Style currentStyle = Style.EMPTY;
        for (int i = 0; i < input.length(); i++) {
            char chr = input.charAt(i);
            if (chr == '&' && i + 1 < input.length()) {
                if (!buffer.isEmpty()) {
                    segments.add(new TextSegment(buffer.toString(), currentStyle));
                    buffer.setLength(0);
                }

                char code = input.charAt(i + 1);
                i++;

                currentStyle = switch (code) {
                    case 'i' -> currentStyle.withItalic(true);
                    case 'b' -> currentStyle.withBold(true);
                    case 's' -> currentStyle.withStrikethrough(true);
                    case 'u' -> currentStyle.withUnderlined(true);
                    case 'k' -> currentStyle.withObfuscated(true);
                    case 'r' -> Style.EMPTY;
                    default -> {
                        buffer.append('&').append(code);
                        yield currentStyle;
                    }
                };
            } else if (chr == '/' && i + 1 < input.length() && input.charAt(i + 1) == '&') {
                if (!buffer.isEmpty()) {
                    segments.add(new TextSegment(buffer.toString(), currentStyle));
                    buffer.setLength(0);
                }
                currentStyle = Style.EMPTY;
                i++;
            }

            else if (chr == '#' && i + 6 < input.length()) {
                String hex = input.substring(i + 1, i + 7);
                if (hex.matches("[0-9a-fA-F]{6}")) {
                    if (!buffer.isEmpty()) {
                        segments.add(new TextSegment(buffer.toString(), currentStyle));
                        buffer.setLength(0);
                    }
                    try {
                        int colorValue = Integer.parseInt(hex, 16);
                        currentStyle = currentStyle.withColor(TextColor.fromRgb(colorValue));
                        i += 6;
                    } catch (NumberFormatException ignored) {
                        buffer.append('#');
                    }
                } else {
                    buffer.append('#');
                }
            }
            else {
                buffer.append(chr);
            }
        }

        if (!buffer.isEmpty()) {
            segments.add(new TextSegment(buffer.toString(), currentStyle));
        }

        return segments;
    }}