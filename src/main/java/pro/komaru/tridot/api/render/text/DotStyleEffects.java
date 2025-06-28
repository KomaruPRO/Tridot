package pro.komaru.tridot.api.render.text;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import pro.komaru.tridot.client.ClientTick;
import pro.komaru.tridot.client.gfx.text.DotStyle;
import pro.komaru.tridot.client.gfx.text.DotStyle.StyleEffect;
import pro.komaru.tridot.util.Col;
import pro.komaru.tridot.util.Log;
import pro.komaru.tridot.util.Tmp;
import pro.komaru.tridot.util.math.ArcRandom;
import pro.komaru.tridot.util.math.Direction2;
import pro.komaru.tridot.util.math.Mathf;
import pro.komaru.tridot.util.phys.Vec2;
import pro.komaru.tridot.util.struct.Structs;
import pro.komaru.tridot.util.struct.func.Prov;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static pro.komaru.tridot.Tridot.ofTridot;

public class DotStyleEffects {
    static ArcRandom random = Tmp.rnd;

    public static Map<String, DotStyle.EffectEntry> EFFECTS = new LinkedHashMap<>();

    public static StyleEffect read(CompoundTag tag) {
        String id = tag.getString("id");
        if (EFFECTS.containsKey(id)) {
            DotStyle.EffectEntry entry = EFFECTS.get(id);
            StyleEffect effect = entry.get();
            effect.read(tag);
            return effect;
        } else {
            Log.error("Unknown style effect: " + id);
            return null;
        }
    }
    public static void write(StyleEffect effect, CompoundTag tag) {
        tag.putString("id", effect.id().toString());
        effect.write(tag);
    }

    public static StyleEffect getEmpty(ResourceLocation effect) {
        return EFFECTS.get(effect.toString()).get();
    }

    static {
        add(
                AdvanceFX::new,
                ShakeFX::new,
                ScaleFX::new,
                WaveFX::new,
                RainbowFX::new,
                GlintFX::new,
                SpinFX::new,
                OutlineFX::new,
                PulseAlphaFX::new,
                PulseColorFX::new,
                AdvanceWaveFX::new
        );
    }

    public static void add(Prov<StyleEffect> ...effectObjects) {
        for (Prov<StyleEffect> effectObject : effectObjects) {
            add(effectObject);
        }
    }
    public static void add(Prov<StyleEffect> effectObject) {
        add(effectObject.get().id(), effectObject);
    }
    /** Not recommended to use */
    @Deprecated
    public static void add(ResourceLocation effect, Prov<StyleEffect> effectObject) {
        EFFECTS.put(effect.toString(),new DotStyle.EffectEntry(effectObject, effect));
    }

    public static class AdvanceFX extends StyleEffect {
        public static AdvanceFX of(float advance) {
            AdvanceFX fx = new AdvanceFX();
            fx.advance = advance;
            return fx;
        }

        public float advance;

        @Override public float advance(float advance) {return advance + this.advance;}

        @Override public ResourceLocation id() {return ofTridot("advance");}
        @Override public void write(CompoundTag tag) {
            tag.putFloat("advance", advance);}
        @Override public void read(CompoundTag tag) {
            advance = tag.getFloat("advance");}
    }
    public static class ShakeFX extends StyleEffect {
        public static ShakeFX of(float xi, float yi) {
            ShakeFX fx = new ShakeFX();
            fx.xi = xi;
            fx.yi = yi;
            return fx;
        }
        public static ShakeFX of(float xi) {
            return of(xi,xi);
        }

        public float xi,yi;



        float offX, offY;
        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {
            offX = (float) (xi * random.nextGaussian());
            offY = (float) (yi * random.nextGaussian());

            self.x += offX; self.y += offY;
        }
        @Override
        public void afterGlyph(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7) {
            self.x -= offX; self.y -= offY;
        }

        @Override public ResourceLocation id() {return ofTridot("shake");}
        @Override public void write(CompoundTag tag) {
            tag.putFloat("xi",xi); tag.putFloat("yi",yi);}
        @Override public void read(CompoundTag tag) {
            xi = tag.getFloat("xi"); yi = tag.getFloat("yi");}
    }
    public static class ScaleFX extends StyleEffect {
        public static ScaleFX of(float sclx, float scly) {
            ScaleFX fx = new ScaleFX();
            fx.sclx = sclx;
            fx.scly = scly;
            return fx;
        }
        public static ScaleFX of(float scl) {
            return of(scl,scl);
        }

        public float sclx = 1f, scly = 1f;

