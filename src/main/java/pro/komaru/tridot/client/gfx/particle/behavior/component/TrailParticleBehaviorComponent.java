package pro.komaru.tridot.client.gfx.particle.behavior.component;

import pro.komaru.tridot.client.gfx.trail.TrailPointBuilder;

public class TrailParticleBehaviorComponent extends ParticleBehaviorComponent{
    public TrailPointBuilder trailPointBuilder = TrailPointBuilder.create(10);

    public float st;
    public float mt;
    public float et;

    public float r;
    public float g;
    public float b;
    public float a;

    public float[] hsv1 = new float[3], hsv2 = new float[3];
}
