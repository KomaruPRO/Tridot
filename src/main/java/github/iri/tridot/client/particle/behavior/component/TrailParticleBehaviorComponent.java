package github.iri.tridot.client.particle.behavior.component;

import github.iri.tridot.client.render.trail.*;

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
