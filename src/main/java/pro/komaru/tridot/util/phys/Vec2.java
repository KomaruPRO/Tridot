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
    public float cx() {
        return x;
    }

    @Override
    public Vec2 cx(float value) {
        x = value;
        return this;
    }

    @Override
    public float cy() {
        return y;
    }

    @Override
    public Vec2 cy(float value) {
        y = value;
        return this;
    }

    public <XY extends X & Y> Vec2 add(XY xy) {
        return add(xy,xy);
    }
    public Vec2 add(X x, Y y) {
        return add(x.cx(),y.cy());
    }
    public Vec2 add(float x, float y) {
        set(cx()+x, cy()+y);
        return this;
    }

    public <XY extends X & Y> Vec2 sub(XY xy) {
        return sub(xy,xy);
    }
    public Vec2 sub(X x, Y y) {
        return sub(x.cx(),y.cy());
    }
    public Vec2 sub(float x, float y) {
        return add(-x,-y);
    }

    public Vec2 set(Pos2 other) {
        return set(other.cx(),other.cy());
    }
    public Vec2 set(float x, float y) {
        cx(x); this.cy(y);
        return this;
    }

    public Vec2 scale(float scl) {
        return scale(scl,scl);
    }
    public <XY extends X & Y> Vec2 scale(XY xy) {
        return scale(xy,xy);
    }
    public Vec2 scale(X x, Y y) {
        return scale(x.cx(),y.cy());
    }
    public Vec2 scale(float x, float y) {
        set(cx()*x, cy()*y);
        return this;
    }

    public float len2() {
        return cx()* cx()+ cy()* cy();
    }
    public float len() {
        return (float) Math.sqrt(len2());
    }

    public Vec2 nor() {
        return scale(1/len());
    }

    public net.minecraft.world.phys.Vec2 mcVec() {
        return new net.minecraft.world.phys.Vec2(cx(), cy());
    }
    public CompoundTag toNbtVec() {
        var nbt = new CompoundTag();
        nbt.putFloat("x", cx());
        nbt.putFloat("y", cy());
        return nbt;
    }
    public Vec2 fromNbtVec(CompoundTag nbt) {
        return set(nbt.getFloat("x"),nbt.getFloat("y"));
    }

    public Vec2 lerp(Pos2 pos, float progress, Interp interp) {
        float x = interp.apply(progress, cx(),pos.cx());
        float y = interp.apply(progress, cy(),pos.cy());
        return set(x,y);
    }

    public Vec2 rotate(float degrees) {
        float radians = (float) Math.toRadians(degrees);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        float x = cx(), y = cy();
        set(x * cos - y * sin, x * sin + y * cos);
        return this;
    }
    public float angle() {
        return (float) Math.toDegrees(Math.atan2(cy(), cx()));
    }
    public float angleBetween(Pos2 other) {
        return other.cpypos().vec().sub(this).angle();
    }
    public float cross(Pos2 other) {
        return cx() * other.cy() - cy() * other.cx();
    }
    public Vec2 rotationFix() {
        if(cx() > 180) cx(cx()-360);
        if(cy() > 180) this.cy(cy()-360);
        return this;
    }
    public float dst(Pos2 b) {
        return cpy().sub(b).len();
    }
    public float dst2(Pos2 b) {
        return cpy().sub(b).len2();
    }

    public boolean posEquals(Pos2 b) {
        return b.cx() == cx() && b.cy() == cy();
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
        float newX = target.cx() + delta.cx() * scale;
        float newY = target.cy() + delta.cy() * scale;

        set(newX, newY);
    }
    public void constrain2(Vec2 b, float targetDist, boolean less, boolean greater) {
        var delta = b.cpy().sub(this);
        float dst = delta.len();

        if(less && dst < targetDist) return;
        if(greater && dst > targetDist) return;
        if (dst == 0) return;

        float diff = (dst - targetDist) / dst / 2f;

        float offsetX = delta.cx() * diff;
        float offsetY = delta.cy() * diff;

        add(offsetX, offsetY);
        b.sub(offsetX, offsetY);
    }

}
