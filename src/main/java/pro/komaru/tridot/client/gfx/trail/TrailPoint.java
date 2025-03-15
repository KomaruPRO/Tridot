package pro.komaru.tridot.client.gfx.trail;

import org.joml.Vector4f;
import pro.komaru.tridot.util.phys.Vec3;

public class TrailPoint{
    private final Vec3 position;
    private int timeActive;

    public TrailPoint(Vec3 position, int timeActive){
        this.position = position;
        this.timeActive = timeActive;
    }

    public TrailPoint(Vec3 position){
        this(position, 0);
    }

    public Vector4f getMatrixPosition(){
        Vec3 position = getPosition();
        return new Vector4f(position.x, position.y, position.z, 1.0f);
    }

    public Vec3 getPosition(){
        return position;
    }

    public int getTimeActive(){
        return timeActive;
    }

    public TrailPoint lerp(TrailPoint trailPoint, float delta){
        Vec3 position = getPosition();
        return new TrailPoint(position.lerp(trailPoint.position, delta), timeActive);
    }

    public void tick(){
        timeActive++;
    }
}