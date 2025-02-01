package pro.komaru.tridot.client.graphics.gui.bossbars;

import net.minecraft.resources.*;
import net.minecraftforge.api.distmarker.*;
import pro.komaru.tridot.*;

import java.awt.*;
import java.util.*;

@OnlyIn(Dist.CLIENT)
public class Bossbar{
    public static Map<String, Bossbar> bossbars = new HashMap<>();

    public boolean rainbow = false;
    private final Color color;
    private final ResourceLocation texture;

    public Bossbar(ResourceLocation tex){
        this.texture = tex;
        this.color = new Color(1, 1, 1);
    }

    public Bossbar(Color color){
        this.texture = new ResourceLocation(TridotLib.ID, "textures/gui/bossbars/base.png");
        this.color = color;
    }

    public Bossbar(boolean rainbow){
        this.texture = new ResourceLocation(TridotLib.ID, "textures/gui/bossbars/base.png");
        this.color = new Color(1, 1, 1);
        this.rainbow = rainbow;
    }

    public Color getColor(){
        return color;
    }

    public ResourceLocation getTexture(){
        return texture;
    }
}