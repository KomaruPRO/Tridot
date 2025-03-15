package pro.komaru.tridot.util.math;

import pro.komaru.tridot.util.phys.Vec2;

public class Direction2 {
    public static Vec2[] d4 = new Vec2[] {
            new Vec2(1,0),
            new Vec2(0,1),
            new Vec2(-1,0),
            new Vec2(0,-1)
    };
    public static Vec2[] d8 = new Vec2[] {
            new Vec2(0,1),
            new Vec2(1,1),
            new Vec2(1,0),
            new Vec2(1,-1),
            new Vec2(0,-1),
            new Vec2(-1,-1),
            new Vec2(-1,0),
            new Vec2(-1,1),
    };
}
