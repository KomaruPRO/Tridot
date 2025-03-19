package pro.komaru.tridot.util.phys;

import net.minecraft.nbt.*;
import net.minecraft.world.entity.*;
import pro.komaru.tridot.util.math.Interp;
import pro.komaru.tridot.util.math.Mathf;

import java.io.*;

public class Vec3 implements Serializable {
    public float x, y, z;

    /**
     * Constructs a vector with specified x, y, and z coordinates.
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructs a vector with specified x, y, and z coordinates (using double inputs).
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public Vec3(double x, double y, double z) {
        this((float) x, (float) y, (float) z);
    }

    // Static methods

    /**
     * Creates a vector with all coordinates set to zero.
     * @return a zero vector
     */
    public static Vec3 zero() {
        return new Vec3(0, 0, 0);
    }

    /**
     * Creates a vector from a CompoundTag.
     * @param tag CompoundTag containing "x", "y", and "z" values
     * @return a vector created from the tag
     */
    public static Vec3 from(CompoundTag tag) {
        return new Vec3(tag.getFloat("x"), tag.getFloat("y"), tag.getFloat("z"));
    }

    /**
     * Creates a vector from an Entity's position.
     * @param entity Entity to extract position from
     * @return a vector representing the entity's position
     */
    public static Vec3 from(Entity entity) {
        return new Vec3((float) entity.getX(), (float) entity.getY(), (float) entity.getZ());
    }

    /**
     * Creates a vector from a Minecraft Vec3
     * @param position Minecraft Vec3 object
     * @return converted vector
     */
    public static Vec3 from(net.minecraft.world.phys.Vec3 position) {
        return new Vec3(position.x,position.y,position.z);
    }

    // Getters and Setters

    /** @return the X coordinate */
    public float x() {
        return x;
    }

    /**
     * Sets the X coordinate.
     * @param x New X coordinate
     * @return this vector for chaining
     */
    public Vec3 x(float x) {
        this.x = x;
        return this;
    }

    /** @return the Y coordinate */
    public float y() {
        return y;
    }

    /**
     * Sets the Y coordinate.
     * @param y New Y coordinate
     * @return this vector for chaining
     */
    public Vec3 y(float y) {
        this.y = y;
        return this;
    }

    /** @return the Z coordinate */
    public float z() {
        return z;
    }

    /**
     * Sets the Z coordinate.
     * @param z New Z coordinate
     * @return this vector for chaining
     */
    public Vec3 z(float z) {
        this.z = z;
        return this;
    }

    /**
     * Sets the X, Y, and Z coordinates.
     * @param x New X coordinate
     * @param y New Y coordinate
     * @param z New Z coordinate
     * @return this vector for chaining
     */
    public Vec3 set(float x, float y, float z) {
        this.x(x);
        this.y(y);
        this.z(z);
        return this;
    }

    // Utility methods

    /**
     * Creates a copy of this vector.
     * @return a new vector with the same coordinates
     */
    public Vec3 cpy() {
        return new Vec3(x, y, z);
    }

    /**
     * Normalizes this vector.
     * @return this vector after normalization
     */
    public Vec3 nor() {
        return scale(1 / len());
    }

