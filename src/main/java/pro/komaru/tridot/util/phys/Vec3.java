package pro.komaru.tridot.util.phys;

import net.minecraft.nbt.CompoundTag;
import pro.komaru.tridot.util.comps.phys.Pos3;
import pro.komaru.tridot.util.comps.phys.X;
import pro.komaru.tridot.util.comps.phys.Y;
import pro.komaru.tridot.util.comps.phys.Z;
import pro.komaru.tridot.util.math.Interp;

import java.io.*;

public class Vec3 implements Pos3,Serializable {

    public float x,y,z;

    public Vec3(float x, float y, float z) {
        set(x,y,z);
    }
    public Vec3(double x, double y, double z) {
        set((float) x, (float) y, (float) z);
    }
    public Vec3(Pos3 pos) {
        set(pos);
    }
    public Vec3() {
        set(0f,0f,0f);
    }

    public static Vec3 from(net.minecraft.world.phys.Vec3 mc) {
        return new Vec3((float) mc.x, (float) mc.y, (float) mc.z);
    }

    public static Vec3 zero() {
        return new Vec3();
    }

    public Vec3 cpy() {
        return new Vec3(this);
    }

    @Override
    public float cx() {
        return x;
    }

    @Override
    public Vec3 cx(float value) {
        x = value;
        return this;
    }

    @Override
    public float cy() {
        return y;
    }

    @Override
    public Vec3 cy(float value) {
        y = value;
        return this;
    }

    @Override
    public float z() {
        return z;
    }

    @Override
    public Vec3 z(float value) {
        z = value;
        return this;
    }

    public Vec3 add(Pos3 xyz) {
        return add((X & Y & Z) xyz);
    }
    public <XYZ extends X & Y & Z> Vec3 add(XYZ xyz) {
        return add(xyz, xyz, xyz);
    }
    public Vec3 add(X x, Y y, Z z) {
        return add(x.cx(), y.cy(), z.z());
    }
    public Vec3 add(float x, float y, float z) {
        set(cx() + x, cy() + y, z() + z);
        return this;
    }

    public Vec3 sub(Pos3 xyz) {
        return sub((X & Y & Z) xyz);
    }
    public <XYZ extends X & Y & Z> Vec3 sub(XYZ xyz) {
        return sub(xyz, xyz, xyz);
    }
    public Vec3 sub(X x, Y y, Z z) {
        return sub(x.cx(), y.cy(), z.z());
    }
    public Vec3 sub(float x, float y, float z) {
        return add(-x, -y, -z);
    }

    public Vec3 set(Pos3 other) {
        return set(other.cx(),other.cy(),other.z());
    }
    public Vec3 set(float x, float y, float z) {
        cx(x);
        this.cy(y);
        z(z);
        return this;
    }

    public Vec3 scale(float scl) {
        return scale(scl, scl, scl);
    }
    public Vec3 scale(Pos3 xyz) {
        return scale((X & Y & Z) xyz);
    }
    public <XYZ extends X & Y & Z> Vec3 scale(XYZ xyz) {
        return scale(xyz,xyz,xyz);
    }
    public Vec3 scale(X x, Y y, Z z) {
        return scale(x.cx(), y.cy(), z.z());
    }
    public Vec3 scale(float x, float y, float z) {
        set(cx() * x, cy() * y, z() * z);
        return this;
    }

    public float len2() {
        return cx() * cx() + cy() * cy() + z() * z();
    }
    public float len() {
        return (float) Math.sqrt(len2());
    }

    public Vec3 nor() {
        return scale(1 / len());
    }

    public net.minecraft.world.phys.Vec3 mcVec() {
        return new net.minecraft.world.phys.Vec3(cx(), cy(), z());
    }
    public CompoundTag toNbtVec() {
        var nbt = new CompoundTag();
        nbt.putFloat("x", cx());
        nbt.putFloat("y", cy());
        nbt.putFloat("z", z());
        return nbt;
    }
    public Vec3 fromNbtVec(CompoundTag nbt) {
        return set(nbt.getFloat("x"), nbt.getFloat("y"), nbt.getFloat("z"));
    }

    public Vec3 lerp(Pos3 pos, float progress) {
        return lerp(pos,progress,Interp.linear);
    }
    public Vec3 lerp(Pos3 pos, float progress, Interp interp) {
        float x = interp.apply(progress, cx(), pos.cx());
        float y = interp.apply(progress, cy(), pos.cy());
        float z = interp.apply(progress, z(), pos.z());
        return set(x, y, z);
    }

    public Vec3 rotate(float degX, float degY, float degZ) {
        float radiansX = (float) Math.toRadians(degX);
        float radiansY = (float) Math.toRadians(degY);
        float radiansZ = (float) Math.toRadians(degZ);

        float cosX = (float) Math.cos(radiansX);
        float sinX = (float) Math.sin(radiansX);

        float cosY = (float) Math.cos(radiansY);
        float sinY = (float) Math.sin(radiansY);

        float cosZ = (float) Math.cos(radiansZ);
        float sinZ = (float) Math.sin(radiansZ);

        float x = cx();
        float y = cy();
        float z = z();

        float tempY = y * cosX - z * sinX;
        float tempZ = y * sinX + z * cosX;

        float tempX = x * cosY + tempZ * sinY;
        tempZ = -x * sinY + tempZ * cosY;

        set(tempX * cosZ - tempY * sinZ, tempX * sinZ + tempY * cosZ, tempZ);

        return this;
    }

    public Vec3 angle() {
        Pos3 normalized = cpy().nor();
        float yaw = (float) Math.atan2(normalized.z(), normalized.cx());
        float pitch = (float) Math.asin(normalized.cy());
        return set(pitch,yaw,0);
    }

    public Vec3 angleBetween(Pos3 other) {
        return other.cpypos().vec().sub(this).angle().rotationFix();
    }

    public float cross(Pos3 other) {
        return cx() * other.cy() - cy() * other.cx();
    }

    public Vec3 rotationFix() {
        if (cx() > 180) cx(cx() - 360);
        if (cy() > 180) this.cy(cy() - 360);
        if (z() > 180) z(z() - 360);
        return this;
    }

    public float dst(Pos3 b) {
        return cpy().sub(b).len();
    }

    public float dst2(Pos3 b) {
        return cpy().sub(b).len2();
    }

    public boolean posEquals(Pos3 b) {
        return b.cx() == cx() && b.cy() == cy() && b.z() == z();
    }

    public void constrain(Vec3 target, float targetDist, boolean less, boolean greater) {
        var delta = target.cpy().sub(this);
        float dst = delta.len();

        if(less && dst < targetDist) return;
        if(greater && dst > targetDist) return;
        if (dst == 0) {
            add(targetDist, 0, 0);
            return;
        }

        float scale = targetDist / dst;
        float newX = target.cx() + delta.cx() * scale;
        float newY = target.cy() + delta.cy() * scale;
        float newZ = target.z() + delta.z() * scale;

        set(newX, newY, newZ);
    }

    public void constrain2(Vec3 b, float targetDist, boolean less, boolean greater) {
        var delta = b.cpy().sub(this);
        float dst = delta.len();

        if(less && dst < targetDist) return;
        if(greater && dst > targetDist) return;
        if (dst == 0) return;

        float diff = (dst - targetDist) / dst / 2f;

        float offsetX = delta.cx() * diff;
        float offsetY = delta.cy() * diff;
        float offsetZ = delta.z() * diff;

        add(offsetX, offsetY, offsetZ);
        b.sub(offsetX, offsetY, offsetZ);
    }
}
