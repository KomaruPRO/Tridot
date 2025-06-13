package pro.komaru.tridot.core.math;

import pro.komaru.tridot.core.struct.Seq;

public class Mathf {
    public static final int[] signs = {-1, 1};
    public static final int[] zeroOne = {0, 1};
    public static final boolean[] booleans = {true, false};
    public static final float PI = 3.141592653f, pi = PI, halfPi = PI/2;
    public static final float PI2 = 6.283185307f;
    public static final float E = 2.718281828f;
    public static final float radiansToDegrees = 57.29577951308232f;
    public static final float radDeg = radiansToDegrees;
    /** multiply by this to convert from degrees to radians */
    public static final float degreesToRadians = 0.017453293f;
    public static final float degRad = degreesToRadians;
    private static final int sinBits = 14; // 16KB. Adjust for accuracy.
    private static final int sinMask = ~(-1 << sinBits);
    private static final int sinCount = sinMask + 1;
    private static final float[] sinTable = new float[sinCount];
    private static final float radFull = PI * 2;
    private static final float degFull = 360;
    private static final float radToIndex = sinCount / radFull;
    private static final float degToIndex = sinCount / degFull;
    static{
        for(int i = 0; i < sinCount; i++)
            sinTable[i] = (float)Math.sin((i + 0.5f) / sinCount * radFull);
        for(int i = 0; i < 360; i += 90)
            sinTable[(int)(i * degToIndex) & sinMask] = (float)Math.sin(i * degreesToRadians);

        sinTable[0] = 0f;
        sinTable[(int)(90 * degToIndex) & sinMask] = 1f;
        sinTable[(int)(180 * degToIndex) & sinMask] = 0f;
        sinTable[(int)(270 * degToIndex) & sinMask] = -1f;
    }
    /** Distance between two 3D points*/
    public static double dst(double x, double y, double z, double x2, double y2, double z2) {
        double dx = x-x2;
        double dy = y-y2;
        double dz = z-z2;
        return Math.sqrt(dx*dx+dy*dy+dz*dz);
    }
    /**Lerps from one point to another with specified progress*/
    public static float lerp(float from, float to, float dst) {
        return (to-from)*clamp(dst)+from;
    }
    /** Lerps from one point to another with specified progress without clamping*/
    public static float lerpf(float from, float to, float dst) {
        return (to-from)*dst+from;
    }

    /** Clamps to [min,max]. */
    public static int clamp(int value, int min, int max){
        return Math.max(Math.min(value, max), min);
    }
    /** Clamps to [min,max]. */
    public static long clamp(long value, long min, long max){
        return Math.max(Math.min(value, max), min);
    }
    /** Clamps to [min,max]. */
    public static float clamp(float value, float min, float max){
        return Math.max(Math.min(value, max), min);
    }
    /** Clamps to [min,max]. */
    public static double clamp(double value, double min, double max){
        return Math.max(Math.min(value, max), min);
    }
    /** Clamps to [0, 1]. */
    public static float clamp(float value){
        return clamp(value, 0f, 1f);
    }
    /** Clamps floating point to specified points */
    public static float toFixed(float val, int points) {
        int pow = powTen(points);
        return (float) Math.round(val * pow) / pow;
    }
    /** 10^pow */
    public static int powTen(int pow) {
        return (int) Math.pow(10,pow);
    }
    /** Returns the sine in radians from a lookup table. */
    public static float sin(float radians){
        return sinTable[(int)(radians * radToIndex) & sinMask];
    }

    /** Returns the cosine in radians from a lookup table. */
    public static float cos(float radians){
        return sinTable[(int)((radians + PI / 2) * radToIndex) & sinMask];
    }
    /** Returns the sine in radians from a lookup table. */
    public static float sinDeg(float degrees){
        return sinTable[(int)(degrees * degToIndex) & sinMask];
    }

    /** Returns the cosine in radians from a lookup table. */
    public static float cosDeg(float degrees){
        return sinTable[(int)((degrees + 90) * degToIndex) & sinMask];
    }

    /** Returns: the input 0-1 value scaled to 0-1-0. */
    public static float slope(float fin){
        return 1f - Math.abs(fin - 0.5f) * 2f;
    }

