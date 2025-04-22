package pro.komaru.tridot.util.comps.phys;

import pro.komaru.tridot.util.phys.Vec2;

public interface Pos2 extends X,Y {

    static Pos2 init(float x, float y) {
        return new Vec2(x,y);
    }

    @Override
    Pos2 x(float value);
    @Override
    Pos2 y(float value);


    default Pos2 cpypos() {
        float x = x();
        float y = y();
        return init(x,y);
    }

    default pro.komaru.tridot.util.phys.Vec2 vec() {
        return new Vec2(this);
    }

}
