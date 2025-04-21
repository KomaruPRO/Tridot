package pro.komaru.tridot.util.comps.phys;


import pro.komaru.tridot.util.phys.AbsRect;
import pro.komaru.tridot.util.phys.CenteredRect;
import pro.komaru.tridot.util.phys.Vec2;

public interface Rectc extends X,Y {
    @Override
    Rectc cx(float value);
    @Override
    Rectc cy(float value);

    float w();
    float h();

    Rectc w(float w);
    Rectc h(float h);

    static Rectc init(float x, float y, float w, float h) {
        return new Rectc() {

            float xo = x,yo = y,wo = w,ho = h;

            @Override
            public Rectc cx(float value) {
                xo = value;
                return this;
            }

            @Override
            public Rectc cy(float value) {
                yo = value;
                return this;
            }

            @Override
            public float w() {
                return wo;
            }

            @Override
            public float h() {
                return ho;
            }

            @Override
            public Rectc w(float w) {
                wo = w;
                return this;
            }

            @Override
            public Rectc h(float h) {
                ho = h;
                return this;
            }

            @Override
            public float cx() {
                return xo;
            }

            @Override
            public float cy() {
                return yo;
            }
        };
    }

    default Rectc xywh(float x, float y, float w, float h) {
        cx(x); cy(y); w(w); h(h);
        return this;
    }
    default Rectc xywhTopLeft(float x, float y, float w, float h) {
        return xywh(x+w/2f,y+h/2f,w,h);
    }

    default Rectc translate(float x, float y) {
        cx(cx()+x); cy(cy()+y);
        return this;
    }

    default <XY extends X & Y> Rectc scale(XY whscl) {
        return scale(whscl,whscl);
    }
    default Rectc scale(X wscl, Y hscl) {
        return scale(wscl.cx(),hscl.cy());
    }
    default Rectc scale(float wscl, float hscl) {
        w(w()*wscl);
        h(h()*hscl);
        return this;
    }

    default boolean collides(Rectc rect) {
        Pos2 tl1 = topLeft(), tr1 = topRight(), bl1 = botLeft(), br1 = botRight();
        Pos2 tl2 = rect.topLeft(), tr2 = rect.topRight(), bl2 = rect.botLeft(), br2 = rect.botRight();

        return tr1.cx() > tl2.cx() && tl1.cx() < tr2.cx() && br1.cy() > tl2.cy() && bl1.cy() < tr2.cy();
    }

    default <XY extends X & Y> boolean inside(XY xy) {
        return inside(xy,xy);
    }
    default boolean inside(X x, Y y) {
        return inside(x.cx(),y.cy());
    }
    default boolean inside(float x, float y) {
        float otherX = x - cx();
        float otherY = y - cy();
        return -w()/2f <= otherX && otherY <= w()/2f &&
                -h()/2f <= otherY && otherY <= h()/2f;
    }

    default Vec2 topLeft() {
        return new Vec2(cx(), cy()).add(-w()/2f,-h()/2f);
    }
    default Vec2 topRight() {
        return new Vec2(cx(), cy()).add(w()/2f,-h()/2f);
    }
    default Vec2 botLeft() {
        return new Vec2(cx(), cy()).add(-w()/2f,h()/2f);
    }
    default Vec2 botRight() {
        return new Vec2(cx(), cy()).add(w()/2f,h()/2f);
    }

    default CenteredRect centered() {
        return new CenteredRect(cx(),cy(),w(),h());
    }
    default AbsRect absolute() {
        return AbsRect.xywhCent(cx(),cy(),w(),h());
    }
}
