package pro.komaru.tridot.client.gfx.particle.data;

import pro.komaru.tridot.util.math.Interp;
import net.minecraft.util.*;
import pro.komaru.tridot.util.math.Mathf;

public class GenericParticleData{
    public final float startingValue, middleValue, endingValue;
    public final float rs1, rs2, rm1, rm2, re1, re2;
    public final float coefficient;
    public final Interp startToMiddleEasing, middleToEndEasing;

    public float valueMultiplier = 1;
    public float coefficientMultiplier = 1;

    protected GenericParticleData(float startingValue, float middleValue, float endingValue, float rs1, float rs2, float rm1, float rm2, float re1, float re2, float coefficient, Interp startToMiddleEasing, Interp middleToEndEasing){
        this.startingValue = startingValue;
        this.middleValue = middleValue;
        this.endingValue = endingValue;
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.rm1 = rm1;
        this.rm2 = rm2;
        this.re1 = re1;
        this.re2 = re2;
        this.coefficient = coefficient;
        this.startToMiddleEasing = startToMiddleEasing;
        this.middleToEndEasing = middleToEndEasing;
    }

    public GenericParticleData copy(){
        return new GenericParticleData(startingValue, middleValue, endingValue, rs1, rs2, rm1, rm2, re1, re2, coefficient, startToMiddleEasing, middleToEndEasing).overrideValueMultiplier(valueMultiplier).overrideCoefficientMultiplier(coefficientMultiplier);
    }

    public GenericParticleData bake(){
        return new GenericParticleData(startingValue * valueMultiplier, middleValue * valueMultiplier, endingValue * valueMultiplier, rs1 * valueMultiplier, rs2 * valueMultiplier, rm1 * valueMultiplier, rm2 * valueMultiplier, re1 * valueMultiplier, re2 * valueMultiplier, coefficient * coefficientMultiplier, startToMiddleEasing, middleToEndEasing);
    }

    public GenericParticleData multiplyCoefficient(float coefficientMultiplier){
        this.coefficientMultiplier *= coefficientMultiplier;
        return this;
    }

    public GenericParticleData multiplyValue(float valueMultiplier){
        this.valueMultiplier *= valueMultiplier;
        return this;
    }

    public GenericParticleData overrideCoefficientMultiplier(float coefficientMultiplier){
        this.coefficientMultiplier = coefficientMultiplier;
        return this;
    }

    public GenericParticleData overrideValueMultiplier(float valueMultiplier){
        this.valueMultiplier = valueMultiplier;
        return this;
    }

    public boolean isTrinary(){
        return endingValue != -1;
    }

    public float getProgress(float age, float lifetime){
        return Mathf.clamp((age * coefficient * coefficientMultiplier) / lifetime);
    }

    public float getValue(float age, float lifetime, float startingValue, float middleValue, float endingValue){
        float progress = getProgress(age, lifetime);
        float result;
        if(isTrinary()){
            if(progress >= 0.5f){
                result = Mth.lerp(middleToEndEasing.apply((progress-0.5f)/0.5f), middleValue, endingValue);
            }else{
                result = Mth.lerp(startToMiddleEasing.apply(progress/0.5f), startingValue, middleValue);
            }
        }else{
            result = Mth.lerp(startToMiddleEasing.apply(progress), startingValue, middleValue);
        }
        return result * valueMultiplier;
    }

    public float getValue(float age, float lifetime){
        return getValue(age, lifetime, startingValue, middleValue, endingValue);
    }

    public static GenericParticleDataBuilder create(){
        return new GenericParticleDataBuilder(-1, -1, -1);
    }

    public static GenericParticleDataBuilder create(float value){
        return new GenericParticleDataBuilder(value, value, -1);
    }

    public static GenericParticleDataBuilder create(float startingValue, float endingValue){
        return new GenericParticleDataBuilder(startingValue, endingValue, -1);
    }

    public static GenericParticleDataBuilder create(float startingValue, float middleValue, float endingValue){
        return new GenericParticleDataBuilder(startingValue, middleValue, endingValue);
    }

    public static GenericParticleData constrictTransparency(GenericParticleData data){
        float startingValue = Mathf.clamp(data.startingValue);
        float middleValue = Mathf.clamp(data.middleValue);
        float endingValue = data.endingValue == -1 ? -1 : Mathf.clamp(data.endingValue);
        float rs1 = data.rs1 == -1 ? -1 : Mathf.clamp(data.rs1);
        float rm1 = data.rm1 == -1 ? -1 : Mathf.clamp(data.rm1);
        float re1 = data.re1 == -1 ? -1 : Mathf.clamp(data.re1);
        float rs2 = data.rs2 == -1 ? -1 : Mathf.clamp(data.rs2);
        float rm2 = data.rm2 == -1 ? -1 : Mathf.clamp(data.rm2);
        float re2 = data.re2 == -1 ? -1 : Mathf.clamp(data.re2);
        float coefficient = data.coefficient;
        Interp startToMiddleEasing = data.startToMiddleEasing;
        Interp middleToEndEasing = data.middleToEndEasing;
        return new GenericParticleData(startingValue, middleValue, endingValue, rs1, rs2, rm1, rm2, re1, re2, coefficient, startToMiddleEasing, middleToEndEasing);
    }
}
