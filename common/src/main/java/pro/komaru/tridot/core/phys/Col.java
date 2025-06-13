package pro.komaru.tridot.core.phys;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.network.chat.TextColor;

import java.awt.Color;

public class Col {
    public float r, g, b, a;
    private static final float[] tmpHsv = new float[3];

    // --- Static Color Constants ---
    public static final Col WHITE = new Col(1f, 1f, 1f, 1f);
    public static final Col LIGHT_GRAY = new Col(192, 192, 192);
    public static final Col GRAY = new Col(128, 128, 128);
    public static final Col DARK_GRAY = new Col(64, 64, 64);
    public static final Col BLACK = new Col(0, 0, 0);
    public static final Col RED = new Col(255, 0, 0);
    public static final Col PINK = new Col(255, 175, 175);
    public static final Col ORANGE = new Col(255, 200, 0);
    public static final Col YELLOW = new Col(255, 255, 0);
    public static final Col GREEN = new Col(0, 255, 0);
    public static final Col MAGENTA = new Col(255, 0, 255);
    public static final Col CYAN = new Col(0, 255, 255);
    public static final Col BLUE = new Col(0, 0, 255);

    // --- Constructors ---

    /**
     * Constructs a new color by copying an existing Col object.
     * @param color The Col object to copy.
     */
    public Col(Col color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
    }

    /**
     * Constructs a new color from an integer RGBA8888 value.
     * The format is RRGGBBAA where each component is 8 bits.
     * @param rgba8888 An integer color value in RGBA8888 format.
     */
    public Col(int rgba8888) {
        set(rgba8888);
    }

    /**
     * Constructs a new color with specified red, green, blue, and alpha components (0.0-1.0).
     * @param r The red component (0.0-1.0).
     * @param g The green component (0.0-1.0).
     * @param b The blue component (0.0-1.0).
     * @param a The alpha component (0.0-1.0).
     */
    public Col(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        clamp();
    }

    /**
     * Constructs a new color with specified red, green, and blue components (0.0-1.0), with full alpha (1.0).
     * @param r The red component (0.0-1.0).
     * @param g The green component (0.0-1.0).
     * @param b The blue component (0.0-1.0).
     */
    public Col(float r, float g, float b) {
        this(r, g, b, 1f);
    }

    /**
     * Constructs a new color with specified red, green, and blue components (0-255), with full alpha (1.0).
     * @param r The red component (0-255).
     * @param g The green component (0-255).
     * @param b The blue component (0-255).
     */
    public Col(int r, int g, int b) {
        this((float) r / 255f, (float) g / 255f, (float) b / 255f, 1f);
    }

    // --- Static Factories ---

    /**
     * Creates a Col object from an integer ARGB8888 value.
     * The format is AARRGGBB where each component is 8 bits.
     * @param aarrggbb An integer color value in ARGB format.
     * @return A new Col object with the components in RGB order.
     */
    public static Col fromARGB(int aarrggbb) {
        Col col = new Col(aarrggbb);
        return col.set(col.g,col.b,col.a,col.r); //yes its messy but it works
    }
    /**
     * Creates a Col object from a Java AWT Color object.
     * @param color The AWT Color object.
     * @return A new Col object.
     */
    public static Col fromAwt(Color color) {
        return new Col(color.getRGB()); // AWT Color.getRGB() returns ARGB
    }

