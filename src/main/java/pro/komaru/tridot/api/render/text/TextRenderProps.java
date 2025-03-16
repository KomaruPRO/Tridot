package pro.komaru.tridot.api.render.text;

import pro.komaru.tridot.util.phys.Rect;

public class TextRenderProps {
    public boolean xCentered = false;
    public boolean yCentered = false;
    public boolean shadow = true;

    public float maxWidth = -1f;
    public Rect clipRect = Rect.ZERO;

    public float alpha = 1f;
    public float scaleX = 1f, scaleY = 1f;

    public TextRenderProps centered(boolean val) {
        xCentered = val;
        return this;
    }
    public TextRenderProps centered(boolean val, boolean y) {
        xCentered = val;
        yCentered = y;
        return this;
    }
    public TextRenderProps shadow(boolean val) {
        shadow = val;
        return this;
    }

    public TextRenderProps maxWidth(float val) {
        maxWidth = val;
        return this;
    }
    public TextRenderProps clipRect(Rect val) {
        clipRect = val;
        return this;
    }

    public TextRenderProps alpha(float val) {
        alpha = val;
        return this;
    }
    public TextRenderProps sclX(float val) {
        scaleX = val;
        return this;
    }
    public TextRenderProps sclY(float val) {
        scaleY = val;
        return this;
    }
    public TextRenderProps scl(float val) {
        sclX(val); sclY(val);
        return this;
    }
    public TextRenderProps scl(float xval, float yval) {
        sclX(xval); sclY(yval);
        return this;
    }
}