        float x,y;
        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {
            x = self.x;
            y = self.y + 8;

            self.pose.translate(x,y,0f);
            self.pose.scale(sclx,scly,1f);
            self.pose.translate(-x,-y,0f);
        }

        @Override
        public void afterGlyph(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7) {
            self.pose.translate(x,y,0f);
            self.pose.scale(1f/sclx,1f/scly,1f);
            self.pose.translate(-x,-y,0f);
        }

        @Override public ResourceLocation id() {return ofTridot("scale");}

        @Override public void write(CompoundTag tag) {
            tag.putFloat("sclx",sclx); tag.putFloat("scly",scly);}

        @Override public void read(CompoundTag tag) {
            sclx = tag.getFloat("sclx"); scly = tag.getFloat("scly");}
    }
    public static class WaveFX extends StyleEffect {
        public static WaveFX of(float intensity, float charIntensity) {
            WaveFX fx = new WaveFX();
            fx.intensity = intensity;
            fx.charIntensity = charIntensity;
            return fx;
        }
        public static WaveFX of(float intensity) {
            return of(intensity,1f);
        }

        public float intensity;
        public float charIntensity;

        float off;

        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {
            off = (float) Math.sin(Math.toDegrees(
                    (index * charIntensity * 0.02f + ClientTick.getTotal() * 0.005f * intensity)
            ));
            self.y += off;
        }

        @Override
        public void afterGlyph(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7) {
            self.y -= off;
        }

        @Override public ResourceLocation id() {return ofTridot("wave");}
        @Override public void write(CompoundTag tag) {
            tag.putFloat("intensity",intensity);
            tag.putFloat("char_intensity",charIntensity);}
        @Override public void read(CompoundTag tag) {
            intensity = tag.getFloat("intensity");
            charIntensity = tag.getFloat("char_intensity");}
    }
    public static class RainbowFX extends StyleEffect {
        public static RainbowFX of(float intensity, boolean shiftSymbols) {
            RainbowFX fx = new RainbowFX();
            fx.intensity = intensity;
            fx.shiftSymbols = shiftSymbols;
            return fx;
        }
        public static RainbowFX of(float intensity) {
            return of(intensity,false);
        }

        public float intensity;
        public boolean shiftSymbols;

        float off;
        Col col;

        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {
            if(shiftSymbols) off = (index * 36f + ClientTick.getTotal() * 3.25f * intensity) % 360f;
            else off = ((index * 0.05f) + (ClientTick.getTotal() * 1 * intensity)) % 360f;
            col = style.color == null ? Col.white : Col.fromARGB(style.color.getValue());
            style.color(Col.HSVtoRGB(off, 90, 100));
        }
        @Override
        public void afterGlyph(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7) {

        }

        @Override public ResourceLocation id() {return ofTridot("rainbow");}
        @Override public void write(CompoundTag tag) {
            tag.putFloat("intensity",intensity);
            tag.putBoolean("shift_symbols",shiftSymbols);}
        @Override public void read(CompoundTag tag) {
            intensity = tag.getFloat("intensity");
            shiftSymbols = tag.getBoolean("shift_symbols");}
    }

    public static class GlintFX extends StyleEffect{
        public float speed = 1f;
        public float scl = 1f;
        public Col glintColor;
        Col col;

        public static GlintFX of(float speed, float scl, Col glintColor){
            GlintFX fx = new GlintFX();
            fx.speed = speed;
            fx.scl = scl;
            fx.glintColor = glintColor;
            return fx;
        }

        public static GlintFX of(float speed, float scl){
            return of(speed, scl, Col.white);
        }

        public static GlintFX of(float speed){
            return of(speed, 1f, Col.white);
        }

        public static GlintFX of(){
            return of(1f, 1f, Col.white);
        }

        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index){
            col = style.color();

            float t = (ClientTick.getTotal() * speed - index / scl) / 60f;
            t = (Mathf.sin(t * Mathf.PI));
            style.color(col.copy().lerp(glintColor, t));
        }

        @Override
        public void afterGlyph(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7){
            style.color(col);
        }

        @Override
        public ResourceLocation id(){
            return ofTridot("glint");
        }

        @Override
        public void write(CompoundTag tag){
            tag.putFloat("speed", speed);
            tag.putFloat("scl", scl);
        }

