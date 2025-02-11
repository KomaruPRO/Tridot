package pro.komaru.tridot.client.graphics.render;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;

//todo fluffy
public class TridotRenderType extends RenderType{
    public final CompositeState state;

    public static TridotRenderType createRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, CompositeState state){
        return new TridotRenderType(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, state);
    }

    public TridotRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, CompositeState state){
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, () -> {
            state.states.forEach(RenderStateShard::setupRenderState);
        }, () -> {
            state.states.forEach(RenderStateShard::clearRenderState);
        });
        this.state = state;
    }
}
