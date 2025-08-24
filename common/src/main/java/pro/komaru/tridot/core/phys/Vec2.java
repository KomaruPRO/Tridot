package pro.komaru.tridot.core.phys;

import net.minecraft.nbt.CompoundTag;
import pro.komaru.tridot.core.primitive.Pos2;
import pro.komaru.tridot.core.math.Interp;

public class Vec2 implements Pos2 {

    public static Vec2[] d4 = new Vec2[] {
        new Vec2(1, 0),
        new Vec2(0, 1),
        new Vec2(-1, 0),
        new Vec2(0, -1)
    };
    public static Vec2[] d8 = new Vec2[] {
        new Vec2(1, 0),
        new Vec2(1, 1),
        new Vec2(0, 1),
        new Vec2(-1, 1),
        new Vec2(-1, 0),
        new Vec2(-1, -1),
        new Vec2(0, -1),
        new Vec2(1, -1)
    };

    public float x,y;

    public Vec2(float x, float y) {
        set(x,y);
    }
    public Vec2(Pos2 pos) {
        set(pos);
    }
    public Vec2() {
        set(0f,0f);
    }

    public Vec2 cpy() {
        return new Vec2(this);
    }

    @Override
    public float x() {
        return x;
    }

    @Override
    public void x(float value) {
        x = value;
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public void y(float value) {
        y = value;
    }

    public Vec2 add(Pos2 pos) {
        return add(pos.x(),pos.y());
    }
    public Vec2 add(float x, float y) {
        set(x()+x, y()+y);
        return this;
    }

    public Vec2 sub(Pos2 pos) {
        return sub(pos.x(), pos.y());
    }
    public Vec2 sub(float x, float y) {
        return add(-x,-y);
    }

    public Vec2 set(Pos2 other) {
        return set(other.x(),other.y());
    }
    public Vec2 set(float x, float y) {
        x(x); this.y(y);
        return this;
    }

    public Vec2 scale(float scl) {
        return scale(scl,scl);
    }
    public Vec2 scale(Pos2 pos) {
        return scale(pos.x(),pos.y());
    }
    public Vec2 scale(float x, float y) {
        set(x()*x, y()*y);
        return this;
    }

    public float len2() {
        return x()* x()+ y()* y();
    }
    public float len() {
        return (float) Math.sqrt(len2());
    }

    public Vec2 nor() {
        return scale(1/len());
    }

    public net.minecraft.world.phys.Vec2 toMC() {
        return new net.minecraft.world.phys.Vec2(x(), y());
    }
    public CompoundTag toTag() {
        var nbt = new CompoundTag();
        nbt.putFloat("x", x());
        nbt.putFloat("y", y());
        return nbt;
    }
    public Vec2 fromTag(CompoundTag nbt) {
        return set(nbt.getFloat("x"),nbt.getFloat("y"));
    }

    public Vec2 lerp(Pos2 pos, float progress, Interp interp) {
        float x = interp.apply(progress, x(),pos.x());
        float y = interp.apply(progress, y(),pos.y());
        return set(x,y);
    }

    public Vec2 rotate(float degrees) {
        float radians = (float) Math.toRadians(degrees);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        float x = x(), y = y();
        set(x * cos - y * sin, x * sin + y * cos);
        return this;
    }
    public float angle() {
        return (float) Math.toDegrees(Math.atan2(y(), x()));
    }
    public float angleBetween(Pos2 other) {
        return cpy().sub(other).inv().angle();
    }
    public float cross(Pos2 other) {
        return x() * other.y() - y() * other.x();
    }
    public Vec2 rotationFix() {
        if(x() > 180) x(x()-360);
        if(y() > 180) this.y(y()-360);
        return this;
    }
    public float dst(Pos2 b) {
        return cpy().sub(b).len();
    }
    public float dst2(Pos2 b) {
        return cpy().sub(b).len2();
    }
    public Vec2 inv() {
        return scale(-1f);
    }

    public boolean posEquals(Pos2 b) {
        return b.x() == x() && b.y() == y();
    }

    public void constrain(Vec2 target, float targetDist, boolean less, boolean greater) {
        var delta = target.cpy().sub(this);
        float dst = delta.len();

        if(less && dst < targetDist) return;
        if(greater && dst > targetDist) return;
        if (dst == 0) {
            add(targetDist, 0);
            return;
        }

        float scale = targetDist / dst;
        float newX = target.x() + delta.x() * scale;
        float newY = target.y() + delta.y() * scale;

        set(newX, newY);
    }
    public void constrain2(Vec2 b, float targetDist, boolean less, boolean greater) {
        var delta = b.cpy().sub(this);
        float dst = delta.len();

        if(less && dst < targetDist) return;
        if(greater && dst > targetDist) return;
        if (dst == 0) return;

        float diff = (dst - targetDist) / dst / 2f;

        float offsetX = delta.x() * diff;
        float offsetY = delta.y() * diff;

        add(offsetX, offsetY);
        b.sub(offsetX, offsetY);
    }
    public float dot(Pos2 other) {
        return dot(other.x(), other.y());
    }
    public float dot(float ox, float oy){
        return x * ox + y * oy;
    }
    public Vec2 trns(float angle, float amount){
        return set(amount, 0).rotate(angle);
    }

    public Vec2 trns(float angle, float x, float y){
        return set(x, y).rotate(angle);
    }
}
