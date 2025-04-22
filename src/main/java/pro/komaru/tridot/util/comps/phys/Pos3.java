package pro.komaru.tridot.util.comps.phys;

import pro.komaru.tridot.util.phys.Vec3;

public interface Pos3 extends X,Y,Z {
    @Override
    Pos3 x(float value);
    @Override
    Pos3 y(float value);
    @Override
    Pos3 z(float value);

    static Pos3 init(float x, float y, float z) {
        return new Vec3(x,y,z);
    }

    default Pos3 cpypos() {
        float x = x();
        float y = y();
        float z = z();
        return init(x,y,z);
    }

    default pro.komaru.tridot.util.phys.Vec3 vec() {
        return new pro.komaru.tridot.util.phys.Vec3(this);
    }
}
