package pro.komaru.tridot.client.model.render.item;

import com.mojang.blaze3d.vertex.*;

public class GhostVertexConsumer implements VertexConsumer{
    private final VertexConsumer wrapped;
    private final float alpha;
    private final float time;

    public GhostVertexConsumer(VertexConsumer wrapped, float alpha, float time) {
        this.wrapped = wrapped;
        this.alpha = alpha;
        this.time = time;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        double offset = Math.sin((y * 10) + time) * 0.02;
        return wrapped.vertex(x + offset, y, z + offset);
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        return wrapped.color(100, 200, 255, (int)(255 * this.alpha));
    }

    @Override
    public VertexConsumer uv(float u, float v) {
        return wrapped.uv(u, v);
    }

    @Override public VertexConsumer overlayCoords(int u, int v) { return wrapped.overlayCoords(u, v); }
    @Override public VertexConsumer uv2(int u, int v) { return wrapped.uv2(u, v); }
    @Override public VertexConsumer normal(float x, float y, float z) { return wrapped.normal(x, y, z); }
    @Override public void endVertex() { wrapped.endVertex(); }
    @Override public void defaultColor(int r, int g, int b, int a) { wrapped.defaultColor(r, g, b, a); }
    @Override public void unsetDefaultColor() { wrapped.unsetDefaultColor(); }
}