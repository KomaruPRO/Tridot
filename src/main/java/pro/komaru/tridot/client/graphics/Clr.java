package pro.komaru.tridot.client.graphics;

import net.minecraft.network.chat.TextColor;
import net.minecraft.world.phys.*;

import java.awt.*;

public class Clr{
    public float r, g, b, a;
    private static final float[] tmpHSV = new float[3];

    public static Color getColor(int color) {
        return new Color(getRed(color), getGreen(color), getBlue(color), getAlpha(color));
    }
    public TextColor toTextColor() {
        return TextColor.fromRgb(toMC());
    }
    public int toMC() {
        return Clr.intArgb(this);
    }

    public static int intArgb(Clr color) {
        // Input color string in RRGGBBAA format
        String rrggbbaa = color.toString(); // Example

        // Extract components
        String rr = rrggbbaa.substring(0, 2);
        String gg = rrggbbaa.substring(2, 4);
        String bb = rrggbbaa.substring(4, 6);
        String aa = rrggbbaa.substring(6, 8);

        // Rearrange to AARRGGBB
        String aarrggbb = aa + rr + gg + bb;

        // Convert to integer
        int colorInt = (int) Long.parseLong(aarrggbb, 16);
        return colorInt;
    }
    public static int intArgb(String str) {
        return intArgb(fromHex(str));
    }

    public static float relativeLuminance(int colorCode) {
        Clr color = new Clr(colorCode);
        float r = color.r <= 0.03928
                ? color.r/12.92f
                : (float) Math.pow((color.r + 0.055) / 1.055d, 2.4d);
        float g = color.g <= 0.03928
                ? color.g/12.92f
                : (float) Math.pow((color.g + 0.055) / 1.055d, 2.4d);
        float b = color.b <= 0.03928
                ? color.b/12.92f
                : (float) Math.pow((color.b + 0.055) / 1.055d, 2.4d);
        return 0.2126f * r + 0.7152f * g + 0.0722f * b;
    }

    public static float chatContrast(int color) {
        return contrast(color,0x4B5668);
    }
    public static float contrast(int first, int second) {
        return (float) ((relativeLuminance(first)+0.05)/(relativeLuminance(second)+0.05));
    }

    /**
     * Constructs a new color using the given color
     * @param color the color
     */
    public Clr(Clr color){
        set(color);
    }

    /**
     * Constructor, sets the components of the color
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     */
    public Clr(float r, float g, float b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        clamp();
    }

    public Clr(int rgba8888){
        rgba8888(rgba8888);
    }

    /**
     * Multiplies this color and the given color
     * @param color the color
     * @return this color.
     */
    public Clr mul(Clr color){
        this.r *= color.r;
        this.g *= color.g;
        this.b *= color.b;
        this.a *= color.a;
        return clamp();
    }

    /**
     * Multiplies RGB components of this Color with the given value.
     * @param value the value
     * @return this color
     */
    public Clr mul(float value){
        this.r *= value;
        this.g *= value;
        this.b *= value;
        return clamp();
    }

    /**
     * Multiplies RGBA components of this Color with the given value.
     * @param value the value
     * @return this color
     */
    public Clr mula(float value){
        this.r *= value;
        this.g *= value;
        this.b *= value;
        this.a *= value;
        return clamp();
    }

    /**
     * Adds the given color to this color.
     * @param color the color
     * @return this color
     */
    public Clr add(Clr color){
        this.r += color.r;
        this.g += color.g;
        this.b += color.b;
        return clamp();
    }

    /**
     * Subtracts the given color from this color
     * @param color the color
     * @return this color
     */
    public Clr sub(Clr color){
        this.r -= color.r;
        this.g -= color.g;
        this.b -= color.b;
        return clamp();
    }

    /**
     * Clamps this Color's components to a valid range [0 - 1]
     * @return this Color for chaining
     */
    public Clr clamp(){
        if(r < 0)
            r = 0;
        else if(r > 1) r = 1;

        if(g < 0)
            g = 0;
        else if(g > 1) g = 1;

        if(b < 0)
            b = 0;
        else if(b > 1) b = 1;

        if(a < 0)
            a = 0;
        else if(a > 1) a = 1;
        return this;
    }

    public static int rgba8888(float r, float g, float b, float a){
        return ((int)(r * 255) << 24) | ((int)(g * 255) << 16) | ((int)(b * 255) << 8) | (int)(a * 255);
    }

