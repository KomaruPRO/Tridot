package pro.komaru.tridot.utilities.client;

import net.minecraft.network.chat.*;
import net.minecraft.resources.*;

import java.util.function.*;

public class Styles{
    public Style createStyle(){
        return Style.EMPTY
        .withBold(false)
        .withItalic(false)
        .withUnderlined(false)
        .withStrikethrough(false)
        .withObfuscated(false)
        .withFont(new ResourceLocation("minecraft", "default"));
    }

    /**
     * Creates an empty instance of style with colored text
     */
    public UnaryOperator<Style> create(Clr color){
        return style -> createStyle().withColor(TextColor.fromRgb(color.rgb()));
    }
}