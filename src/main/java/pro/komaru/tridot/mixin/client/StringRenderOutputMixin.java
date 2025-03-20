package pro.komaru.tridot.mixin.client;

import com.mojang.blaze3d.font.GlyphInfo;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pro.komaru.tridot.client.gfx.text.DotStyle;

@Mixin(Font.StringRenderOutput.class)
public class StringRenderOutputMixin {
    @Unique DotStyle tridot$style;

    @Inject(method = "accept", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void accept(int index, Style pStyle, int pCodePoint, CallbackInfoReturnable<Boolean> cir, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, boolean flag, float f3, TextColor textcolor, float f, float f1, float f2, float f6, float f7) {
        if(pStyle instanceof DotStyle ds) {
            for (DotStyle.DotStyleEffect effect : ds.effects) {
                effect.afterGlyph(tridot$self(),ds,index,fontset,glyphinfo,bakedglyph,textcolor,f,f1,f2,f3,f6,f7);
            }
        }
    }
    @Inject(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Style;isStrikethrough()Z"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void acceptBeforeEffects(int index, Style pStyle, int pCodePoint, CallbackInfoReturnable<Boolean> cir, FontSet fontset, GlyphInfo glyphinfo, BakedGlyph bakedglyph, boolean flag, float f3, TextColor textcolor, float f, float f1, float f2, float f6, float f7) {
        if(pStyle instanceof DotStyle ds) {
            for (DotStyle.DotStyleEffect effect : ds.effects) {
                effect.beforeGlyphEffects(tridot$self(),ds,index,fontset,glyphinfo,bakedglyph,textcolor,f,f1,f2,f3,f6,f7);
            }
        }
    }

    @ModifyVariable(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Style;getColor()Lnet/minecraft/network/chat/TextColor;"), name = "f3")
    public float changeF3(float value) {
        if(tridot$style != null) {
            for (DotStyle.DotStyleEffect effect : tridot$style.effects)
                value = effect.alpha(value);
        }
        return value;
    }
    @ModifyVariable(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Style;isStrikethrough()Z"), name = "f6")
    public float changeF6(float value) {
        if(tridot$style != null) {
            for (DotStyle.DotStyleEffect effect : tridot$style.effects)
                value = effect.advance(value);
        }
        return value;
    }

    @Inject(method = "accept", at = @At("HEAD"))
    public void acceptBefore(int index, Style pStyle, int pCodePoint, CallbackInfoReturnable<Boolean> cir) {
        tridot$style = null;
        if(pStyle instanceof DotStyle ds) {
            tridot$style = ds;
            for (DotStyle.DotStyleEffect effect : ds.effects) {
                effect.beforeGlyph(tridot$self(),ds,index);
            }
        }
    }

    @Unique
    Font.StringRenderOutput tridot$self() {
        return (Font.StringRenderOutput) ((Object) this);
    }
}
