package pro.komaru.tridot.api.render.text;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class DotText {
    public static TextBuilder create(Component component) {
        return new TextBuilder((MutableComponent) component);
    }
    public static TextBuilder create(String text) {
        return new TextBuilder(Component.literal(text));
    }
}