        @Override
        public void read(CompoundTag tag){
            speed = tag.getFloat("speed");
            scl = tag.getFloat("scl");
        }
    }

    public static class SpinFX extends StyleEffect {
        public static SpinFX of(float speed) {
            SpinFX fx = new SpinFX();
            fx.speed = speed;
            return fx;
        }

        public float speed = 1f;


        float x,y;
        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {
            x = self.x + 2.5f;
            y = self.y + 4.5f;


            self.pose.translate(x,y,0f);
            self.pose.rotateZ(ClientTick.getTotal()/20f * speed);
            self.pose.translate(-x,-y,0f);
        }

        @Override
        public void afterGlyph(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7) {
            self.pose.translate(x,y,0f);
            self.pose.rotateZ(-ClientTick.getTotal()/20f * speed);
            self.pose.translate(-x,-y,0f);
        }

        @Override public ResourceLocation id() {return ofTridot("spin");}
        @Override public void write(CompoundTag tag) {
            tag.putFloat("speed",speed);}
        @Override public void read(CompoundTag tag) {
            speed = tag.getFloat("speed");}
    }
    public static class OutlineFX extends StyleEffect {
        public static OutlineFX of(Col textCol, boolean square) {
            OutlineFX fx = new OutlineFX();
            fx.textCol = textCol;
            fx.square = square;
            return fx;
        }
        public static OutlineFX of(Col textCol) {
            return of(textCol,false);
        }

        public Col textCol;
        public boolean square;

        @Override
        public void beforeGlyphEffects(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7) {
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

        @Override public ResourceLocation id() {return ofTridot("outline");}
        @Override public void write(CompoundTag tag) {
            tag.putInt("text_color",textCol.toARGB());
            tag.putBoolean("square",square);}
        @Override public void read(CompoundTag tag) {
            textCol = Col.fromARGB(tag.getInt("text_color"));
            square = tag.getBoolean("square");}
    }
    public static class PulseAlphaFX extends StyleEffect {
        public static PulseAlphaFX of(float intensity) {
            PulseAlphaFX fx = new PulseAlphaFX();
            fx.intensity = intensity;
            return fx;
        }
        public static PulseAlphaFX of() {
            return of(1f);
        }

        public float intensity = 1f;

        float pulse;
        @Override public float alpha(float alpha) {
            return alpha * Mathf.clamp(pulse);
        }
        @Override public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {
            pulse = (float)(1 - Math.abs(0.3 * Math.sin(ClientTick.getTotal()/20f * intensity)));
        }

        @Override public ResourceLocation id() {return ofTridot("pulse_alpha");}
        @Override public void write(CompoundTag tag) {
            tag.putFloat("intensity", intensity);}
        @Override public void read(CompoundTag tag) {
            intensity = tag.getFloat("intensity");}
    }
    public static class PulseColorFX extends StyleEffect {
        public static PulseColorFX of(float intensity) {
            PulseColorFX fx = new PulseColorFX();
            fx.intensity = intensity;
            return fx;
        }
        public static PulseColorFX of() {
            return of(1f);
        }

        public float intensity;

        @Override public float alpha(float alpha) {
            hue = (float)(Math.sin(ClientTick.getTotal() * 0.05f * intensity) * 0.5 + 0.5);
            return alpha * (hue);
        }

        float hue;
        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index) {
            hue = (float)(Math.sin(ClientTick.ticksInGame * 0.05f * intensity) * 0.5 + 0.5);
            if (hue > 1 || hue < 0) {
                hue += (random.nextFloat() - 0.5f) * 0.1f;
                hue = Math.max(0f, Math.min(1f, hue));
            }

            style.color(Col.HSVtoRGB(hue*360f, 100.0f, 100.0f));
        }
        @Override public ResourceLocation id() {return ofTridot("pulse_color");}
        @Override public void write(CompoundTag tag) {
            tag.putFloat("intensity",intensity);}
        @Override public void read(CompoundTag tag) {
            this.intensity = tag.getFloat("intensity");}
    }
    public static class AdvanceWaveFX extends StyleEffect {

        public static AdvanceWaveFX of(float speed, float power) {
            AdvanceWaveFX fx = new AdvanceWaveFX();
            fx.speed = speed;
            fx.power = power;
            return fx;
        }
        public static AdvanceWaveFX of(float speed) {
            return of(speed,1f);
        }
        public static AdvanceWaveFX of() {
            return of(1f,1f);
        }

        public float speed = 1f;
        public float power = 1f;

        @Override public float advance(float advance) {
            return advance + (float) ((Math.sin(ClientTick.getTotal() / 20f * speed) + 1f) / 2f * power);
        }

        @Override public ResourceLocation id() {return ofTridot("advance_wave");}
        @Override public void write(CompoundTag tag) {
            tag.putFloat("speed",speed);
            tag.putFloat("power",power);}
        @Override public void read(CompoundTag tag) {
            speed = tag.getFloat("speed");
            power = tag.getFloat("power");}
    }
}
