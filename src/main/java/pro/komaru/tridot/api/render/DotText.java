package pro.komaru.tridot.api.render;

import com.mojang.blaze3d.font.GlyphInfo;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import pro.komaru.tridot.client.gfx.text.DotStyle;
import pro.komaru.tridot.util.Tmp;
import pro.komaru.tridot.util.math.ArcRandom;

import java.util.LinkedHashMap;
import java.util.Map;

public class DotText {

    static ArcRandom random = Tmp.rnd;
    public static Map<String, DotStyle.DotStyleEffect> EFFECTS = new LinkedHashMap<>();

    public static void registerEffect(ResourceLocation effectId, DotStyle.DotStyleEffect effectObject) {
        effectObject.id = effectId;
        EFFECTS.put(effectId.toString(),effectObject);
    }

    public static ShakeEffect shake(float intensity) {
        return new ShakeEffect(intensity);
    }
    public static ShakeEffect shake(float xIntensity, float yIntensity) {
        return new ShakeEffect(xIntensity,yIntensity);
    }

    public static AdvanceEffect advance(float adv) {
        return new AdvanceEffect(adv);
    }

    public static class ShakeEffect extends DotStyle.DotStyleEffect {
        public float xi,yi;

        public ShakeEffect(float intensity) {
            this(intensity,0);
        }
        public ShakeEffect(float intensity, float yIntensity) {
            xi = intensity;
            yi = yIntensity;
        }

        float offX, offY;

        @Override
        public void beforeGlyph(Font.StringRenderOutput self, int index) {
            super.beforeGlyph(self, index);

            offX = (float) (xi * random.nextGaussian());
            offY = (float) (yi * random.nextGaussian());

            self.x += offX; self.y += offY;
        }

        @Override
        public void afterGlyph(Font.StringRenderOutput self, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor) {
            super.afterGlyph(self, index, fontset, glyphinfo, bakedglyph, textcolor);
            self.x -= offX; self.y -= offY;
        }
    }

    public static class AdvanceEffect extends DotStyle.DotStyleEffect {
        public float advance;

        public AdvanceEffect(float advance) {
            this.advance = advance;
        }

        @Override
        public float advance() {
            return advance;
        }
    }
}