    /**
     * Calculates the length (magnitude) of this vector.
     * @return the vector's length
     */
    public float len() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Saves this vector to a CompoundTag.
     * @return a CompoundTag containing "x", "y", and "z" values
     */
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("x", x);
        tag.putFloat("y", y);
        tag.putFloat("z", z);
        return tag;
    }

    /**
     * Converts this vector to a Minecraft Vec3.
     * @return a Minecraft Vec3 instance
     */
    public net.minecraft.world.phys.Vec3 mcVec() {
        return new net.minecraft.world.phys.Vec3(x, y, z);
    }

    // Transformation methods

    /**
     * Scales this vector by the given factors.
     * @param sx Scale factor for X
     * @param sy Scale factor for Y
     * @param sz Scale factor for Z
     * @return this vector after scaling
     */
    public Vec3 scale(float sx, float sy, float sz) {
        return set(x * sx, y * sy, z * sz);
    }

    /**
     * Scales this vector uniformly by the given factor.
     * @param s Scale factor
     * @return this vector after scaling
     */
    public Vec3 scale(float s) {
        return scale(s, s, s);
    }

    /**
     * Uses a distance constraint to correct the position of this vector
     * @param to vector linked to
     * @param constraint max/min distance
     * @return Constrained pos
     */
    public Vec3 constraintMin(Vec3 to, float constraint) {
        return constraintMin(to,constraint,1f);
    }
    /**
     * Uses a distance constraint to correct the position of this vector
     * @param to vector linked to
     * @param constraint max/min distance
     * @param speed Step (0.0-1.0)
     * @return Constrained pos
     */
    public Vec3 constraintMin(Vec3 to, float constraint,float speed) {
        return constraint(to,constraint,true,false,speed);
    }
    /**
     * Uses a distance constraint to correct the position of this vector
     * @param to vector linked to
     * @param constraint max/min distance
     * @return Constrained pos
     */
    public Vec3 constraintMax(Vec3 to, float constraint) {
        return constraintMax(to,constraint,1f);
    }
    /**
     * Uses a distance constraint to correct the position of this vector
     * @param to vector linked to
     * @param constraint max/min distance
     * @param speed Step (0.0-1.0)
     * @return Constrained pos
     */
    public Vec3 constraintMax(Vec3 to, float constraint,float speed) {
        return constraint(to,constraint,false,true,speed);
    }

    /**
     * Uses a distance constraint to correct the position of this vector
     * @param to vector linked to
     * @param constraint max/min distance
     * @param min  Clamp pos if distance is smaller
     * @param max Clamp pos if distance is bigger
     * @param speed Step (0.0-1.0)
     * @return Constrained pos
     */
    public Vec3 constraint(Vec3 to, float constraint,boolean min,boolean max,float speed) {
        Vec3 vec = to.cpy().sub(this);
        float len = vec.len();
        if((max && len > constraint) || (min && len < constraint)) {
            float step = (len-constraint)*speed;
            Vec3 norVec = vec.cpy().nor().scale(step);
            add(norVec);
        }
        return this;
    }

    /**
     * Rotates this vector around the Z-axis by the given angle.
     * @param angle Rotation angle in radians
     * @return this vector after rotation
     */
    public Vec3 rotate(float angle) {
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);

        float xNew = (float) (x * cosTheta - y * sinTheta);
        float yNew = (float) (x * sinTheta + y * cosTheta);

        return set(xNew, yNew, z);
    }

    /**
     * Rotates this vector around the X, Y, and Z axes by the given angles.
     * @param x Rotation angle around X axis in radians
     * @param y Rotation angle around Y axis in radians
     * @param z Rotation angle around Z axis in radians
     * @return this vector after rotation
     */
    public Vec3 rotate(float x, float y, float z) {
        double cosX = Math.cos(x);
        double sinX = Math.sin(x);
        float yNew = (float) (this.y * cosX - this.z * sinX);
        float zNew = (float) (this.y * sinX + this.z * cosX);
        this.y = yNew;
        this.z = zNew;

        // Rotate around Y axis
        double cosY = Math.cos(y);
        double sinY = Math.sin(y);
        float xNew = (float) (this.x * cosY + this.z * sinY);
        zNew = (float) (-this.x * sinY + this.z * cosY);
        this.x = xNew;
        this.z = zNew;

        // Rotate around Z axis
        double cosZ = Math.cos(z);
        double sinZ = Math.sin(z);
        xNew = (float) (this.x * cosZ - this.y * sinZ);
        yNew = (float) (this.x * sinZ + this.y * cosZ);
        this.x = xNew;
        this.y = yNew;

        return this;
    }

    /**
     * Rotates this vector around the X, Y, and Z axes by the given angles.
     * @param vec Rotation angles around X, Y, and Z axes in radians
     * @return this vector after rotation
     */
    public Vec3 rotate(Vec3 vec) {
        return rotate(vec.x, vec.y, vec.z);
    }

    /**
     * Rotates this vector around the X, Y, and Z axes by the given angles.
     * @param x Rotation angle around X axis in degrees
     * @param y Rotation angle around Y axis in degrees
     * @param z Rotation angle around Z axis in degrees
     * @return this vector after rotation
     */
    public Vec3 rotateDeg(float x, float y, float z) {
        return rotate(x * Mathf.degRad, y * Mathf.degRad, z * Mathf.degRad);
    }

    /**
     * Rotates this vector around the X, Y, and Z axes by the given angles.
     * @param vec Rotation angles around X, Y, and Z axes in degrees
     * @return this vector after rotation
     */
    public Vec3 rotateDeg(Vec3 vec) {
        return rotateDeg(vec.x, vec.y, vec.z);
    }


    /**
     * Adds the specified values to this vector.
     * @param dx Value to add to X
     * @param dy Value to add to Y
     * @param dz Value to add to Z
     * @return this vector after addition
     */
    public Vec3 add(float dx, float dy, float dz) {
        return set(x + dx, y + dy, z + dz);
    }

    /**
     * Adds the specified values to this vector.
     * @param dx Value to add to X
     * @param dy Value to add to Y
     * @param dz Value to add to Z
     * @return this vector after addition
     */
    public Vec3 add(double dx, double dy, double dz) {
        return set(x + (float) dx, y + (float) dy, z + (float) dz);
    }

    /**
     * Adds another vector to this vector.
     * @param vec Vector to add
     * @return this vector after addition
     */
    public Vec3 add(Vec3 vec) {
        return add(vec.x, vec.y, vec.z);
    }

    /**
     * Subtracts the specified values from this vector.
     * @param dx Value to subtract from X
     * @param dy Value to subtract from Y
     * @param dz Value to subtract from Z
     * @return this vector after subtraction
     */
    public Vec3 sub(float dx, float dy, float dz) {
        return add(-dx, -dy, -dz);
    }

    /**
     * Subtracts another vector from this vector.
     * @param vec Vector to subtract
     * @return this vector after subtraction
     */
    public Vec3 sub(Vec3 vec) {
        return sub(vec.x, vec.y, vec.z);
    }

    public Vec3 cross(Vec3 pVec) {
        return new Vec3(this.y * pVec.z - this.z * pVec.y, this.z * pVec.x - this.x * pVec.z, this.x * pVec.y - this.y * pVec.x);
    }

    /**
     * Linearly interpolates between this vector and another vector.
     * @param target Target vector
     * @param progress Interpolation progress (0 to 1)
     * @return this vector after interpolation
     */
    public Vec3 lerp(Vec3 target, float progress) {
        return set(
                Mathf.lerp(x, target.x, progress),
                Mathf.lerp(y, target.y, progress),
                Mathf.lerp(z, target.z, progress)
        );
    }

    /**
     * Interpolates between this vector and another vector with a custom interpolation function.
     * @param target Target vector
     * @param progress Interpolation progress (0 to 1)
     * @param interp Interpolation function
     * @return this vector after interpolation
     */
    public Vec3 lerp(Vec3 target, float progress, Interp interp) {
        return set(
                interp.apply(progress, x, target.x),
                interp.apply(progress, y, target.y),
                interp.apply(progress, z, target.z)
        );
    }

    /**
     * Calculates angles between this vector and another.
     * @param orig Origin vector
     * @param target Target vector
     * @return a vector representing the angles (pitch, yaw, 0)
     */
    public Vec3 anglesBetween(Vec3 orig, Vec3 target) {
        return target.cpy().sub(orig).angles();
    }

    /**
     * Calculates the pitch and yaw angles of this vector.
     * @return a vector containing pitch (X) and yaw (Y)
     */
    public Vec3 angles() {
        Vec3 normalized = cpy().nor();
        double yaw = Math.atan2(normalized.z, normalized.x);
        double pitch = Math.asin(normalized.y);
        return new Vec3((float) pitch, (float) yaw, 0);
    }

    /**
     * Ensures angles are within the range of -180 to 180 degrees.
     * @return this vector after fixing angles
     */
    public Vec3 fixAngles() {
        if (x > 180) x -= 360;
        if (y > 180) y -= 360;
        if (z > 180) z -= 360;
        return this;
    }
}
