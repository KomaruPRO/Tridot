package pro.komaru.tridot.mixin.client;

import com.mojang.blaze3d.font.GlyphInfo;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pro.komaru.tridot.client.gfx.text.EffectedStyle;

@Mixin(Font.StringRenderOutput.class)
public class StringRenderOutputMixin {
    @Inject(method = "accept", at = @At("TAIL"),
        locals = LocalCapture.CAPTURE_FAILSOFT)
    public void accept(int index, Style pStyle, int pCodePoint, CallbackInfoReturnable<Boolean> cir, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, boolean flag, float f3, TextColor textcolor, float f, float f1, float f2, float f6, float f7) {
        if(pStyle instanceof EffectedStyle ds) {
            for (EffectedStyle.DotStyleEffect effect : ds.effects) {
                effect.afterGlyph(self(),index,fontset,glyphinfo,bakedglyph,textcolor);
            }
        }
    }
    @Inject(method = "accept", at = @At("HEAD"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    public void acceptBefore(int index, Style pStyle, int pCodePoint, CallbackInfoReturnable<Boolean> cir, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, boolean flag, float f3, TextColor textcolor, float f, float f1, float f2, float f6, float f7) {
        if(pStyle instanceof EffectedStyle ds) {
            for (EffectedStyle.DotStyleEffect effect : ds.effects) {
                effect.beforeGlyph(self(),index);
            }
        }
    }

    Font.StringRenderOutput self() {
        return (Font.StringRenderOutput) ((Object) this);
    }
}
