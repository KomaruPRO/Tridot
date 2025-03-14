package pro.komaru.tridot.client.render.screenshake;

import pro.komaru.tridot.core.math.Interp;
import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;

import java.util.*;

//todo fluffy
public class ScreenshakeInstance{
    public int progress;
    public final int duration;
    public float intensity1, intensity2, intensity3;
    public Interp intensityCurveStartEasing = Interp.linear, intensityCurveEndEasing = Interp.linear;

    public boolean isNormalize = true;
    public boolean isRotation = true;
    public boolean isPosition = false;
    public boolean isVector = false;
    public boolean isFov = false;
    public boolean isFovNormalize = false;

    public Vec3 vector = Vec3.ZERO;

    public static final Random random = new Random();

    public ScreenshakeInstance(int duration){
        this.duration = duration;
    }

    public ScreenshakeInstance setIntensity(float intensity){
        return setIntensity(intensity, intensity);
    }

    public ScreenshakeInstance setIntensity(float intensity1, float intensity2){
        return setIntensity(intensity1, intensity2, intensity2);
    }

    public ScreenshakeInstance setIntensity(float intensity1, float intensity2, float intensity3){
        this.intensity1 = intensity1;
        this.intensity2 = intensity2;
        this.intensity3 = intensity3;
        return this;
    }

    public ScreenshakeInstance setEasing(Interp easing){
        return setEasing(easing, easing);
    }

    public ScreenshakeInstance setEasing(Interp intensityCurveStartEasing, Interp intensityCurveEndEasing){
        this.intensityCurveStartEasing = intensityCurveStartEasing;
        this.intensityCurveEndEasing = intensityCurveEndEasing;
        return this;
    }

    public ScreenshakeInstance setNormalize(boolean normalize){
        this.isNormalize = normalize;
        return this;
    }

    public ScreenshakeInstance setRotation(boolean rotation){
        this.isRotation = rotation;
        return this;
    }

    public ScreenshakeInstance setPosition(boolean position){
        this.isPosition = position;
        return this;
    }

    public ScreenshakeInstance setVector(boolean vector){
        this.isVector = vector;
        return this;
    }

    public ScreenshakeInstance setVector(Vec3 vector){
        this.vector = vector;
        return this;
    }

    public ScreenshakeInstance setRandomVector(){
        double angleA = random.nextDouble() * Math.PI * 2;
        double angleB = random.nextDouble() * Math.PI * 2;
        float x = (float)(Math.cos(angleA) * Math.cos(angleB));
        float y = (float)(Math.sin(angleA) * Math.cos(angleB));
        float z = (float)Math.sin(angleB);
        this.vector = new Vec3(x, y, z);
        return this;
    }

    public ScreenshakeInstance setFov(boolean fov){
        this.isFov = fov;
        return this;
    }

    public ScreenshakeInstance setFovNormalize(boolean fovNormalize){
        this.isFovNormalize = fovNormalize;
        return this;
    }

    public float updateIntensity(Camera camera){
        progress++;
        float percentage = progress / (float)duration;
        if(intensity2 != intensity3){
            float v = percentage >= 0.5f ? (percentage - 0.5f)/0.5f : percentage/0.5f;
            return Mth.lerp(intensityCurveEndEasing.apply(v), intensity2, intensity1);
        }else{
            return Mth.lerp(intensityCurveStartEasing.apply(percentage), intensity1, intensity2);
        }
    }
}