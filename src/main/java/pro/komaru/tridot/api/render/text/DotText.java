package pro.komaru.tridot.api.render.text;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.*;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import pro.komaru.tridot.client.ClientTick;
import pro.komaru.tridot.client.gfx.text.DotStyle;
import pro.komaru.tridot.util.Col;
import pro.komaru.tridot.util.Tmp;
import pro.komaru.tridot.util.math.ArcRandom;
import pro.komaru.tridot.util.math.Direction2;
import pro.komaru.tridot.util.math.Mathf;
import pro.komaru.tridot.util.phys.Vec2;
import pro.komaru.tridot.util.struct.Structs;

import java.util.LinkedHashMap;
import java.util.Map;

public class DotText {
    static ArcRandom random = Tmp.rnd;
    public static Map<String, DotStyle.DotStyleEffect> EFFECTS = new LinkedHashMap<>();

    public static void registerEffect(ResourceLocation effectId, DotStyle.DotStyleEffect effectObject) {
        effectObject.id = effectId;
        EFFECTS.put(effectId.toString(),effectObject);
    }

    public static TextBuilder create(Component component) {
        return new TextBuilder((MutableComponent) component);
    }
    public static TextBuilder create(String text) {
        return new TextBuilder(Component.literal(text));
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

    public static ScaleEffect scale(float scl) {
        return new ScaleEffect(scl);
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
    public static OutlineEffect outline(Col col) {
        return outline(col,false);
    }
    public static OutlineEffect outline(Col col,boolean square) {
        return new OutlineEffect(col,square);
    }

    public static AdvanceWaveEffect advanceWave(float speed, float power) {
        return new AdvanceWaveEffect(speed,power);
    }
    public static SpinEffect spin(float speed) {
        return new SpinEffect(speed);
    }

    public static GlintEffect glint(float speed, float scl, Col glintColor) {
        return new GlintEffect(speed,scl,glintColor);
    }
    public static GlintEffect glint(float speed, Col glintColor) {
        return new GlintEffect(speed,1f,glintColor);
    }
    public static GlintEffect glint(Col glintColor) {
        return new GlintEffect(1f,1f,glintColor);
    }

    public static class GlintEffect extends DotStyle.DotStyleEffect {
        public float speed = 1f;
        public float scl = 1f;

        public Col glintColor;

        TextColor col;

        public GlintEffect(float speed, float scl, Col glintColor) {
            this.speed = speed;
            this.scl = scl;
            this.glintColor = glintColor;
        }

        @Override
        public void beforeGlyph(StringRenderOutput self, DotStyle style, int index) {
            col = style.color;

            float t = (ClientTick.getTotal() * speed - index / scl) / 60f;
            t = (Mathf.sin(t * Mathf.PI));
            style.color(Col.fromARGB(col.getValue()).lerp(glintColor, t));
        }

        @Override
        public void afterGlyph(StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7) {
            style.color(col);
        }
    }

    public static class SpinEffect extends DotStyle.DotStyleEffect {
        public float speed = 1f;

        public SpinEffect(float speed) {
            this.speed = speed;
        }

        float x,y;
        @Override
        public void beforeGlyph(StringRenderOutput self, DotStyle style, int index) {
            super.beforeGlyph(self, style, index);
            x = self.x + 2.5f;
            y = self.y + 4.5f;

            self.pose.translate(x,y,0f);
            self.pose.rotateZ(ClientTick.getTotal()/20f * speed);
            self.pose.translate(-x,-y,0f);
        }

        @Override
        public void afterGlyph(StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7) {
            super.afterGlyph(self, style, index, fontset, glyphinfo, bakedglyph, textcolor, f, f1, f2, f3, f6, f7);
            self.pose.translate(x,y,0f);
            self.pose.rotateZ(-ClientTick.getTotal()/20f * speed);
            self.pose.translate(-x,-y,0f);
        }
    }

    public static class AdvanceWaveEffect extends DotStyle.DotStyleEffect {
        public float speed = 1f;
        public float power = 1f;
        public AdvanceWaveEffect(float speed, float power) {
            this.speed = speed;
            this.power = power;
        }

        @Override
        public float advance(float advance) {
            return advance + (float) ((Math.sin(ClientTick.getTotal() / 20f * speed) + 1f) / 2f * power);
        }
    }

    public static class ScaleEffect extends DotStyle.DotStyleEffect {

        public float scl = 1f;

        public ScaleEffect(float scale) {
            this.scl = scale;
        }

        float x,y;

        @Override
        public float advance(float advance) {
            return advance * scl;
        }

        @Override
        public void beforeGlyph(StringRenderOutput self, DotStyle style, int index) {
            super.beforeGlyph(self, style, index);
            x = self.x;
            y = self.y + 8;
            self.pose.translate(x,y,0f);
            self.pose.scale(scl,scl,1f);
            self.pose.translate(-x,-y,0f);
        }

        @Override
        public void afterGlyph(StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7) {
            super.afterGlyph(self, style, index, fontset, glyphinfo, bakedglyph, textcolor, f, f1, f2, f3, f6, f7);
            self.pose.translate(x,y,0f);
            self.pose.scale(1f/scl,1f/scl,1f);
            self.pose.translate(-x,-y,0f);
        }
    }

    public static class OutlineEffect extends DotStyle.DotStyleEffect {

        public Col textCol;
        public boolean square;

        public OutlineEffect(Col textCol, boolean square) {
            this.textCol = textCol;
            this.square = square;
        }

        @Override
        public void beforeGlyphEffects(StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7) {
            super.beforeGlyphEffects(self, style, index, fontset, glyphinfo, bakedglyph, textcolor, f, f1, f2, f3, f6, f7);
            Font font = Minecraft.getInstance().font;
            VertexConsumer vertexconsumer = self.bufferSource.getBuffer(bakedglyph.renderType(self.mode));
            var col = Structs.or(textCol,Col.black);

            for (Vec2 vec2 : square ? Direction2.d8 : Direction2.d4) {
                font.renderChar(bakedglyph, style.isBold(), style.isItalic(), 1f,
                        self.x + f7 + vec2.x, self.y + f7 + vec2.y, self.pose,
                        vertexconsumer, f, f1, f2, f3, self.packedLightCoords);
            }
            font.renderChar(bakedglyph, style.isBold(), style.isItalic(),1f,
                    self.x + f7, self.y + f7, self.pose,
                    vertexconsumer, col.r, col.g, col.b, col.a * f3, self.packedLightCoords);
        }
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
            pulse = (float)(1 - Math.abs(0.3 * Math.sin(ClientTick.getTotal()/20f * intensity)));
        }
    }

    public static class PulseColorEffect extends DotStyle.DotStyleEffect{
        public float intensity;
        public PulseColorEffect(float intensity){
            this.intensity = intensity;
        }

        @Override
        public float alpha(float alpha) {
            hue = (float)(Math.sin(ClientTick.getTotal() * 0.05f * intensity) * 0.5 + 0.5);
            return alpha * (hue);
        }

        float hue;
        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {
            super.beforeGlyph(self, style, index);
            hue = (float)(Math.sin(ClientTick.ticksInGame * 0.05f * intensity) * 0.5 + 0.5);
            if (hue > 1 || hue < 0) {
                hue += (random.nextFloat() - 0.5f) * 0.1f;
                hue = Math.max(0f, Math.min(1f, hue));
            }

            style.color(Col.HSVtoRGB(hue*360f, 100.0f, 100.0f));
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
        Col oldCol;
        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {
            super.beforeGlyph(self, style, index);

            if(shiftSymbols) off = (index * 36f + ClientTick.getTotal() * 3.25f * intensity) % 360f;
            else off = ((index * 0.05f) + (ClientTick.getTotal() * 1 * intensity)) % 360f;
            oldCol = style.color == null ? new Col(1f,1f,1f,1f) : new Col(style.color.getValue());
            style.color(Col.HSVtoRGB(off, 90, 100));
        }

        @Override
        public void afterGlyph(StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7) {
            super.afterGlyph(self, style, index, fontset, glyphinfo, bakedglyph, textcolor, f, f1, f2, f3, f6, f7);
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
                    (index * charIntensity * 0.02f + ClientTick.getTotal() * 0.005f * intensity)
            ));

            self.y += off;
        }

        @Override
        public void afterGlyph(StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7) {
            super.afterGlyph(self, style, index, fontset, glyphinfo, bakedglyph, textcolor, f, f1, f2, f3, f6, f7);

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
        public void afterGlyph(StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7) {
            super.afterGlyph(self, style, index, fontset, glyphinfo, bakedglyph, textcolor, f, f1, f2, f3, f6, f7);
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
