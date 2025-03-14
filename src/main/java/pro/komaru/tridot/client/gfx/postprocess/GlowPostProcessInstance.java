package pro.komaru.tridot.client.gfx.postprocess;

import org.joml.*;
import pro.komaru.tridot.client.ClientTick;
import pro.komaru.tridot.util.struct.func.Cons2;

public class GlowPostProcessInstance extends PostProcessInstance{
    public Vector3f center;
    public Vector3f color;
    public float radius = 10;
    public float intensity = 1;
    public float fade = 1;
    public float startTick = 0;
    public float tickTime = 0;
    public float fadeTime = 20;
    public boolean isFade = true;

    public GlowPostProcessInstance(Vector3f center, Vector3f color){
        this.center = center;
        this.color = color;
        this.startTick = ClientTick.getTotal();
    }

    public GlowPostProcessInstance setRadius(float radius){
        this.radius = radius;
        return this;
    }

    public GlowPostProcessInstance setIntensity(float intensity){
        this.intensity = intensity;
        return this;
    }

    public GlowPostProcessInstance setFade(float fade){
        this.fade = fade;
        return this;
    }

    public GlowPostProcessInstance setStartTime(float time){
        this.startTick = time;
        return this;
    }

    public GlowPostProcessInstance setTime(float time){
        this.tickTime = time;
        return this;
    }

    public GlowPostProcessInstance setFadeTime(float fade){
        this.fadeTime = fade;
        return this;
    }

    public GlowPostProcessInstance setIsFade(boolean fade){
        this.isFade = fade;
        return this;
    }

    public void fadeUpdate(float deltaTime){
        if(isFade){
            tickTime = ClientTick.getTotal() - startTick;
            fade = 1f - (tickTime / fadeTime);
            if(tickTime > fadeTime){
                fade = 0;
                remove();
            }
        }
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        fadeUpdate(deltaTime);
    }

    @Override
    public void writeDataToBuffer(Cons2<Integer, Float> writer){
        writer.get(0, center.x());
        writer.get(1, center.y());
        writer.get(2, center.z());
        writer.get(3, color.x());
        writer.get(4, color.y());
        writer.get(5, color.z());
        writer.get(6, radius);
        writer.get(7, intensity);
        writer.get(8, fade);
        writer.get(9, tickTime);
        writer.get(10, fadeTime);
    }
}
