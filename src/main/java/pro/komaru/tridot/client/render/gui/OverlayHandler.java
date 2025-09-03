package pro.komaru.tridot.client.render.gui;

import net.minecraft.resources.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.*;
import pro.komaru.tridot.util.struct.data.*;

@OnlyIn(Dist.CLIENT)
public class OverlayHandler{
    public static final Seq<OverlayInstance> instanceSeq = Seq.with();
    public static void addInstance(OverlayInstance instance) {
        instanceSeq.add(instance);
    }

    public static void renderInstances(RenderGuiOverlayEvent.Post event) {
        instanceSeq.forEach((inst) -> inst.onDraw(event));
    }

    public static void tickInstances(TickEvent.ClientTickEvent event) {
        instanceSeq.forEach((inst) -> inst.tick(event));
    }
}