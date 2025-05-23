package pro.komaru.tridot.api.render.bossbars;

import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.resources.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.util.Col;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractBossbar{
    public static Map<String, AbstractBossbar> bossbars = new HashMap<>(); // maybe move it somewhere else?

    public boolean rainbow = false;
    private final Col color;
    private final ResourceLocation texture;

    public AbstractBossbar(ResourceLocation tex){
        this.texture = tex;
        this.color = new Col(1, 1, 1);
    }

    public AbstractBossbar(Col color){
        this.texture = new ResourceLocation(Tridot.ID, "textures/gui/bossbars/base.png");
        this.color = color;
    }

    public AbstractBossbar(boolean rainbow){
        this.texture = new ResourceLocation(Tridot.ID, "textures/gui/bossbars/base.png");
        this.color = new Col(1, 1, 1);
        this.rainbow = rainbow;
    }

    public abstract void render(BossEventProgress ev, LerpingBossEvent event, int offset, int screenWidth, GuiGraphics pGuiGraphics, AbstractBossbar abstractBossbar, Minecraft mc);

    public Col getColor(){
        return color;
    }

    public ResourceLocation getTexture(){
        return texture;
    }
}