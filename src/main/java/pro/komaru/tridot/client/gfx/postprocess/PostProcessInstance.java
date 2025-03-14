package pro.komaru.tridot.client.gfx.postprocess;

import pro.komaru.tridot.util.struct.func.Cons2;


public abstract class PostProcessInstance{
    public float time = 0F;
    public boolean removed;

    public void update(float deltaTime){
        time += deltaTime / 20F;
    }

    public abstract void writeDataToBuffer(Cons2<Integer, Float> writer);

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
