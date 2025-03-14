package pro.komaru.tridot.client.render.screenshake;

import pro.komaru.tridot.util.math.ArcRandom;
import pro.komaru.tridot.util.math.Interp;
import net.minecraft.client.*;
import net.minecraft.util.*;
import pro.komaru.tridot.util.phys.Vec3;

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

    public Vec3 vector = Vec3.zero();

    public static final ArcRandom random = new ArcRandom();

    public ScreenshakeInstance(int duration){
        this.duration = duration;
    }

    public ScreenshakeInstance intensity(float intensity){
        return intensity(intensity, intensity);
    }

    public ScreenshakeInstance intensity(float intensity1, float intensity2){
        return intensity(intensity1, intensity2, intensity2);
    }

    public ScreenshakeInstance intensity(float intensity1, float intensity2, float intensity3){
        this.intensity1 = intensity1;
        this.intensity2 = intensity2;
        this.intensity3 = intensity3;
        return this;
    }

    public ScreenshakeInstance interp(Interp interp){
        return interp(interp, interp);
    }

    public ScreenshakeInstance interp(Interp intensityCurveStartEasing, Interp intensityCurveEndEasing){
        this.intensityCurveStartEasing = intensityCurveStartEasing;
        this.intensityCurveEndEasing = intensityCurveEndEasing;
        return this;
    }

    public ScreenshakeInstance normalize(boolean normalize){
        this.isNormalize = normalize;
        return this;
    }

    public ScreenshakeInstance rot(boolean rotation){
        this.isRotation = rotation;
        return this;
    }

    public ScreenshakeInstance pos(boolean position){
        this.isPosition = position;
        return this;
    }

    public ScreenshakeInstance vec(boolean vector){
        this.isVector = vector;
        return this;
    }

    public ScreenshakeInstance vec(Vec3 vector){
        this.vector = vector;
        return this;
    }

    public ScreenshakeInstance randVec(){
        double angleA = random.nextDouble() * Math.PI * 2;
        double angleB = random.nextDouble() * Math.PI * 2;
        float x = (float)(Math.cos(angleA) * Math.cos(angleB));
        float y = (float)(Math.sin(angleA) * Math.cos(angleB));
        float z = (float)Math.sin(angleB);
        this.vector = new Vec3(x, y, z);
        return this;
    }

    public ScreenshakeInstance fov(boolean fov){
        this.isFov = fov;
        return this;
    }

    public ScreenshakeInstance normalizeFov(boolean fovNormalize){
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