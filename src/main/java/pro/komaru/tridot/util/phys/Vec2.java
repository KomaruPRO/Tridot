package pro.komaru.tridot.util.phys;

import net.minecraft.nbt.CompoundTag;
import pro.komaru.tridot.util.comps.phys.Pos2;
import pro.komaru.tridot.util.comps.phys.X;
import pro.komaru.tridot.util.comps.phys.Y;
import pro.komaru.tridot.util.math.Interp;

import java.io.Serializable;

public class Vec2 implements Pos2, Serializable {

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
    public Vec2 x(float value) {
        x = value;
        return this;
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public Vec2 y(float value) {
        y = value;
        return this;
    }

    public <XY extends X & Y> Vec2 add(XY xy) {
        return add(xy,xy);
    }
    public Vec2 add(X x, Y y) {
        return add(x.x(),y.y());
    }
    public Vec2 add(float x, float y) {
        set(x()+x, y()+y);
        return this;
    }

    public <XY extends X & Y> Vec2 sub(XY xy) {
        return sub(xy,xy);
    }
    public Vec2 sub(X x, Y y) {
        return sub(x.x(),y.y());
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
    public <XY extends X & Y> Vec2 scale(XY xy) {
        return scale(xy,xy);
    }
    public Vec2 scale(X x, Y y) {
        return scale(x.x(),y.y());
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

    public net.minecraft.world.phys.Vec2 mcVec() {
        return new net.minecraft.world.phys.Vec2(x(), y());
    }
    public CompoundTag toNbtVec() {
        var nbt = new CompoundTag();
        nbt.putFloat("x", x());
        nbt.putFloat("y", y());
        return nbt;
    }
    public Vec2 fromNbtVec(CompoundTag nbt) {
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
        return other.cpypos().vec().sub(this).angle();
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

}
