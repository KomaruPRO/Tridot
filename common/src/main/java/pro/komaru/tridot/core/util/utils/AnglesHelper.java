package pro.komaru.tridot.core.util.utils;

import pro.komaru.tridot.core.math.Mathf;
import pro.komaru.tridot.core.math.Rand;
import pro.komaru.tridot.core.phys.Vec2;
import pro.komaru.tridot.core.struct.func.Cons2;

public class AnglesHelper {
    private static final AnglesHelper instance = new AnglesHelper();
    public static AnglesHelper get() {
        return instance;
    }

    private final Rand rand = new Rand();
    private final Vec2 rv = new Vec2();

    public float forwardDistance(float angle1, float angle2){
        return Math.abs(angle1 - angle2);
    }

    public float backwardDistance(float angle1, float angle2){
        return 360 - Math.abs(angle1 - angle2);
    }

    public boolean within(float a, float b, float margin){
        return angleDist(a, b) <= margin;
    }

    public float angleDist(float a, float b){
        a = Mathf.mod(a, 360f);
        b = Mathf.mod(b, 360f);
        return Math.min((a - b) < 0 ? a - b + 360 : a - b, (b - a) < 0 ? b - a + 360 : b - a);
    }

    public boolean near(float a, float b, float range){
        return angleDist(a, b) < range;
    }

    public float clampRange(float angle, float dest, float range){
        float dst = angleDist(angle, dest);
        return dst <= range ? angle : moveToward(angle, dest, dst - range);
    }

    public float moveToward(float angle, float to, float speed){
        if(Math.abs(angleDist(angle, to)) < speed) return to;
        angle = Mathf.mod(angle, 360f);
        to = Mathf.mod(to, 360f);

        if(angle > to == backwardDistance(angle, to) > forwardDistance(angle, to)){
            angle -= speed;
        }else{
            angle += speed;
        }

        return angle;
    }

    public float angle(float x, float y){
        return angle(0, 0, x, y);
    }

    public float angle(float x, float y, float x2, float y2){
        float ang = Mathf.atan2(x2 - x, y2 - y) * Mathf.radDeg;
        if(ang < 0) ang += 360f;
        return ang;
    }

    public float angleRad(float x, float y, float x2, float y2){
        return Mathf.atan2(x2 - x, y2 - y);
    }

    public float trnsx(float angle, float len){
        return len * Mathf.cosDeg(angle);
    }

    public float trnsy(float angle, float len){
        return len * Mathf.sinDeg(angle);
    }

    public float trnsx(float angle, float x, float y){
        return rv.set(x, y).rotate(angle).x;
    }

    public float trnsy(float angle, float x, float y){
        return rv.set(x, y).rotate(angle).y;
    }

    public float mouseAngle(float cx, float cy, float mx, float my){
        return angle(cx, cy, mx,my);
    }

    public void circleVectors(int points, float length, Cons2<Float,Float> pos){
        for(int i = 0; i < points; i++){
            float f = i * 360f / points;
            pos.get(trnsx(f, length), trnsy(f, length));
        }
    }

    public void circleVectors(int points, float length, float offset, Cons2<Float,Float> pos){
        for(int i = 0; i < points; i++){
            float f = i * 360f / points + offset;
            pos.get(trnsx(f, length), trnsy(f, length));
        }
    }

    public void randVectors(long seed, int amount, float length, Cons2<Float,Float> cons){
        rand.setSeed(seed);
        for(int i = 0; i < amount; i++){
            rv.trns(rand.random(360f), length);
            cons.get(rv.x, rv.y);
        }
    }

    public void randLenVectors(long seed, int amount, float length, Cons2<Float,Float> cons){
        rand.setSeed(seed);
        for(int i = 0; i < amount; i++){
            rv.trns(rand.random(360f), rand.random(length));
            cons.get(rv.x, rv.y);
        }
    }

    public void randLenVectors(long seed, int amount, float minLength, float length, Cons2<Float,Float> cons){
        rand.setSeed(seed);
        for(int i = 0; i < amount; i++){
            rv.trns(rand.random(360f), minLength + rand.random(length));
            cons.get(rv.x, rv.y);
        }
    }

    public void randLenVectors(long seed, int amount, float length, float angle, float range, Cons2<Float,Float> cons){
        rand.setSeed(seed);
        for(int i = 0; i < amount; i++){
            rv.trns(angle + rand.range(range), rand.random(length));
            cons.get(rv.x, rv.y);
        }
    }

    public void randLenVectors(long seed, int amount, float length, float angle, float range, float spread, Cons2<Float,Float> cons){
        rand.setSeed(seed);
        for(int i = 0; i < amount; i++){
            rv.trns(angle + rand.range(range), rand.random(length));
            cons.get(rv.x + rand.range(spread), rv.y + rand.range(spread));
        }
    }

    public void randLenVectors(long seed, float fin, int amount, float length, ParticleConsumer cons){
        rand.setSeed(seed);
        for(int i = 0; i < amount; i++){
            float l = rand.nextFloat();
            rv.trns(rand.random(360f), length * l * fin);
            cons.accept(rv.x, rv.y, fin * l, (1f - fin) * l);
        }
    }

    public void randLenVectors(long seed, float fin, int amount, float length, float angle, float range, ParticleConsumer cons){
        rand.setSeed(seed);
        for(int i = 0; i < amount; i++){
            rv.trns(angle + rand.range(range), rand.random(length * fin));
            cons.accept(rv.x, rv.y, fin * (rand.nextFloat()), 0f);
        }
    }

    public interface ParticleConsumer{
        void accept(float x, float y, float fin, float fout);
    }

}

