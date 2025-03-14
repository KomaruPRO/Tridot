package pro.komaru.tridot.client.render.screenshake;


import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import org.jetbrains.annotations.NotNull;
import pro.komaru.tridot.TridotLib;
import pro.komaru.tridot.client.ClientTick;
import pro.komaru.tridot.common.config.ClientConfig;
import pro.komaru.tridot.util.Tmp;
import pro.komaru.tridot.util.math.ArcRandom;
import pro.komaru.tridot.util.math.Mathf;
import pro.komaru.tridot.util.math.raycast.RayCast;
import pro.komaru.tridot.util.math.raycast.RayCastContext;
import pro.komaru.tridot.util.math.raycast.RayHitResult;
import pro.komaru.tridot.util.phys.Vec3;

import java.util.ArrayList;

public class ScreenshakeHandler{
    public static final ArrayList<ScreenshakeInstance> INSTANCES = new ArrayList<>();
    public static float intensityRotation;
    public static float intensityPosition;
    public static float intensityFov;
    public static float intensityFovNormalize;
    public static Vec3 intensityVector = Vec3.zero();
    public static Vec3 intensityVectorOld = Vec3.zero();

    public static ArcRandom rand = Tmp.rnd;

    public static void cameraTick(Camera camera){
        if(intensityRotation > 0){
            float yawOffset = randomizeOffset(intensityRotation);
            float pitchOffset = randomizeOffset(intensityRotation);
            camera.setRotation(camera.getYRot() + yawOffset, camera.getXRot() + pitchOffset);
        }

        boolean cameraUpdate = false;
        Vec3 pos = Vec3.from(camera.getPosition());
        Vec3 cameraPos = pos;

        if(intensityPosition > 0){
            pos = intensityPosVec(pos).cpy();
            cameraUpdate = true;
        }
        if(!intensityVector.equals(Vec3.zero()) && !intensityVectorOld.equals(Vec3.zero())){
            pos = intensityVec(pos).cpy();
            cameraUpdate = true;
        }

        if(cameraUpdate){
            Level level = TridotLib.PROXY.getLevel();
            if(level != null){
                RayHitResult hitResult = RayCast.getHit(level, new RayCastContext(cameraPos, pos).setBlockShape(RayCastContext.Block.VISUAL));
                double distance = Math.max(0f,cameraPos.cpy().sub(hitResult.getPos()).len() - 0.1f);

                float dX = cameraPos.x() - hitResult.getPos().x();
                float dY = cameraPos.y() - hitResult.getPos().y();
                float dZ = cameraPos.z() - hitResult.getPos().z();

                float pitch = (float) (Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI);
                float x = (float) (Math.sin(pitch) * Math.cos(Math.atan2(dZ, dX)) * distance);
                float y = (float) (Math.cos(pitch) * distance);
                float z = (float) (Math.sin(pitch) * Math.sin(Math.atan2(dZ, dX)) * distance);
                pos = cameraPos.cpy().add(x,y,z);
            }

            camera.setPosition(pos.x(), pos.y(), pos.z());
        }
    }

    private static @NotNull Vec3 intensityVec(Vec3 pos) {
        return pos.cpy().add(intensityVectorOld.cpy().lerp(intensityVector,ClientTick.partialTicks));
    }

    private static @NotNull Vec3 intensityPosVec(Vec3 pos) {
        float angleA = rand.nextFloat() * Mathf.pi * 2f;
        float angleB = rand.nextFloat() * Mathf.pi * 2f;
        float x = (float)(Math.cos(angleA) * Math.cos(angleB)) * randomizeOffset(intensityPosition);
        float y = (float)(Math.sin(angleA) * Math.cos(angleB)) * randomizeOffset(intensityPosition);
        float z = (float)Math.sin(angleB) * randomizeOffset(intensityPosition);
        return pos.cpy().add(x,y,z);
    }

    public static void fovTick(ComputeFovModifierEvent event){
        float fovModifier = event.getFovModifier();
        if(fovModifier != event.getNewFovModifier()) fovModifier = event.getNewFovModifier();
        boolean update = false;
        if(intensityFov >= 0){
            float offset = randomizeOffset(intensityFov);
            fovModifier = fovModifier + offset;
            update = true;
        }

        if(intensityFovNormalize != 0){
            fovModifier = fovModifier + intensityFovNormalize;
            update = true;
        }

        if(update) event.setNewFovModifier((float)Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0F, fovModifier));
    }

    public static float apply(ScreenshakeInstance instance, float current, float update) {
        if (instance.isNormalize) {
            return current + update;
        } else {
            return update;
        }
    }

    public static void clientTick(Camera camera){
        float intensity = (float) (double) ClientConfig.SCREENSHAKE_INTENSITY.get();
        float rotationNormalize = 0;
        float positionNormalize = 0;
        float fovNormalize = 0;
        float fovNorm = 0;
        float rotation = 0;
        float position = 0;
        float fov = 0;
        Vec3 vector = Vec3.zero();
        for(ScreenshakeInstance instance : INSTANCES){
            float update = instance.updateIntensity(camera);
            if(instance.isRotation)rotation = apply(instance, rotationNormalize, update);
            if(instance.isPosition)position = apply(instance, positionNormalize, update);
            if(instance.isFov){
                if(instance.isNormalize){
                    fovNormalize = fovNormalize + update;
                }else{
                    if(instance.isFovNormalize){
                        fovNorm = fovNorm + update;
                    }else{
                        fov = fov + update;
                    }
                }
            }

            if(instance.isVector){
                Vec3 scaledVector = instance.vector.scale(update * intensity);
                if (instance.isNormalize) {
                    vector = vector.add(scaledVector.sub(vector));
                } else {
                    vector = vector.add(scaledVector);
                }
            }
        }

        rotationNormalize = Math.min(rotationNormalize, intensity);
        positionNormalize = Math.min(positionNormalize, intensity);
        fovNorm = Math.min(fovNorm, intensity);
        rotation = rotation * intensity;
        position = position * intensity;
        fov = fov * intensity;

        intensityRotation = (float)Math.max(Math.pow(rotationNormalize, 3f), rotation);
        intensityPosition = (float)Math.max(Math.pow(positionNormalize / 2f, 3f), position);
        intensityFov = Math.max(fovNorm, fov);
        intensityFovNormalize = fovNormalize;
        intensityVectorOld = intensityVector;
        intensityVector = vector;
        INSTANCES.removeIf(i -> i.progress >= i.duration);
    }

    public static void add(ScreenshakeInstance instance){
        INSTANCES.add(instance);
    }

    public static float randomizeOffset(float offset){
        if(offset == 0) return 0f;
        return rand.nextFloat(-offset * 2, offset * 2);
    }
}
