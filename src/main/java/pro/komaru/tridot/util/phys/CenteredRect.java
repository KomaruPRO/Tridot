package pro.komaru.tridot.util.phys;

import pro.komaru.tridot.util.comps.phys.Rectc;

public class CenteredRect implements Rectc {

    public float x,y,w,h;

    public CenteredRect(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public float x() {
        return x;
    }

    @Override
    public Rectc x(float value) {
        x = value;
        return this;
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public Rectc y(float value) {
        y = value;
        return this;
    }

    @Override
    public float w() {
        return w;
    }

    @Override
    public float h() {
        return h;
    }

    @Override
    public Rectc w(float w) {
        this.w = w;
        return this;
    }

    @Override
    public Rectc h(float h) {
        this.h = h;
        return this;
    }
}
