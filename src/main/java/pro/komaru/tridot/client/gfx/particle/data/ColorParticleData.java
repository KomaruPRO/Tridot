package pro.komaru.tridot.client.gfx.particle.data;

import pro.komaru.tridot.util.Col;
import pro.komaru.tridot.util.math.Interp;
import net.minecraft.util.*;


public class ColorParticleData{

    public final float r1, g1, b1, r2, g2, b2;
    public final float rr11, rr12, rb11, rb12, rg11, rg12, rr21, rr22, rb21, rb22, rg21, rg22;
    public final float colorCoefficient;
    public final Interp colorCurveEasing;

    public float coefficientMultiplier = 1;

    protected ColorParticleData(float r1, float g1, float b1, float r2, float g2, float b2, float rr11, float rr12, float rb11, float rb12, float rg11, float rg12, float rr21, float rr22, float rb21, float rb22, float rg21, float rg22, float colorCoefficient, Interp colorCurveEasing){
        this.r1 = r1;
        this.g1 = g1;
        this.b1 = b1;
        this.r2 = r2;
        this.g2 = g2;
        this.b2 = b2;
        this.rr11 = rr11;
        this.rr12 = rr12;
        this.rb11 = rb11;
        this.rb12 = rb12;
        this.rg11 = rg11;
        this.rg12 = rg12;
        this.rr21 = rr21;
        this.rr22 = rr22;
        this.rb21 = rb21;
        this.rb22 = rb22;
        this.rg21 = rg21;
        this.rg22 = rg22;
        this.colorCoefficient = colorCoefficient;
        this.colorCurveEasing = colorCurveEasing;
    }

    public ColorParticleData multiplyCoefficient(float coefficientMultiplier){
        this.coefficientMultiplier *= coefficientMultiplier;
        return this;
    }

    public ColorParticleData overrideCoefficientMultiplier(float coefficientMultiplier){
        this.coefficientMultiplier = coefficientMultiplier;
        return this;
    }

    public float getProgress(float age, float lifetime){
        return Mth.clamp((age * colorCoefficient * coefficientMultiplier) / lifetime, 0, 1);
    }

    public ColorParticleDataBuilder copy(){
        return create(r1, g1, b1, r2, g2, b2).setCoefficient(colorCoefficient).setEasing(colorCurveEasing);
    }

    public static ColorParticleDataBuilder create(float r1, float g1, float b1, float r2, float g2, float b2){
        return new ColorParticleDataBuilder(r1, g1, b1, r2, g2, b2);
    }

    public static ColorParticleDataBuilder create(float r, float g, float b){
        return new ColorParticleDataBuilder(r, g, b, r, g, b);
    }

    public static ColorParticleDataBuilder create(Col start, Col end){
        return create(start.r,start.g,start.b,end.r,end.g,end.b);
    }

    public static ColorParticleDataBuilder create(Col color){
        return create(color.r,color.g,color.b);
    }

    public static ColorParticleDataBuilder create(){
        return create(0, 0, 0);
    }
}
