package pro.komaru.tridot.oclient.graphics.gui.screenshake;

import pro.komaru.tridot.ocore.math.Interp;
import net.minecraft.client.*;
import net.minecraft.world.phys.*;
import org.joml.*;

import java.lang.Math;

//todo fluffy
public class PositionedScreenshakeInstance extends ScreenshakeInstance{
    public final Vec3 position;
    public final float falloffDistance;
    public final float maxDistance;
    public final Interp falloffEasing;

    public PositionedScreenshakeInstance(int duration, Vec3 position, float falloffDistance, float maxDistance, Interp falloffEasing){
        super(duration);
        this.position = position;
        this.falloffDistance = falloffDistance;
        this.maxDistance = maxDistance;
        this.falloffEasing = falloffEasing;
    }

    public PositionedScreenshakeInstance(int duration, Vec3 position, float falloffDistance, float maxDistance){
        this(duration, position, falloffDistance, maxDistance, Interp.linear);
    }

    @Override
    public float updateIntensity(Camera camera){
        float intensity = super.updateIntensity(camera);
        float distance = (float)position.distanceTo(camera.getPosition());
        if(distance > maxDistance){
            return 0;
        }
        float distanceMultiplier = 1;
        if(distance > falloffDistance){
            float remaining = maxDistance - falloffDistance;
            float current = distance - falloffDistance;
            distanceMultiplier = 1 - current / remaining;
        }
        Vector3f lookDirection = camera.getLookVector();
        Vec3 directionToScreenshake = position.subtract(camera.getPosition()).normalize();
        float angle = Math.max(0, lookDirection.dot(new Vector3f((float)directionToScreenshake.x, (float)directionToScreenshake.y, (float)directionToScreenshake.z)));
        return (intensity + (intensity * angle)) * 0.5f * distanceMultiplier;
    }
}