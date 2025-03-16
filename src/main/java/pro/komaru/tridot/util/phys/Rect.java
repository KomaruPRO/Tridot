package pro.komaru.tridot.util.phys;

public class Rect {
    public float x;
    public float y;
    public float x2;
    public float y2;

    public static Rect ZERO = new Rect(0f,0f,0f,0f);

    public Rect(float x, float y, float x2, float y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    public static Rect xywh(float x, float y, float w, float h) {
        return new Rect(x,y,x+w,y+h);
    }
}
