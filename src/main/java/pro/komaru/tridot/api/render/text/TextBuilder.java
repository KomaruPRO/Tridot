package pro.komaru.tridot.api.render.text;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import pro.komaru.tridot.api.Utils;
import pro.komaru.tridot.api.render.GuiDraw;
import pro.komaru.tridot.client.gfx.text.DotStyle;
import pro.komaru.tridot.util.Col;
import pro.komaru.tridot.util.Tmp;

import java.awt.*;
import java.util.List;
import java.util.function.UnaryOperator;

public class TextBuilder {

    public MutableComponent text;
    public TextRenderProps renderProps;

    public TextBuilder(MutableComponent text) {
        this.text = text;
        renderProps = new TextRenderProps();
    }

    public TextBuilder renderStyle(UnaryOperator<TextRenderProps> renderStyle) {
        renderProps = renderStyle.apply(renderProps);
        return this;
    }

    public TextBuilder add(String text) {
        this.text.append(text);
        return this;
    }
    public TextBuilder add(Component component) {
        text.append(component);
        return this;
    }
    public TextBuilder add(TextBuilder builder) {
        text.append(builder.get());
        return this;
    }

    public TextBuilder color(TextColor col) {
        return style(style -> style.color(col));
    }
    public TextBuilder color(int col) {
        return style(style -> style.color(TextColor.fromRgb(col)));
    }
    public TextBuilder color(String hex) {
        return style(style -> style.color(Col.fromHex(hex)));
    }
    public TextBuilder color(Color col) {
        return style(style -> style.color(col));
    }
    public TextBuilder color(Col col) {
        return style(style -> style.color(col));
    }

    public TextBuilder style(UnaryOperator<DotStyle> style) {
        if(!(text.getStyle() instanceof DotStyle)) text.setStyle(DotStyle.of().from(text.getStyle()));
        text.setStyle(style.apply((DotStyle) text.getStyle()));
        return this;
    }

    public TextBuilder text(Component comp) {
        text = (MutableComponent) comp;
        return this;
    }
    public TextBuilder text(String comp) {
        text = Component.literal(comp);
        return this;
    }

    public TextBuilder translatable() {
        text = Component.translatable(text.getString());
        return this;
    }

    public TextBuilder effects() {
        return style(DotStyle::effects);
    }
    public TextBuilder effects(DotStyle.DotStyleEffect... effs) {
        return style(s -> s.effects(effs));
    }
    public TextBuilder effects(ResourceLocation... effs) {
        return style(s -> s.effects(effs));
    }

    public TextBuilder render(GuiGraphics g, float x, float y) {
        GuiDraw d = new GuiDraw(g);

        float w = width(true);
        float h = height(true);

        List<FormattedCharSequence> split = split(true);

        if(renderProps.centered) x -= w/2f;
        if(renderProps.yCentered) y -= h/2f;

        d.push();
        d.move(x,y);
        if(renderProps.centered) d.scale(renderProps.scaleX, renderProps.scaleY, 0f, 0f);
        else d.scale(renderProps.scaleX, renderProps.scaleY);

        int i = 0;
        for (FormattedCharSequence charseq : split) {
            float locw = Utils.mc().font.width(charseq);
            g.drawString(Utils.mc().font,charseq,w/2f/renderProps.scaleX-locw/2f,i * 9, Tmp.c1.set(1f,1f,1f,renderProps.alpha).argb8888(), renderProps.shadow);
            i++;
        }

        d.pop();

        return this;
    }

    @OnlyIn(Dist.CLIENT)
    public float width(boolean useRender) {
        var spl = split(useRender);
        int max = 0;
        for (FormattedCharSequence ch : spl) {
            int v = Minecraft.getInstance().font.width(ch);
            if(v > max) max = v;
        }
        return max * (useRender ? renderProps.scaleX : 1f);
    }
    public float height(boolean useRender) {
        return split(useRender).size() * 9 * (useRender ? renderProps.scaleY : 1f);
    }
    public List<FormattedCharSequence> split(boolean useRender) {
        int maxw = renderProps.maxWidth != -1f ? (int) (renderProps.maxWidth / (useRender ? renderProps.scaleX : 1f)) : Integer.MAX_VALUE;
        return Utils.mc().font.split(text, maxw);
    }

    public MutableComponent get() {
        return text;
    }
}
