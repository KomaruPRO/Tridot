package pro.komaru.tridot.client.graphics.shader.postprocess;

import java.util.function.*;

public abstract class PostProcessInstance{
    public float time = 0F;
    public boolean removed;

    public void update(double deltaTime){
        time += deltaTime / 20F;
    }

    public abstract void writeDataToBuffer(BiConsumer<Integer, Float> writer);

    public void remove(){
        removed = true;
    }

    public boolean isRemoved(){
        return removed;
    }

    public float getTime(){
        return time;
    }
}