    /**
     * Creates a Col object from a hexadecimal string (RRGGBB or RRGGBBAA).
     * @param hex The hexadecimal string.
     * @return A new Col object.
     */
    public static Col fromHex(String hex) {
        int offset = hex.charAt(0) == '#' ? 1 : 0;
        int r = parseHexComponent(hex, offset, offset + 2);
        int g = parseHexComponent(hex, offset + 2, offset + 4);
        int b = parseHexComponent(hex, offset + 4, offset + 6);
        int a = hex.length() - offset != 8 ? 255 : parseHexComponent(hex, offset + 6, offset + 8);
        return new Col(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    /**
     * Converts HSV color components to a new Col object.
     * @param h Hue in degrees (0-360).
     * @param s Saturation (0-100).
     * @param v Value/Brightness (0-100).
     * @param alpha Alpha component (0.0-1.0).
     * @return A new Col object representing the converted color.
     */
    public static Col fromHsv(float h, float s, float v, float alpha) {
        Col c = fromHsv(h, s, v);
        c.a = alpha;
        return c;
    }

    /**
     * Converts HSV color components to a new Col object with full alpha (1.0).
     * @param h Hue in degrees (0-360).
     * @param s Saturation (0-100).
     * @param v Value/Brightness (0-100).
     * @return A new Col object representing the converted color.
     */
    public static Col fromHsv(float h, float s, float v) {
        Col c = new Col(1, 1, 1, 1);
        hsvToRgb(h, s, v, c);
        return c;
    }

    /**
     * Generates a rainbow color based on the given ticks (hue).
     * @param ticks The value determining the hue (e.g., time ticks).
     * @return A new Col object representing a rainbow color.
     */
    public static Col rainbow(float ticks) {
        return Col.fromHsv(ticks % 360, 100f, 100f);
    }

    /**
     * Sets Minecraft RenderSystem's current shader color to this color.
     */
    public void toRenderSys() {
        RenderSystem.setShaderColor(r, g, b, a);
    }
    /**
     * Creates a Col object from the current Minecraft RenderSystem shader color.
     * @return A new Col object with the current shader color.
     */
    public static Col fromRenderSys() {
        float[] color = RenderSystem.getShaderColor();
        return new Col(color[0], color[1], color[2], color[3]);
    }

    // --- Component Accessors (0-255) ---

    /**
     * Returns the red component in the range 0-1.
     * @return The red component.
     */
    public float red() {
        return (int) (r * 255f);
    }

    /**
     * Returns the green component in the range 0-1.
     * @return The green component.
     */
    public int green() {
        return (int) (g * 255f);
    }

    /**
     * Returns the blue component in the range 0-1.
     * @return The blue component.
     */
    public int blue() {
        return (int) (b * 255f);
    }

    /**
     * Returns the alpha component in the range 0-1.
     * @return The alpha component.
     */
    public int alpha() {
        return (int) (a * 255f);
    }

    // --- Modifiers (Chainable) ---

    /**
     * Sets this color's components by copying another Col object.
     * @param color The Col object to copy from.
     * @return This Col object for chaining.
     */
    public Col set(Col color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
        return this;
    }

    /**
     * Sets this color's components with new red, green, blue, and alpha values (0.0-1.0).
     * @param r The new red component (0.0-1.0).
     * @param g The new green component (0.0-1.0).
     * @param b The new blue component (0.0-1.0).
     * @param a The new alpha component (0.0-1.0).
     * @return This Col object for chaining.
     */
    public Col set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return clamp();
    }

    /**
     * Sets this color's components with new red, green, and blue values (0.0-1.0), preserving current alpha.
     * @param r The new red component (0.0-1.0).
     * @param g The new green component (0.0-1.0).
     * @param b The new blue component (0.0-1.0).
     * @return This Col object for chaining.
     */
    public Col set(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        return clamp();
    }

    /**
     * Sets this color's components from an integer RGBA8888 value.
     * The format is RRGGBBAA where each component is 8 bits.
     * @param value An integer color value in RGBA8888 format.
     * @return This Col object for chaining.
     */
    public Col set(int value) {
        r = ((value & 0xff000000) >>> 24) / 255f;
        g = ((value & 0x00ff0000) >>> 16) / 255f;
        b = ((value & 0x0000ff00) >>> 8) / 255f;
        a = ((value & 0x000000ff)) / 255f;
        return this;
    }

    /**
     * Sets this color's alpha component (0.0-1.0), preserving RGB.
     * @param alpha The new alpha component (0.0-1.0).
     * @return A new Col object with the updated alpha.
     */
    public Col alpha(float alpha) {
        return new Col(r, g, b, alpha);
    }

    /**
     * Multiplies this color's components by the components of another color.
     * @param color The color to multiply by.
     * @return This Col object for chaining.
     */
    public Col mul(Col color) {
        this.r *= color.r;
        this.g *= color.g;
        this.b *= color.b;
        this.a *= color.a;
        return clamp();
    }

    /**
     * Multiplies this color's RGB components by a scalar value.
     * @param value The scalar value.
     * @return This Col object for chaining.
     */
    public Col mulRgb(float value) {
        this.r *= value;
        this.g *= value;
        this.b *= value;
        return clamp();
    }

    /**
     * Multiplies this color's RGBA components by a scalar value.
     * @param value The scalar value.
     * @return This Col object for chaining.
     */
    public Col mulRgba(float value) {
        this.r *= value;
        this.g *= value;
        this.b *= value;
        this.a *= value;
        return clamp();
    }

    /**
     * Adds the components of another color to this color.
     * @param color The color to add.
     * @return This Col object for chaining.
     */
    public Col add(Col color) {
        this.r += color.r;
        this.g += color.g;
        this.b += color.b;
        return clamp();
    }

    /**
     * Subtracts the components of another color from this color.
     * @param color The color to subtract.
     * @return This Col object for chaining.
     */
    public Col sub(Col color) {
        this.r -= color.r;
        this.g -= color.g;
        this.b -= color.b;
        return clamp();
    }

    /**
     * Darkens this color by a factor of 0.7.
     * @return A new Col object representing the darker color.
     */
    public Col darker() {
        return mulRgb(0.7f).clamp();
    }

    /**
     * Brightens this color by a factor of 0.7.
     * @return A new Col object representing the brighter color.
     */
    public Col brighter() {
        return mulRgb(1f/0.7f).clamp();
    }

    /**
     * Linearly interpolates between this color and a target color. The result is stored in this color.
     * @param target The target color.
     * @param t The interpolation coefficient (0.0-1.0).
     * @return This Col object for chaining.
     */
    public Col lerp(final Col target, final float t) {
        this.r += t * (target.r - this.r);
        this.g += t * (target.g - this.g);
        this.b += t * (target.b - this.b);
        this.a += t * (target.a - this.a);
        return clamp();
    }

    /**
     * Linearly interpolates between this color and target RGB A values. The result is stored in this color.
     * @param r The red component of the target color.
     * @param g The green component of the target color.
     * @param b The blue component of the target color.
     * @param a The alpha component of the target color.
     * @param t The interpolation coefficient (0.0-1.0).
     * @return This Col object for chaining.
     */
    public Col lerp(final float r, final float g, final float b, final float a, final float t) {
        this.r += t * (r - this.r);
        this.g += t * (g - this.g);
        this.b += t * (b - this.b);
        this.a += t * (a - this.a);
        return clamp();
    }

    /**
     * Clamps this Color's components to a valid range [0.0 - 1.0].
     * @return This Col object for chaining.
     */
    public Col clamp() {
        if (r < 0) r = 0;
        else if (r > 1) r = 1;

        if (g < 0) g = 0;
        else if (g > 1) g = 1;

        if (b < 0) b = 0;
        else if (b > 1) b = 1;

        if (a < 0) a = 0;
        else if (a > 1) a = 1;
        return this;
    }

    // --- HSV Conversion (Instance Methods) ---

    /**
     * Sets RGB components using the specified Hue-Saturation-Value.
     * @param hsv The Hue, Saturation and Value components in that order (e.g., [hue, saturation, value]).
     * Hue (0-360), Saturation (0-1), Value (0-1).
     * @return The modified Col object for chaining.
     */
    public Col hsv(float[] hsv) {
        return hsv(hsv[0], hsv[1], hsv[2]);
    }

    /**
     * Sets the RGB Color components using the specified Hue-Saturation-Value.
     * Note that HSV components are voluntarily not clamped to preserve high range color and can range beyond typical values.
     * @param h The Hue in degrees from 0 to 360.
     * @param s The Saturation from 0.0 to 1.0.
     * @param v The Value (brightness) from 0.0 to 1.0.
     * @return The modified Col object for chaining.
     */
    public Col hsv(float h, float s, float v) {
        float x = (h / 60f + 6) % 6;
        int i = (int) x;
        float f = x - i;
        float p = v * (1 - s);
        float q = v * (1 - s * f);
        float t = v * (1 - s * (1 - f));
        switch (i) {
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
     * Extracts Hue-Saturation-Value from this color.
     * Hue (0-360), Saturation (0-1), Value (0-1).
     * @return The HSV array for chaining.
     */
    public float[] hsv() {
        float max = Math.max(Math.max(r, g), b);
        float min = Math.min(Math.min(r, g), b);
        float range = max - min;
        float[] hsv = new float[3];

        if (range == 0) {
            hsv[0] = 0;
        } else if (max == r) {
            hsv[0] = (60 * (g - b) / range + 360) % 360;
        } else if (max == g) {
            hsv[0] = 60 * (b - r) / range + 120;
        } else {
            hsv[0] = 60 * (r - g) / range + 240;
        }

        hsv[1] = max > 0 ? (1 - min / max) : 0;
        hsv[2] = max;

        return hsv;
    }

    /**
     * Adjusts the hue of this color.
     * @param amount The new hue value (0-360).
     * @return This Col object for chaining.
     */
    public Col hue(float amount) {
        hsv(tmpHsv);
        tmpHsv[0] = amount;
        hsv(tmpHsv[0], tmpHsv[1], tmpHsv[2]);
        return this;
    }

    /**
     * Adjusts the saturation of this color.
     * @param amount The new saturation value (0-1).
     * @return This Col object for chaining.
     */
    public Col saturation(float amount) {
        hsv(tmpHsv);
        tmpHsv[1] = amount;
        hsv(tmpHsv[0], tmpHsv[1], tmpHsv[2]);
        return this;
    }

    /**
     * Adjusts the value (brightness) of this color.
     * @param amount The new value (brightness) (0-1).
     * @return This Col object for chaining.
     */
    public Col value(float amount) {
        hsv(tmpHsv);
        tmpHsv[2] = amount;
        hsv(tmpHsv[0], tmpHsv[1], tmpHsv[2]);
        return this;
    }

    /**
     * Shifts the hue of this color by a given amount.
     * @param amount The amount to shift the hue by.
     * @return This Col object for chaining.
     */
    public Col shiftHue(float amount) {
        hsv(tmpHsv);
        tmpHsv[0] += amount;
        hsv(tmpHsv[0], tmpHsv[1], tmpHsv[2]);
        return this;
    }

    /**
     * Shifts the saturation of this color by a given amount.
     * @param amount The amount to shift the saturation by.
     * @return This Col object for chaining.
     */
    public Col shiftSaturation(float amount) {
        hsv(tmpHsv);
        tmpHsv[1] += amount;
        hsv(tmpHsv[0], tmpHsv[1], tmpHsv[2]);
        return this;
    }

    /**
     * Shifts the value (brightness) of this color by a given amount.
     * @param amount The amount to shift the value by.
     * @return This Col object for chaining.
     */
    public Col shiftValue(float amount) {
        hsv(tmpHsv);
        tmpHsv[2] += amount;
        hsv(tmpHsv[0], tmpHsv[1], tmpHsv[2]);
        return this;
    }

    // --- Static HSV Conversion (Utility Methods) ---

    /**
     * Converts HSV color components to RGB and stores the result in a target Col object.
     * @param h Hue in degrees (0-360).
     * @param s Saturation (0-100).
     * @param v Value/Brightness (0-100).
     * @param targetColor The Col object where the RGB result will be stored. Alpha component is preserved.
     */
    public static void hsvToRgb(float h, float s, float v, Col targetColor) {
        if (h == 360) h = 359; // Handle 360 as 0 for continuity
        float r, g, b;
        int i;
        float f, p, q, t;
        h = Math.max(0.0f, Math.min(360.0f, h));
        s = Math.max(0.0f, Math.min(100.0f, s));
        v = Math.max(0.0f, Math.min(100.0f, v));
        s /= 100f;
        v /= 100f;
        h /= 60f;
        i = (int) Math.floor(h);
        f = h - i;
        p = v * (1 - s);
        q = v * (1 - s * f);
        t = v * (1 - s * (1 - f));
        switch (i) {
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
            default: // case 5
                r = v;
                g = p;
                b = q;
        }
        targetColor.set(r, g, b); // Use the new rgb method
    }

    /**
     * Converts Col object to HSV color system.
     * @param color The Col object to convert.
     * @return A 3-element int array with hue (0-360), saturation (0-100) and value (0-100).
     */
    public static int[] rgbToHsvInts(Col color) {
        return rgbToHsvInts(color.r, color.g, color.b);
    }

    /**
     * Converts RGB (0.0-1.0) to HSV color system.
     * @param r Red component (0.0-1.0).
     * @param g Green component (0.0-1.0).
     * @param b Blue component (0.0-1.0).
     * @return A 3-element int array with hue (0-360), saturation (0-100) and value (0-100).
     */
    public static int[] rgbToHsvInts(float r, float g, float b) {
        float h, s, v;
        float min, max, delta;

        min = Math.min(Math.min(r, g), b);
        max = Math.max(Math.max(r, g), b);
        v = max;

        delta = max - min;

        if (max != 0) {
            s = delta / max;
        } else {
            s = 0;
            h = 0;
            return new int[]{Math.round(h), Math.round(s * 100), Math.round(v * 100)};
        }

        if (delta == 0) {
            h = 0;
        } else {
            if (r == max) {
                h = (g - b) / delta;
            } else if (g == max) {
                h = 2 + (b - r) / delta;
            } else { // b == max
                h = 4 + (r - g) / delta;
            }
        }

        h *= 60;
        if (h < 0) {
            h += 360;
        }

        s *= 100;
        v *= 100;

        return new int[]{Math.round(h), Math.round(s), Math.round(v)};
    }


    // --- Color Space Conversions (Instance Methods) ---

    /**
     * Converts this color to a Minecraft TextColor.
     * @return A TextColor object.
     */
    public TextColor toTextColor() {
        return TextColor.fromRgb(rgbInt());
    }

    /**
     * Converts this color to a Java AWT Color object.
     * @return A Java AWT Color object.
     */
    public Color toAwt() {
        return new Color(r, g, b, a);
    }

    /**
     * Converts this color to an integer ARGB (Alpha-Red-Green-Blue) value.
     * @return An integer representing the color in AARRGGBB format.
     */
    public int argbInt() {
        return ((int) (a * 255) << 24) | ((int) (r * 255) << 16) | ((int) (g * 255) << 8) | ((int) (b * 255));
    }

    /**
     * Converts this color to an integer RGBA (Red-Green-Blue-Alpha) value.
     * @return An integer representing the color in RRGGBBAA format.
     */
    public int rgbaInt() {
        return ((int) (r * 255) << 24) | ((int) (g * 255) << 16) | ((int) (b * 255) << 8) | ((int) (a * 255));
    }

    /**
     * Converts this color to an integer RGB (Red-Green-Blue) value. Alpha is ignored.
     * @return An integer representing the color in RRGGBB format.
     */
    public int rgbInt() {
        return ((int) (r * 255) << 16) | ((int) (g * 255) << 8) | ((int) (b * 255));
    }

    /**
     * Returns the color encoded as a hexadecimal string with the format RRGGBBAA.
     * @return The hexadecimal string representation of the color.
     */
    @Override
    public String toString() {
        return String.format("%02x%02x%02x%02x", red(), green(), blue(), alpha());
    }

    /**
     * Returns the color encoded as a hexadecimal string with the format RRGGBB.
     * @return The hexadecimal string representation of the color.
     */
    public String hexRgb() {
        return String.format("%02x%02x%02x", red(), green(), blue());
    }

    /**
     * Converts this color to a Vec3 object where x=red, y=green, z=blue (0.0-1.0).
     * @return A Vec3 object.
     */
    public Vec3 toVec3() {
        return new Vec3(r, g, b);
    }

    // --- Static Color Utility Methods (Integer based) ---

    /**
     * Converts an integer ARGB (Alpha-Red-Green-Blue) value to an integer RGB (Red-Green-Blue) value.
     * @param argb The integer ARGB value.
     * @return The integer RGB value (AARRGGBB to RRGGBB, alpha ignored).
     */
    public static int argbToRgb(int argb) {
        return argb & 0x00FFFFFF; // Mask out the alpha component
    }

    /**
     * Extracts the alpha component from a packed integer color (AARRGGBB or RRGGBBAA).
     * @param packedColor The integer color.
     * @return The alpha component (0-255).
     */
    public static int alpha(int packedColor) {
        return (packedColor >> 24) & 0xFF; // For AARRGGBB or RRGGBBAA if it's the high byte
    }

    /**
     * Extracts the red component from a packed integer color (AARRGGBB or RRGGBBAA).
     * @param packedColor The integer color.
     * @return The red component (0-255).
     */
    public static int red(int packedColor) {
        return (packedColor >> 16) & 0xFF; // For AARRGGBB or RRGGBBAA if it's the second high byte
    }

    /**
     * Extracts the green component from a packed integer color (AARRGGBB or RRGGBBAA).
     * @param packedColor The integer color.
     * @return The green component (0-255).
     */
    public static int green(int packedColor) {
        return (packedColor >> 8) & 0xFF; // For AARRGGBB or RRGGBBAA if it's the third high byte
    }

    /**
     * Extracts the blue component from a packed integer color (AARRGGBB or RRGGBBAA).
     * @param packedColor The integer color.
     * @return The blue component (0-255).
     */
    public static int blue(int packedColor) {
        return packedColor & 0xFF; // For AARRGGBB or RRGGBBAA if it's the low byte
    }

    /**
     * Packs red, green, blue, and alpha components (0-255) into an integer ARGB8888 value.
     * @param alpha The alpha component (0-255).
     * @param red The red component (0-255).
     * @param green The green component (0-255).
     * @param blue The blue component (0-255).
     * @return An integer representing the color in AARRGGBB format.
     */
    public static int packArgb(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * Packs red, green, and blue components (0-255) into an integer RGB888 value (alpha implicitly 255).
     * @param red The red component (0-255).
     * @param green The green component (0-255).
     * @param blue The blue component (0-255).
     * @return An integer representing the color in RRGGBB format.
     */
    public static int packRgb(int red, int green, int blue) {
        return (red << 16) | (green << 8) | blue;
    }

    /**
     * Packs red, green, blue, and alpha components (0.0-1.0) into an integer RGBA8888 value.
     * @param r The red component (0.0-1.0).
     * @param g The green component (0.0-1.0).
     * @param b The blue component (0.0-1.0).
     * @param a The alpha component (0.0-1.0).
     * @return An integer representing the color in RRGGBBAA format.
     */
    public static int packRgba(float r, float g, float b, float a) {
        return ((int) (r * 255f) << 24) | ((int) (g * 255f) << 16) | ((int) (b * 255f) << 8) | (int) (a * 255f);
    }

    /**
     * Packs red, green, and blue components (0.0-1.0) into an integer RGB888 value (alpha implicitly 1.0).
     * @param r The red component (0.0-1.0).
     * @param g The green component (0.0-1.0).
     * @param b The blue component (0.0-1.0).
     * @return An integer representing the color in RRGGBB format.
     */
    public static int packRgb(float r, float g, float b) {
        return ((int) (r * 255f) & 255) << 16 | ((int) (g * 255f) & 255) << 8 | (int) (b * 255f) & 255;
    }

    /**
     * Packs alpha, red, green, and blue components (0.0-1.0) into an integer ARGB8888 value.
     * @param a The alpha component (0.0-1.0).
     * @param r The red component (0.0-1.0).
     * @param g The green component (0.0-1.0).
     * @param b The blue component (0.0-1.0).
     * @return An integer representing the color in AARRGGBB format.
     */
    public static int packArgb(float a, float r, float g, float b) {
        return ((int) (a * 255f) << 24) | ((int) (r * 255f) << 16) | ((int) (g * 255f) << 8) | (int) (b * 255f);
    }

    /**
     * Calculates the relative luminance of a color.
     * @param colorInt The integer color (e.g., RGB or ARGB, alpha is ignored for luminance calculation).
     * @return The relative luminance.
     */
    public static float relativeLuminance(int colorInt) {
        Col color = new Col(colorInt); // Temporarily create Col to get float components
        float r = color.r <= 0.03928f ? color.r / 12.92f : (float) Math.pow((color.r + 0.055) / 1.055d, 2.4d);
        float g = color.g <= 0.03928f ? color.g / 12.92f : (float) Math.pow((color.g + 0.055) / 1.055d, 2.4d);
        float b = color.b <= 0.03928f ? color.b / 12.92f : (float) Math.pow((color.b + 0.055) / 1.055d, 2.4d);
        return 0.2126f * r + 0.7152f * g + 0.0722f * b;
    }

    /**
     * Calculates the contrast ratio between two colors.
     * @param foregroundColor The foreground color (integer).
     * @param backgroundColor The background color (integer).
     * @return The contrast ratio.
     */
    public static float contrast(int foregroundColor, int backgroundColor) {
        float l1 = relativeLuminance(foregroundColor);
        float l2 = relativeLuminance(backgroundColor);
        return (float) ((Math.max(l1, l2) + 0.05) / (Math.min(l1, l2) + 0.05));
    }

    /**
     * Calculates the chat contrast for a given color against a standard chat background (0x4B5668).
     * @param color The color (integer).
     * @return The chat contrast ratio.
     */
    public static float chatContrast(int color) {
        return contrast(color, 0x4B5668);
    }

    /**
     * Converts a hexadecimal string to a decimal integer.
     * @param hex The hexadecimal string.
     * @return The decimal integer.
     */
    public static int hexToDecimal(String hex) {
        return Integer.parseInt(hex, 16);
    }

    /**
     * Converts a Java AWT Color object to a decimal integer (RRGGBB).
     * @param color The AWT Color object.
     * @return The decimal integer representation of the color.
     */
    public static int awtToDecimal(Color color) {
        return Integer.parseInt(hexRgb(color), 16);
    }

    /**
     * Converts a Java AWT Color object to a hexadecimal string (RRGGBB).
     * @param color The AWT Color object.
     * @return The hexadecimal string.
     */
    public static String hexRgb(Color color) {
        String hex = Integer.toHexString(color.getRGB() & 0xFFFFFF); // Mask out alpha, AWT getRGB is ARGB
        while (hex.length() < 6) {
            hex = "0" + hex;
        }
        return hex;
    }

    // --- Private Helper Methods ---

    /**
     * Parses a substring of a hexadecimal string into an integer component (0-255).
     * @param string The full hexadecimal string.
     * @param from The starting index (inclusive).
     * @param to The ending index (exclusive).
     * @return The parsed integer component.
     */
    private static int parseHexComponent(String string, int from, int to) {
        int total = 0;
        for (int i = from; i < to; i++) {
            char c = string.charAt(i);
            total += Character.digit(c, 16) * (i == from ? 16 : 1);
        }
        return total;
    }
}