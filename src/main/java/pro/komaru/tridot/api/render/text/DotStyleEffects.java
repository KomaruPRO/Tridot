package pro.komaru.tridot.api.render.text;

import com.mojang.blaze3d.font.GlyphInfo;
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
import pro.komaru.tridot.util.Tmp;
import pro.komaru.tridot.util.math.ArcRandom;

import java.awt.*;
import java.util.Map;

import static pro.komaru.tridot.Tridot.ofTridot;

public class DotStyleEffects {
    static ArcRandom random = Tmp.rnd;

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

        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index, Map<String,Object> buffer) {
            buffer.put("offX", (float) (xi * random.nextGaussian()));
            buffer.put("offY", (float) (yi * random.nextGaussian()));

            self.x += (float) buffer.get("offX"); self.y += (float) buffer.get("offY");
        }
        @Override
        public void afterGlyph(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7, Map<String,Object> buffer) {
            self.x -= (float) buffer.get("offX"); self.y -= (float) buffer.get("offY");
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

        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index, Map<String, Object> buffer) {
            buffer.put("x", self.x);
            buffer.put("y", self.y + 8);

            float x = (float) buffer.get("x");
            float y = (float) buffer.get("y");

            self.pose.translate(x,y,0f);
            self.pose.scale(sclx,scly,1f);
            self.pose.translate(-x,-y,0f);
        }

        @Override
        public void afterGlyph(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7, Map<String, Object> buffer) {
            float x = (float) buffer.get("x");
            float y = (float) buffer.get("y");

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

        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index, Map<String,Object> buffer) {
            buffer.put("off",(float) Math.sin(Math.toDegrees(
                    (index * charIntensity * 0.02f + ClientTick.getTotal() * 0.005f * intensity)
            )));
            self.y += (float) buffer.get("off");
        }

        @Override
        public void afterGlyph(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7, Map<String,Object> buffer) {
            self.y -= (float) buffer.get("off");
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

        @Override
        public void beforeGlyph(Font.StringRenderOutput self, DotStyle style, int index, Map<String,Object> buffer) {
            if(shiftSymbols) buffer.put("off",(index * 36f + ClientTick.getTotal() * 3.25f * intensity) % 360f);
            else buffer.put("off",((index * 0.05f) + (ClientTick.getTotal() * 1 * intensity)) % 360f);
            buffer.put("color",style.color == null ? Col.white : Col.fromARGB(style.color.getValue()));
            style.color(Col.HSVtoRGB((float) buffer.get("off"), 90, 100));
        }
        @Override
        public void afterGlyph(Font.StringRenderOutput self, DotStyle style, int index, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, TextColor textcolor, float f, float f1, float f2, float f3, float f6, float f7, Map<String,Object> buffer) {
            style.color((Color) buffer.get("color"));
        }

        @Override public ResourceLocation id() {return ofTridot("rainbow");}
        @Override public void write(CompoundTag tag) {
            tag.putFloat("intensity",intensity);
            tag.putBoolean("shift_symbols",shiftSymbols);}
        @Override public void read(CompoundTag tag) {
            intensity = tag.getFloat("intensity");
            shiftSymbols = tag.getBoolean("shift_symbols");}
    }

}
