package pro.komaru.tridot.client.render.gui.overlay;

import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.*;

@OnlyIn(Dist.CLIENT)
public interface OverlayInstance{

    default void tick(TickEvent.ClientTickEvent event){
    }

    void onDraw(RenderGuiOverlayEvent.Post event);
}
