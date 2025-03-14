package pro.komaru.tridot.client.gfx.trail;

import org.joml.*;
import pro.komaru.tridot.util.phys.Vec3;
import pro.komaru.tridot.util.struct.data.Seq;
import pro.komaru.tridot.util.struct.func.Prov;

public class TrailPointBuilder{

    private final Seq<TrailPoint> points = Seq.with();
    public final Prov<Integer> length;

    public TrailPointBuilder(Prov<Integer> length){
        this.length = length;
    }

    public static TrailPointBuilder create(int trailLength){
        return create(() -> trailLength);
    }

    public static TrailPointBuilder create(Prov<Integer> trailLength){
        return new TrailPointBuilder(trailLength);
    }

    public Seq<TrailPoint> points(){
        return points;
    }

    public Seq<TrailPoint> points(float lerp){
        Seq<TrailPoint> lerped = Seq.with();
        int size = points.size;
        if(size <= 1) return lerped;

        for(int i = 0; i < size - 2; i++)
            lerped.add(points.get(i).lerp(points.get(i + 1), lerp));
        return lerped;
    }

    public TrailPointBuilder add(Vec3 point){
        return add(new TrailPoint(point, 0));
    }

    public TrailPointBuilder add(TrailPoint point){
        points.add(point);
        return this;
    }

    public TrailPointBuilder tick(){
        int trailLength = this.length.get();
        points.each(TrailPoint::tick);
        points.removeAll(p -> p.lifetime > trailLength);
        return this;
    }

    public Seq<Vector4f> build(){
        return points.map(TrailPoint::getMatrixPosition);
    }
}
