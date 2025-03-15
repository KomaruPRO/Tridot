package pro.komaru.tridot.client.gfx.trail;

import org.joml.Vector4f;
import pro.komaru.tridot.util.phys.Vec3;

public class TrailPoint{
    public final Vec3 pos;
    public int lifetime;

    public TrailPoint(Vec3 pos, int lifetime){
        this.pos = pos;
        this.lifetime = lifetime;
    }

    public TrailPoint(Vec3 pos){
        this(pos, 0);
    }

    public Vector4f getMatrixPosition(){
        return new Vector4f(pos.x, pos.y, pos.z, 1.0f);
    }

    public TrailPoint lerp(TrailPoint trailPoint, float delta){
        return new TrailPoint(pos.cpy().lerp(trailPoint.pos, delta), lifetime);
    }

    public void tick(){
        lifetime++;
    }
}
