package pro.komaru.tridot.util.phys;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import pro.komaru.tridot.util.comps.phys.Rectc;

public class AbsRect implements Rectc {

    public float x,y,x2,y2;

    public static AbsRect ZERO = new AbsRect();

    public AbsRect() {

    }
    public AbsRect(float x, float y, float x2, float y2) {
        set(x,y,x2,y2);
    }

    public AbsRect set(float x, float y, float x2, float y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        return this;
    }

    @Override
    public float cx() {
        return (x+x2)/2f;
    }

    @Override
    public AbsRect cx(float value) {
        final float w = w();
        x = value-w/2f;
        x2 = value+w/2f;
        return this;
    }

    @Override
    public float cy() {
        return (y+y2)/2f;
    }

    @Override
    public AbsRect cy(float value) {
        final float h = h();
        y = value-h/2f;
        y2 = value+h/2f;
        return this;
    }

    @Override
    public float w() {
        return Math.abs(x2-x);
    }

    @Override
    public float h() {
        return Math.abs(y2-y);
    }

    @Override
    public AbsRect w(float w) {
        final float c = cx();
        x = c-w/2f;
        x2 = c+w/2f;
        return this;
    }

    @Override
    public AbsRect h(float h) {
        final float c = cy();
        y = c-h/2f;
        y2 = c+h/2f;
        return this;
    }

    public AbsRect apply(float trx, float try_, float sclx, float scly) {
        return (AbsRect) xywh(trx+x*sclx,try_+y*scly,w()*sclx,h()*scly);
    }

    public AbsRect mat(Matrix4f mat) {
        float scaleX = (float) Math.sqrt(mat.m00() * mat.m00() + mat.m01() * mat.m01() + mat.m02() * mat.m02());
        float scaleY = (float) Math.sqrt(mat.m10() * mat.m10() + mat.m11() * mat.m11() + mat.m12() * mat.m12());

        float translateX = mat.m30();
        float translateY = mat.m31();
        return apply(translateX,translateY,scaleX,scaleY);
    }

    public AbsRect pose(PoseStack pose) {
        return mat(pose.last().pose());
    }

    public static AbsRect xywhDef(float x, float y, float w, float h) {
        var abs = new AbsRect();
        abs.x = x;
        abs.y = y;
        abs.x2 = x+w;
        abs.y2 = y+h;
        return abs;
    }
    public static AbsRect xywhCent(float x, float y, float w, float h) {
        return (AbsRect) new AbsRect().xywh(x,y,w,h);
    }
}