    public static float catmullrom(Seq<Float> points, int cur, int next, float t) {
        int p0Index = Math.max(cur - 1, 0);
        int p1Index = Math.max(cur, 0);
        int p2Index = Math.min(next, points.size - 1);
        int p3Index = Math.min(next + 1, points.size - 1);

        float p0 = points.get(p0Index);
        float p1 = points.get(p1Index);
        float p2 = points.get(p2Index);
        float p3 = points.get(p3Index);

        float t2 = t * t;
        float t3 = t2 * t;

        return 0.5f * (
                (-t3 + 2f * t2 - t) * p0 +
                        (3f * t3 - 5f * t2 + 2f) * p1 +
                        (-3f * t3 + 4f * t2 + t) * p2 +
                        (t3 - t2) * p3
        );
    }
    public static float catmullrom(float p0, float p1, float p2, float p3, float t) {
        float t2 = t * t;
        float t3 = t2 * t;

        return 0.5f * (
                (-t3 + 2 * t2 - t) * p0 +
                        (3 * t3 - 5 * t2 + 2) * p1 +
                        (-3 * t3 + 4 * t2 + t) * p2 +
                        (t3 - t2) * p3
        );
    }
    public static float catmullromLength(float p0, float p1, float p2, float p3, int steps) {
        float length = 0f;
        float prevValue = catmullrom(p0, p1, p2, p3, 0);

        for (int i = 1; i <= steps; i++) {
            float t = (float) i / steps;
            float currentValue = catmullrom(p0, p1, p2, p3, t);
            length += Math.abs(currentValue - prevValue);
            prevValue = currentValue;
        }

        return length;
    }
    public static float catmullromLength(Seq<Float> points, int cur, int next, int steps) {
        int p0Index = Math.max(cur - 1, 0);
        int p1Index = Math.max(cur, 0);
        int p2Index = Math.min(next, points.size - 1);
        int p3Index = Math.min(next + 1, points.size - 1);

        float p0 = points.get(p0Index);
        float p1 = points.get(p1Index);
        float p2 = points.get(p2Index);
        float p3 = points.get(p3Index);

        return catmullromLength(p0,p1,p2,p3,steps);
    }
    public static float interpLength(Interp f, float a, float b, int steps) {
        float length = 0f;
        float stepSize = (b - a) / steps;

        for (int i = 0; i < steps; i++) {
            float x1 = a + i * stepSize;
            float x2 = x1 + stepSize;

            float df_dx = (f.apply(x2) - f.apply(x1)) / stepSize;

            length += (float) (Math.sqrt(1 + df_dx * df_dx) * stepSize);
        }

        return length;
    }

    /** A variant on atan that does not tolerate infinite inputs for speed reasons, and because infinite inputs
     * should never occur where this is used (only in {@link #atan2(float, float)}).
     * @param i any finite float
     * @return an output from the inverse tangent function, from PI/-2.0 to PI/2.0 inclusive */
    private static float atn(final double i){
        // We use double precision internally, because some constants need double precision.
        final double n = Math.abs(i);
        // c uses the "equally-good" formulation that permits n to be from 0 to almost infinity.
        final double c = (n - 1.0) / (n + 1.0);
        // The approximation needs 6 odd powers of c.
        final double c2 = c * c, c3 = c * c2, c5 = c3 * c2, c7 = c5 * c2, c9 = c7 * c2, c11 = c9 * c2;
        return (float)Math.copySign((Math.PI * 0.25)
                + (0.99997726 * c - 0.33262347 * c3 + 0.19354346 * c5 - 0.11643287 * c7 + 0.05265332 * c9 - 0.0117212 * c11), i);
    }
    /** Close approximation of the frequently-used trigonometric method atan2, with higher precision than libGDX's atan2
     * approximation. Average error is 1.057E-6 radians; maximum error is 1.922E-6. Takes y and x (in that unusual order) as
     * floats, and returns the angle from the origin to that point in radians. It is about 4 times faster than
     * {@link Math#atan2(double, double)} (roughly 15 ns instead of roughly 60 ns for Math, on Java 8 HotSpot). <br>
     * Credit for this goes to the 1955 research study "Approximations for Digital Computers," by RAND Corporation. This is sheet
     * 11's algorithm, which is the fourth-fastest and fourth-least precise. The algorithms on sheets 8-10 are faster, but only by
     * a very small degree, and are considerably less precise. That study provides an atan method, and that cleanly
     * translates to atan2().
     * @param y y-component of the point to find the angle towards; note the parameter order is unusual by convention
     * @param x x-component of the point to find the angle towards; note the parameter order is unusual by convention
     * @return the angle to the given point, in radians as a float; ranges from -PI to PI */
    public static float atan2(float x, final float y){
        float n = y / x;
        if(n != n){
            n = (y == x ? 1f : -1f); // if both y and x are infinite, n would be NaN
        }else if(n - n != n - n){
            x = 0f; // if n is infinite, y is infinitely larger than x.
        }

        if(x > 0){
            return atn(n);
        }else if(x < 0){
            return y >= 0 ? atn(n) + PI : atn(n) - PI;
        }else if(y > 0){
            return x + halfPi;
        }else if(y < 0){
            return x - halfPi;
        }else{
            return x + y; // returns 0 for 0,0 or NaN if either y or x is NaN
        }
    }
    /** Mod function that works properly for negative numbers. */
    public static float mod(float f, float n){
        return ((f % n) + n) % n;
    }

    /** Mod function that works properly for negative numbers. */
    public static int mod(int x, int n){
        return ((x % n) + n) % n;
    }
}
