package pro.komaru.tridot.client.render.text;

import com.mojang.blaze3d.font.GlyphInfo;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import pro.komaru.tridot.api.render.DotText;
import pro.komaru.tridot.util.Col;
import pro.komaru.tridot.ocore.struct.data.Seq;

public class EffectedStyle extends Style {

    public Seq<DotStyleEffect> effects = Seq.with();

    public EffectedStyle(@Nullable TextColor pColor, @Nullable Boolean pBold, @Nullable Boolean pItalic, @Nullable Boolean pUnderlined, @Nullable Boolean pStrikethrough, @Nullable Boolean pObfuscated, @Nullable ClickEvent pClickEvent, @Nullable HoverEvent pHoverEvent, @Nullable String pInsertion, @Nullable ResourceLocation pFont) {
        super(pColor, pBold, pItalic, pUnderlined, pStrikethrough, pObfuscated, pClickEvent, pHoverEvent, pInsertion, pFont);
    }
    public EffectedStyle() {
        super(null,null,null,null,null,null,null,null,null,null);
    }

    public static EffectedStyle of() {
        return new EffectedStyle();
    }

    public EffectedStyle color(Col color) {
        return color(color.toTextColor());
    }
    public EffectedStyle color(TextColor color) {
        this.color = color;
        return this;
    }
    public EffectedStyle bold(boolean value) {
        this.bold = value;
        return this;
    }
    public EffectedStyle italic(boolean value) {
        this.italic = value;
        return this;
    }
    public EffectedStyle underlined(boolean value) {
        this.underlined = value;
        return this;
    }
    public EffectedStyle strikethrough(boolean value) {
        this.strikethrough = value;
        return this;
    }
    public EffectedStyle obfuscated(boolean value) {
        this.obfuscated = value;
        return this;
    }
    public EffectedStyle insertion(String value) {
        this.insertion = value;
        return this;
    }
    public EffectedStyle click(ClickEvent event) {
        this.clickEvent = event;
        return this;
    }
    public EffectedStyle hover(HoverEvent event) {
        this.hoverEvent = event;
        return this;
    }
    public EffectedStyle font(ResourceLocation font) {
        this.font = font;
        return this;
    }

    public EffectedStyle effects(DotStyleEffect ...effects) {
        this.effects.clear();
        this.effects.addAll(effects);
        return this;
    }
    public EffectedStyle effects(ResourceLocation ...effects) {
        Seq<DotStyleEffect> fx = Seq.with();
        for (ResourceLocation effect : effects)
            fx.add(DotText.EFFECTS.get(effect.toString()));
        return effects(fx.toArray());
    }

    public EffectedStyle effect(DotStyleEffect effect) {
        effects.add(effect);
        return this;
    }
    public EffectedStyle effect(ResourceLocation location) {
        return effect(DotText.EFFECTS.get(location.toString()));
    }
    public EffectedStyle effect(String modId, String id) {
        return effect(new ResourceLocation(modId,id));
    }


    public static class DotStyleEffect {
        public ResourceLocation id;

        public float advance() {
            return 0;
        }

        public void beforeGlyph(Font.StringRenderOutput self, int index) {

        }
        public void afterGlyph(Font.StringRenderOutput self, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor) {
            self.x += advance();
        }
    }
}