    public static int argb8888(float a, float r, float g, float b){
        return ((int)(a * 255) << 24) | ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);
    }

    public int rgba8888(){
        return ((int)(r * 255) << 24) | ((int)(g * 255) << 16) | ((int)(b * 255) << 8) | (int)(a * 255);
    }

    public int argb8888(){
        return ((int)(a * 255) << 24) | ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);
    }

    /**
     * Sets the Color components using the specified integer value in the format RGBA8888. This is inverse to the rgba8888(r, g,
     * b, a) method.
     * @param value An integer color value in RGBA8888 format.
     */
    public Clr rgba8888(int value){
        r = ((value & 0xff000000) >>> 24) / 255f;
        g = ((value & 0x00ff0000) >>> 16) / 255f;
        b = ((value & 0x0000ff00) >>> 8) / 255f;
        a = ((value & 0x000000ff)) / 255f;
        return this;
    }

    public Clr set(Clr color){
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
        return this;
    }

    public Clr set(float r, float g, float b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return clamp();
    }

    public Clr set(float r, float g, float b){
        this.r = r;
        this.g = g;
        this.b = b;
        return clamp();
    }

    public Clr set(int rgba){
        return rgba8888(rgba);
    }

    /**
     * Linearly interpolates between this color and the target color by t which is in the range [0,1]. The result is stored in
     * this color.
     * @param target The target color
     * @param t The interpolation coefficient
     * @return This color for chaining.
     */
    public Clr lerp(final Clr target, final float t){
        this.r += t * (target.r - this.r);
        this.g += t * (target.g - this.g);
        this.b += t * (target.b - this.b);
        this.a += t * (target.a - this.a);
        return clamp();
    }

    /**
     * Linearly interpolates between this color and the target color by t which is in the range [0,1]. The result is stored in
     * this color.
     * @param r The red component of the target color
     * @param g The green component of the target color
     * @param b The blue component of the target color
     * @param a The alpha component of the target color
     * @param t The interpolation coefficient
     * @return This color for chaining.
     */
    public Clr lerp(final float r, final float g, final float b, final float a, final float t){
        this.r += t * (r - this.r);
        this.g += t * (g - this.g);
        this.b += t * (b - this.b);
        this.a += t * (a - this.a);
        return clamp();
    }

    /**
     * Sets RGB components using the specified Hue-Saturation-Value. This is a convenient method for
     * {@link #fromHsv(float, float, float)}. This is the inverse of {@link #toHsv(float[])}.
     * @param hsv The Hue, Saturation and Value components in that order.
     * @return The modified Color for chaining.
     */
    public Clr fromHsv(float[] hsv){
        return fromHsv(hsv[0], hsv[1], hsv[2]);
    }

    /**
     * Sets the RGB Color components using the specified Hue-Saturation-Value. Note that HSV components are voluntary not clamped
     * to preserve high range color and can range beyond typical values.
     * @param h The Hue in degree from 0 to 360
     * @param s The Saturation from 0 to 1
     * @param v The Value (brightness) from 0 to 1
     * @return The modified Color for chaining.
     */
    public Clr fromHsv(float h, float s, float v){
        float x = (h / 60f + 6) % 6;
        int i = (int)x;
        float f = x - i;
        float p = v * (1 - s);
        float q = v * (1 - s * f);
        float t = v * (1 - s * (1 - f));
        switch(i){
            case 0:
                r = v;
                g = t;
                b = p;
                break;
            case 1:
                r = q;
                g = v;
                b = p;
                break;
            case 2:
                r = p;
                g = v;
                b = t;
                break;
            case 3:
                r = p;
                g = q;
                b = v;
                break;
            case 4:
                r = t;
                g = p;
                b = v;
                break;
            default:
                r = v;
                g = p;
                b = q;
        }

        return clamp();
    }

    /**
     * Extract Hue-Saturation-Value. This is the inverse of {@link #fromHsv(float[])}.
     * @param hsv The HSV array to be modified.
     * @return HSV components for chaining.
     */
    public float[] toHsv(float[] hsv){
        float max = Math.max(Math.max(r, g), b);
        float min = Math.min(Math.min(r, g), b);
        float range = max - min;
        if(range == 0){
            hsv[0] = 0;
        }else if(max == r){
            hsv[0] = (60 * (g - b) / range + 360) % 360;
        }else if(max == g){
            hsv[0] = 60 * (b - r) / range + 120;
        }else{
            hsv[0] = 60 * (r - g) / range + 240;
        }

        if(max > 0){
            hsv[1] = 1 - min / max;
        }else{
            hsv[1] = 0;
        }

        hsv[2] = max;

        return hsv;
    }

    /**
     * Converts HSV to RGB
     * @param h hue 0-360
     * @param s saturation 0-100
     * @param v value 0-100
     * @param alpha 0-1
     */
    public static Clr HSVtoRGB(float h, float s, float v, float alpha){
        Clr c = HSVtoRGB(h, s, v);
        c.a = alpha;
        return c;
    }

    /**
     * Converts HSV color system to RGB
     * @param h hue 0-360
     * @param s saturation 0-100
     * @param v value 0-100
     */
    public static Clr HSVtoRGB(float h, float s, float v){
        Clr c = new Clr(1, 1, 1, 1);
        HSVtoRGB(h, s, v, c);
        return c;
    }

    /**
     * Converts HSV color system to RGB
     * @param h hue 0-360
     * @param s saturation 0-100
     * @param v value 0-100
     * @param targetColor color that result will be stored in
     */
    public static void HSVtoRGB(float h, float s, float v, Clr targetColor){
        if(h == 360) h = 359;
        float r, g, b;
        int i;
        float f, p, q, t;
        h = (float)Math.max(0.0, Math.min(360.0, h));
        s = (float)Math.max(0.0, Math.min(100.0, s));
        v = (float)Math.max(0.0, Math.min(100.0, v));
        s /= 100;
        v /= 100;
        h /= 60;
        i = (int)Math.floor(h);
        f = h - i;
        p = v * (1 - s);
        q = v * (1 - s * f);
        t = v * (1 - s * (1 - f));
        switch(i){
            case 0:
                r = v;
                g = t;
                b = p;
                break;
            case 1:
                r = q;
                g = v;
                b = p;
                break;
            case 2:
                r = p;
                g = v;
                b = t;
                break;
            case 3:
                r = p;
                g = q;
                b = v;
                break;
            case 4:
                r = t;
                g = p;
                b = v;
                break;
            default:
                r = v;
                g = p;
                b = q;
        }

        targetColor.set(r, g, b, targetColor.a);
    }

    /**
     * Converts {@link Clr} to HSV color system
     * @return 3 element int array with hue (0-360), saturation (0-100) and value (0-100)
     */
    public static int[] RGBtoHSV(Clr c){
        return RGBtoHSV(c.r, c.g, c.b);
    }

    /**
     * Converts RGB to HSV color system
     * @param r red 0-1
     * @param g green 0-1
     * @param b blue 0-1
     * @return 3 element int array with hue (0-360), saturation (0-100) and value (0-100)
     */
    public static int[] RGBtoHSV(float r, float g, float b){
        float h, s, v;
        float min, max, delta;

        min = Math.min(Math.min(r, g), b);
        max = Math.max(Math.max(r, g), b);
        v = max;

        delta = max - min;

        if(max != 0)
            s = delta / max;
        else{
            s = 0;
            h = 0;
            return new int[]{Math.round(h), Math.round(s), Math.round(v)};
        }

        if(delta == 0)
            h = 0;
        else{

            if(r == max)
                h = (g - b) / delta;
            else if(g == max)
                h = 2 + (b - r) / delta;
            else
                h = 4 + (r - g) / delta;
        }

        h *= 60;
        if(h < 0)
            h += 360;

        s *= 100;
        v *= 100;

        return new int[]{Math.round(h), Math.round(s), Math.round(v)};
    }

    /** @return a copy of this color */
    public Clr copy(){
        return new Clr(this);
    }

    public Clr hue(float amount){
        toHsv(tmpHSV);
        tmpHSV[0] = amount;
        fromHsv(tmpHSV);
        return this;
    }

    public Clr sat(float amount){
        toHsv(tmpHSV);
        tmpHSV[1] = amount;
        fromHsv(tmpHSV);
        return this;
    }

    public Clr value(float amount){
        toHsv(tmpHSV);
        tmpHSV[2] = amount;
        fromHsv(tmpHSV);
        return this;
    }

    public Clr shiftHue(float amount){
        toHsv(tmpHSV);
        tmpHSV[0] += amount;
        fromHsv(tmpHSV);
        return this;
    }

    public Clr shiftSat(float amount){
        toHsv(tmpHSV);
        tmpHSV[1] += amount;
        fromHsv(tmpHSV);
        return this;
    }

    public Clr shiftValue(float amount){
        toHsv(tmpHSV);
        tmpHSV[2] += amount;
        fromHsv(tmpHSV);
        return this;
    }

    /**
     * Constructor, sets the components of the color
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     */
    public Clr(float r, float g, float b){
        this(r, g, b, 1f);
    }

    /**
     * Returns the red component in the range 0-255 in the default sRGB
     * space.
     * @return the red component.
     * @see #rgb
     */
    public int red(){
        return (rgb() >> 16) & 0xFF;
    }

    /**
     * Returns the green component in the range 0-255 in the default sRGB
     * space.
     * @return the green component.
     * @see #rgb
     */
    public int green(){
        return (rgb() >> 8) & 0xFF;
    }

    /**
     * Returns the blue component in the range 0-255 in the default sRGB
     * space.
     * @return the blue component.
     * @see #rgb
     */
    public int blue(){
        return (rgb()) & 0xFF;
    }

    /**
     * Returns the alpha component in the range 0-255.
     * @return the alpha component.
     * @see #rgb
     */
    public int alpha(){
        return (rgb() >> 24) & 0xff;
    }

    public int rgb(){
        return rgba();
    }

    public int rgba(){
        return ((int)(a * 255) << 24) | ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);
    }

    public static int alpha(int packedColor){
        return packedColor >>> 24;
    }

    public static int red(int packedColor){
        return packedColor >> 16 & 255;
    }

    public static int green(int packedColor){
        return packedColor >> 8 & 255;
    }

    public static int blue(int packedColor){
        return packedColor & 255;
    }

    public static int pack(int alpha, int red, int green, int blue){
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    public static int pack(float red, float green, float blue){
        return ((int)(red * 255.0F) & 255) << 16 | ((int)(green * 255.0F) & 255) << 8 | (int)(blue * 255.0F) & 255;
    }

    public static int pack(float alpha, float red, float green, float blue){
        return ((int)(alpha * 255.0F) & 255) << 24 | ((int)(red * 255.0F) & 255) << 16 | ((int)(green * 255.0F) & 255) << 8 | (int)(blue * 255.0F) & 255;
    }

    public int pack(){
        return ((int)(a * 255.0F) & 255) << 24 | ((int)(r * 255.0F) & 255) << 16 | ((int)(g * 255.0F) & 255) << 8 | (int)(b * 255.0F) & 255;
    }

    public static int toDecimal(String hex){
        return Integer.parseInt(hex, 16);
    }

    public static int toDecimal(Color color){
        return Integer.parseInt(hex(color), 16);
    }

    public int toDecimal(){
        return Integer.parseInt(hex(), 16);
    }

    public Color toJavaColor(){
        return new Color(rgb());
    }

    public static String hex(Color color){
        String hex = Integer.toHexString(color.getRGB() & 0xffffff);
        if(hex.length() < 6){
            hex = "0" + hex;
        }

        return hex;
    }

    /** Returns the color encoded as hex string with the format RRGGBBAA. */
    public String toString(){
        StringBuilder value = new StringBuilder();
        toString(value);
        return value.toString();
    }

    public void toString(StringBuilder builder){
        builder.append(Integer.toHexString(((int)(255 * r) << 24) | ((int)(255 * g) << 16) | ((int)(255 * b) << 8) | ((int)(255 * a))));
        while(builder.length() < 8)
            builder.insert(0, "0");
    }


    public static Clr fromHex(String hex){
        int offset = hex.charAt(0) == '#' ? 1 : 0;
        int r = parseHex(hex, offset, offset + 2);
        int g = parseHex(hex, offset + 2, offset + 4);
        int b = parseHex(hex, offset + 4, offset + 6);
        int a = hex.length() - offset != 8 ? 255 : parseHex(hex, offset + 6, offset + 8);
        return new Clr(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static Color valueOf(String hex){
        return fromHex(hex).toJavaColor();
    }

    public static int getAlpha(int packedColor){
        return packedColor >>> 24;
    }

    public static int getRed(int packedColor){
        return packedColor >> 16 & 255;
    }

    public static int getGreen(int packedColor){
        return packedColor >> 8 & 255;
    }

    public static int getBlue(int packedColor){
        return packedColor & 255;
    }

    public static int packColor(int alpha, int red, int green, int blue){
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    public static int packColor(float red, float green, float blue){
        return ((int)(red * 255.0F) & 255) << 16 | ((int)(green * 255.0F) & 255) << 8 | (int)(blue * 255.0F) & 255;
    }

    public static int packColor(float alpha, float red, float green, float blue){
        return ((int)(alpha * 255.0F) & 255) << 24 | ((int)(red * 255.0F) & 255) << 16 | ((int)(green * 255.0F) & 255) << 8 | (int)(blue * 255.0F) & 255;
    }

    public static int packColor(Color color){
        return (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
    }

    public static Color rainbowColor(float ticks){
        int r = (int)(Math.sin((double)ticks) * 127.0 + 128.0);
        int g = (int)(Math.sin((double)ticks + 1.5707963267948966) * 127.0 + 128.0);
        int b = (int)(Math.sin((double)ticks + Math.PI) * 127.0 + 128.0);
        return new Color(r, g, b);
    }

    public static int hexToDecimal(String hex){
        return Integer.parseInt(hex, 16);
    }

    public static int colorToDecimal(Color color){
        return Integer.parseInt(hex(color), 16);
    }

    public static Vec3 toVec3(Color color){
        return new Vec3((double)color.getRed() / 255, (double)color.getGreen() / 255, (double)color.getBlue() / 255);
    }

    public String hex(){
        String hex = Integer.toHexString(this.rgb() & 0xffffff);
        if(hex.length() < 6){
            hex = "0" + hex;
        }

        return hex;
    }

    private static int parseHex(String string, int from, int to){
        int total = 0;
        for(int i = from; i < to; i++){
            char c = string.charAt(i);
            total += Character.digit(c, 16) * (i == from ? 16 : 1);
        }
        return total;
    }
}