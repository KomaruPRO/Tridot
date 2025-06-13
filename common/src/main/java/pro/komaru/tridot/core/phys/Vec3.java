package pro.komaru.tridot.core.phys;

import net.minecraft.nbt.CompoundTag;
import pro.komaru.tridot.core.comp.Pos3;
import pro.komaru.tridot.core.math.Interp;

public class Vec3 implements Pos3 {

    public static Vec3[] d6 = new Vec3[] {
        new Vec3(1, 0, 0),
        new Vec3(0, 1, 0),
        new Vec3(-1, 0, 0),
        new Vec3(0, -1, 0),
        new Vec3(0, 0, 1),
        new Vec3(0, 0, -1)
    };
    public static Vec3[] d26 = new Vec3[] {
        new Vec3(1, 0, 0),
        new Vec3(1, 1, 0),
        new Vec3(0, 1, 0),
        new Vec3(-1, 1, 0),
        new Vec3(-1, 0, 0),
        new Vec3(-1, -1, 0),
        new Vec3(0, -1, 0),
        new Vec3(1, -1, 0),
        new Vec3(0, 0, 1),
        new Vec3(1, 0, 1),
        new Vec3(0, 1, 1),
        new Vec3(-1, 1, 1),
        new Vec3(-1, 0, 1),
        new Vec3(-1, -1, 1),
        new Vec3(0, -1, 1),
        new Vec3(1, -1, 1),
        new Vec3(0, 0, -1),
        new Vec3(1, 0, -1),
        new Vec3(0, 1, -1),
        new Vec3(-1, 1, -1),
        new Vec3(-1, 0, -1),
        new Vec3(-1, -1, -1),
        new Vec3(0, -1, -1),
        new Vec3(1, -1, -1),
        new Vec3(1, 1, 1),
        new Vec3(-1, -1, -1)
    };

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

    @Override
    public float z() {
        return z;
    }

    @Override
    public void z(float value) {
        z = value;
    }

    public Vec3 add(Pos3 pos) {
        return add(pos.x(), pos.y(), pos.z());
    }
    public Vec3 add(float x, float y, float z) {
        set(x() + x, y() + y, z() + z);
        return this;
    }

    public Vec3 sub(Pos3 pos) {
        return sub(pos.x(), pos.y(), pos.z());
    }
    public Vec3 sub(float x, float y, float z) {
        return add(-x, -y, -z);
    }

    public Vec3 set(Pos3 other) {
        return set(other.x(),other.y(),other.z());
    }
    public Vec3 set(float x, float y, float z) {
        this.x(x);
        this.y(y);
        z(z);
        return this;
    }

    public Vec3 scale(float scl) {
        return scale(scl, scl, scl);
    }
    public Vec3 scale(Pos3 pos) {
        return scale(pos.x(), pos.y(), pos.z());
    }
    public Vec3 scale(float x, float y, float z) {
        set(x() * x, y() * y, z() * z);
        return this;
    }

    public float len2() {
        return x() * x() + y() * y() + z() * z();
    }
    public float len() {
        return (float) Math.sqrt(len2());
    }

    public Vec3 nor() {
        return scale(1 / len());
    }

    public net.minecraft.world.phys.Vec3 toMC() {
        return new net.minecraft.world.phys.Vec3(x(), y(), z());
    }
    public CompoundTag toTag() {
        var nbt = new CompoundTag();
        nbt.putFloat("x", x());
        nbt.putFloat("y", y());
        nbt.putFloat("z", z());
        return nbt;
    }
    public Vec3 fromTag(CompoundTag nbt) {
        return set(nbt.getFloat("x"), nbt.getFloat("y"), nbt.getFloat("z"));
    }

    public Vec3 lerp(Pos3 pos, float progress) {
        return lerp(pos,progress, Interp.linear);
    }
    public Vec3 lerp(Pos3 pos, float progress, Interp interp) {
        float x = interp.apply(progress, x(), pos.x());
        float y = interp.apply(progress, y(), pos.y());
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

        float x = x();
        float y = y();
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
        float yaw = (float) Math.atan2(normalized.z(), normalized.x());
        float pitch = (float) Math.asin(normalized.y());
        return set(pitch,yaw,0);
    }

    public Vec3 angleBetween(Pos3 other) {
        return cpy().sub(other).inv().angle().rotationFix();
    }
    public Vec3 inv() {
        return scale(-1f);
    }

    public float cross(Pos3 other) {
        return x() * other.y() - y() * other.x();
    }

    public Vec3 rotationFix() {
        if (x() > 180) this.x(x() - 360);
        if (y() > 180) this.y(y() - 360);
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
        return b.x() == x() && b.y() == y() && b.z() == z();
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
        float newX = target.x() + delta.x() * scale;
        float newY = target.y() + delta.y() * scale;
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

        float offsetX = delta.x() * diff;
        float offsetY = delta.y() * diff;
        float offsetZ = delta.z() * diff;

        add(offsetX, offsetY, offsetZ);
        b.sub(offsetX, offsetY, offsetZ);
    }
}
