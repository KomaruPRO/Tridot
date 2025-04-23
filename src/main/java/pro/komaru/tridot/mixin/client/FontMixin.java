package pro.komaru.tridot.mixin.client;

import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pro.komaru.tridot.client.gfx.text.DotStyle;

import java.util.function.Function;

@Mixin(Font.class)
public class FontMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Function pFonts, boolean pFilterFishyGlyphs, CallbackInfo ci) {
        tridot$self().splitter = new StringSplitter((ch,style) -> {
            float adv = tridot$self().getFontSet(style.getFont())
                    .getGlyphInfo(ch, tridot$self().filterFishyGlyphs).getAdvance(style.isBold());
            if(style instanceof DotStyle ds)
                for (DotStyle.StyleEffect effect : ds.effects) adv = effect.advance(adv);
            return adv;
        });
    }

    @Unique
    Font tridot$self() {
        return (Font) ((Object) this);
    }
}
