package pro.komaru.tridot.api;

import org.jetbrains.annotations.ApiStatus.*;

/**
 * Utility class for tracking the size and position of the most recently rendered tooltip.
 * <p>
 * This class is updated by mixins/hooks into Minecraft's {@code TooltipRenderUtil} methods.
 * It stores the tooltip's position (top-left corner) and its dimensions, and provides
 * accessors for both the raw size and the "real" size including the background border.
 * </p>
 */
public class TooltipTracker {
    private static int width;
    private static int height;
    private static int xPos;
    private static int yPos;

    /**
     * Sets the current tooltip position (top-left corner).
     *
     * @param x the X coordinate in screen space
     * @param y the Y coordinate in screen space
     */
    @Internal
    public static void setPos(int x, int y) {
        xPos = x; yPos = y;
    }

    /**
     * Sets the current tooltip size (excluding borders).
     *
     * @param w the tooltip content width in pixels
     * @param h the tooltip content height in pixels
     */
    @Internal
    public static void setSize(int w, int h) {
        width = w; height = h;
    }

    /**
     * @return the X coordinate of the tooltip's top-left corner
     */
    public static int getXPos() {
        return xPos;
    }

    /**
     * @return the Y coordinate of the tooltip's top-left corner
     */
    public static int getYPos() {
        return yPos;
    }

    /**
     * @return the tooltip content width in pixels (excluding borders)
     */
    public static int getWidth() {
        return width;
    }

    /**
     * @return the tooltip content height in pixels (excluding borders)
     */
    public static int getHeight() {
        return height;
    }

    /**
     * @return the total tooltip width in pixels, including 3px borders on each side
     */
    public static int getRealWidth() {
        return width + 6;
    }

    /**
     * @return the total tooltip height in pixels, including 3px borders on each side
     */
    public static int getRealHeight() {
        return height + 6;
    }
}