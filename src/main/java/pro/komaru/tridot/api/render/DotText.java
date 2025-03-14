package pro.komaru.tridot.api.render;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.systems.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.TextColor;
import pro.komaru.tridot.client.event.ClientTickHandler;
import pro.komaru.tridot.client.graphics.*;
import pro.komaru.tridot.core.math.ArcRandom;
import pro.komaru.tridot.core.math.Mathf;

import java.awt.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class DotText {

    static ArcRandom random = Tmp.rnd;
    public static Map<String, DotStyle.DotStyleEffect> EFFECTS = new LinkedHashMap<>();

    public static void registerEffect(ResourceLocation effectId, DotStyle.DotStyleEffect effectObject) {
        effectObject.id = effectId;
        EFFECTS.put(effectId.toString(),effectObject);
    }

    public static WaveEffect wave(float intensity) {
        return wave(intensity,1f);
    }
    public static WaveEffect wave(float intensity, float charIntensity) {
        return new WaveEffect(intensity,charIntensity);
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

    public static PulseEffect pulse(float intensity) {
        return new PulseEffect(intensity);
    }

    public static PulseColorEffect pulseColor(float intensity) {
        return new PulseColorEffect(intensity);
    }

    public static RainbowEffect rainbow(float intensity) {
        return new RainbowEffect(intensity, false);
    }

    public static RainbowEffect rainbow(float intensity, boolean shiftSymbols) {
        return new RainbowEffect(intensity, shiftSymbols);
    }

    public static class PulseEffect extends DotStyle.DotStyleEffect{
        public float intensity;
        public PulseEffect(float intensity){
            this.intensity = intensity;
        }

        float pulse;

        @Override
        public float alpha(float alpha) {
            return alpha * Mathf.clamp(pulse);
        }

        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {
            pulse = (float)(1 - Math.abs(0.3 * Math.sin(ClientTickHandler.getTotal()/20f * intensity)));
        }
    }

    //todo might be better
    public static class PulseColorEffect extends DotStyle.DotStyleEffect{
        public float intensity;
        public PulseColorEffect(float intensity){
            this.intensity = intensity;
        }

        @Override
        public float alpha(float alpha) {
            hue = (float)(Math.sin(ClientTickHandler.getTotal() * 0.05f * intensity) * 0.5 + 0.5);
            return alpha * (hue);
        }

        float hue;
        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {
            super.beforeGlyph(self, style, index);
            hue = (float)(Math.sin(ClientTickHandler.ticksInGame * 0.05f * intensity) * 0.5 + 0.5);
            if (hue > 1 || hue < 0) {
                hue += (rand.nextFloat() - 0.5f) * 0.1f;
                hue = Math.max(0f, Math.min(1f, hue));
            }

            style.color(Clr.HSVtoRGB(hue*360f, 100.0f, 100.0f));
        }
    }

    public static class RainbowEffect extends DotStyle.DotStyleEffect{
        public float intensity;
        public boolean shiftSymbols;
        public RainbowEffect(float intensity, boolean shiftSymbols){
            this.intensity = intensity;
            this.shiftSymbols = shiftSymbols;
        }

        float off;
        Clr oldCol;
        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {
            super.beforeGlyph(self, style, index);

            if(shiftSymbols) off = (index * 36f + ClientTickHandler.getTotal() * 3.25f * intensity) % 360f;
            else off = ((index * 0.05f) + (ClientTickHandler.getTotal() * 1 * intensity)) % 360f;
            oldCol = style.color == null ? new Clr(1f,1f,1f,1f) : new Clr(style.color.getValue());
            style.color(Clr.HSVtoRGB(off, 90, 100));
        }

        @Override
        public void afterGlyph(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor) {
            super.afterGlyph(self, style, index, fontset, glyphinfo, bakedglyph, textcolor);
            style.color(oldCol);
        }
    }

    public static class WaveEffect extends DotStyle.DotStyleEffect {
        public float intensity;
        public float charIntensity;
        public WaveEffect(float intensity, float charAffect) {
            this.intensity = intensity;
            this.charIntensity = charAffect;
        }
        float off;
        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {
            super.beforeGlyph(self, style, index);

            off = (float) Math.sin(Math.toDegrees(
                    (index * charIntensity * 0.02f + ClientTickHandler.getTotal() * 0.005f * intensity)
            ));

            self.y += off;
        }

        @Override
        public void afterGlyph(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor) {
            super.afterGlyph(self, style, index, fontset, glyphinfo, bakedglyph, textcolor);

            self.y -= off;
        }
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
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {
            super.beforeGlyph(self, style, index);

            offX = (float) (xi * random.nextGaussian());
            offY = (float) (yi * random.nextGaussian());

            self.x += offX; self.y += offY;
        }

        @Override
        public void afterGlyph(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor) {
            super.afterGlyph(self, style, index, fontset, glyphinfo, bakedglyph, textcolor);
            self.x -= offX; self.y -= offY;
        }
    }

    public static class AdvanceEffect extends DotStyle.DotStyleEffect {
        public float advance;

        public AdvanceEffect(float advance) {
            this.advance = advance;
        }

        @Override
        public float advance(float adv) {
            return adv + advance;
        }
    }
}
