package pro.komaru.tridot.client.gfx.text;

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
import pro.komaru.tridot.client.event.*;
import pro.komaru.tridot.client.graphics.Clr;
import pro.komaru.tridot.core.struct.data.Seq;

import java.awt.*;
import java.util.*;

public class DotStyle extends Style {

    public Seq<DotStyleEffect> effects = Seq.with();

    public DotStyle(@Nullable TextColor pColor, @Nullable Boolean pBold, @Nullable Boolean pItalic, @Nullable Boolean pUnderlined, @Nullable Boolean pStrikethrough, @Nullable Boolean pObfuscated, @Nullable ClickEvent pClickEvent, @Nullable HoverEvent pHoverEvent, @Nullable String pInsertion, @Nullable ResourceLocation pFont) {
        super(pColor, pBold, pItalic, pUnderlined, pStrikethrough, pObfuscated, pClickEvent, pHoverEvent, pInsertion, pFont);
    }
    public DotStyle() {
        super(null,null,null,null,null,null,null,null,null,null);
    }

    public static DotStyle of() {
        return new DotStyle();
    }

    @Override
    public boolean equals(Object pOther) {
        if (this == pOther) {
            return true;
        } else if (!(pOther instanceof DotStyle)) {
            return false;
        }
        DotStyle dot = (DotStyle) pOther;
        if(!dot.effects.equals(this.effects)) return false;
        return super.equals(pOther);
    }

    public DotStyle color(Color color) {
        return this.color(new Clr(color.getRGB()));
    }
    public DotStyle color(Clr color) {
        return color(color.toTextColor());
    }
    public DotStyle color(TextColor color) {
        this.color = color;
        return this;
    }
    public DotStyle bold(boolean value) {
        this.bold = value;
        return this;
    }
    public DotStyle italic(boolean value) {
        this.italic = value;
        return this;
    }
    public DotStyle underlined(boolean value) {
        this.underlined = value;
        return this;
    }
    public DotStyle strikethrough(boolean value) {
        this.strikethrough = value;
        return this;
    }
    public DotStyle obfuscated(boolean value) {
        this.obfuscated = value;
        return this;
    }
    public DotStyle insertion(String value) {
        this.insertion = value;
        return this;
    }
    public DotStyle click(ClickEvent event) {
        this.clickEvent = event;
        return this;
    }
    public DotStyle hover(HoverEvent event) {
        this.hoverEvent = event;
        return this;
    }
    public DotStyle font(ResourceLocation font) {
        this.font = font;
        return this;
    }

    public DotStyle effects(DotStyleEffect ...effects) {
        this.effects.clear();
        this.effects.addAll(effects);
        return this;
    }
    public DotStyle effects(ResourceLocation ...effects) {
        Seq<DotStyleEffect> fx = Seq.with();
        for (ResourceLocation effect : effects)
            fx.add(DotText.EFFECTS.get(effect.toString()));
        return effects(fx.toArray());
    }

    public DotStyle effect(DotStyleEffect effect) {
        effects.add(effect);
        return this;
    }
    public DotStyle effect(ResourceLocation location) {
        return effect(DotText.EFFECTS.get(location.toString()));
    }
    public DotStyle effect(String modId, String id) {
        return effect(new ResourceLocation(modId,id));
    }


    public static class DotStyleEffect {
        public ResourceLocation id;

        public float advance(float advance) {
            return advance;
        }
        public float alpha(float alpha) {
            return alpha;
        }

        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {

        }
        public void beforeGlyphEffects(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor) {

        }
        public void afterGlyph(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor) {
            
        }
    }
}
