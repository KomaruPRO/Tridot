package pro.komaru.tridot.util.phys;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;

public class Rect {
    public float x;
    public float y;
    public float x2;
    public float y2;

    public static Rect ZERO = new Rect(0f,0f,0f,0f);

    /**
     * @param x x position
     * @param y y position
     * @param x2 x2 position
     * @param y2 y2 position
     */
    public Rect(float x, float y, float x2, float y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * @return a new rectangle with the given dimensions
     */
    public static Rect xywh(float x, float y, float w, float h) {
        return new Rect(x,y,x+w,y+h);
    }

    /**
     * @return width of the rectangle
     */
    public float w() {
        return x2-x;
    }
    /**
     * @return height of the rectangle
     */
    public float h() {
        return y2-y;
    }

    /**
     * @return a new rectangle with the given transformations
     */
    public Rect apply(float trx, float try_, float sclx, float scly) {
        return Rect.xywh(trx+x*sclx,try_+y*scly,w()*sclx,h()*scly);
    }

    public Rect mat(Matrix4f mat) {
        float scaleX = (float) Math.sqrt(mat.m00() * mat.m00() + mat.m01() * mat.m01() + mat.m02() * mat.m02());
        float scaleY = (float) Math.sqrt(mat.m10() * mat.m10() + mat.m11() * mat.m11() + mat.m12() * mat.m12());

        float translateX = mat.m30();
        float translateY = mat.m31();
        return apply(translateX,translateY,scaleX,scaleY);
    }

    public Rect pose(PoseStack pose) {
        return mat(pose.last().pose());
    }
}
