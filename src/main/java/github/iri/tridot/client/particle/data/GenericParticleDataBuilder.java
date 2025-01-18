package github.iri.tridot.client.particle.data;

import github.iri.tridot.utilities.math.Interp;

public class GenericParticleDataBuilder{
    protected float startingValue, middleValue, endingValue;
    protected float rs1 = -1, rs2 = -1, rm1 = -1, rm2 = -1, re1 = -1, re2 = -1;
    protected float coefficient = 1f;
    protected Interp startToMiddleEasing = Interp.linear, middleToEndEasing = Interp.linear;

    protected GenericParticleDataBuilder(float startingValue, float middleValue, float endingValue){
        this.startingValue = startingValue;
        this.middleValue = middleValue;
        this.endingValue = endingValue;
    }

    public GenericParticleDataBuilder setCoefficient(float coefficient){
        this.coefficient = coefficient;
        return this;
    }

    public GenericParticleDataBuilder setEasing(Interp easing){
        this.startToMiddleEasing = easing;
        return this;
    }

    public GenericParticleDataBuilder setEasing(Interp easing, Interp middleToEndEasing){
        this.startToMiddleEasing = easing;
        this.middleToEndEasing = easing;
        return this;
    }

    public GenericParticleDataBuilder setRandomValue(float v1, float v2){
        setRandomValue(v1, v2, -1, -1);
        return this;
    }

    public GenericParticleDataBuilder setRandomValue(float rs1, float rs2, float rm1, float rm2){
        setRandomValue(rs1, rs2, rm1, rm2, -1, -1);
        return this;
    }

    public GenericParticleDataBuilder setRandomValue(float rs1, float rs2, float rm1, float rm2, float re1, float re2){
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.rm1 = rm1;
        this.rm2 = rm2;
        this.re1 = re1;
        this.re2 = re2;
        return this;
    }

    public GenericParticleData build(){
        return new GenericParticleData(startingValue, middleValue, endingValue, rs1, rs2, rm1, rm2, re1, re2, coefficient, startToMiddleEasing, middleToEndEasing);
    }
}
