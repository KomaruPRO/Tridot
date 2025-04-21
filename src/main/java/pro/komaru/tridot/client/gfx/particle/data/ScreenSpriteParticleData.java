package pro.komaru.tridot.client.gfx.particle.data;

import pro.komaru.tridot.client.gfx.particle.screen.*;

public class ScreenSpriteParticleData{
    public static final ScreenSpriteParticleData RANDOM = new Random();
    public static final ScreenSpriteParticleData FIRST = new First();
    public static final ScreenSpriteParticleData LAST = new Last();
    public static final ScreenSpriteParticleData WITH_AGE = new WithAge();

    public static final ScreenSpriteParticleData CRUMBS_RANDOM = new CrumbsRandom(3, 1);
    public static final ScreenSpriteParticleData CRUMBS_FIRST = new CrumbsFirst(3, 1);
    public static final ScreenSpriteParticleData CRUMBS_LAST = new CrumbsLast(3, 1);
    public static final ScreenSpriteParticleData CRUMBS_WITH_AGE = new CrumbsWithAge(3, 1);

    public static final ScreenSpriteParticleData CRUMBS_LARGE_RANDOM = new CrumbsRandom(1, 1);
    public static final ScreenSpriteParticleData CRUMBS_LARGE_FIRST = new CrumbsFirst(1, 1);
    public static final ScreenSpriteParticleData CRUMBS_LARGE_LAST = new CrumbsLast(1, 1);
    public static final ScreenSpriteParticleData CRUMBS_LARGE_WITH_AGE = new CrumbsWithAge(1, 1);

    public void init(GenericScreenParticle particle){

    }

    public void tick(GenericScreenParticle particle){

    }

    public void renderTick(GenericScreenParticle particle, float partialTicks){

    }

    public float getU0(GenericScreenParticle particle){
        return particle.sprite.getU0();
    }

    public float getU1(GenericScreenParticle particle){
        return particle.sprite.getU1();
    }

    public float getV0(GenericScreenParticle particle){
        return particle.sprite.getV0();
    }

    public float getV1(GenericScreenParticle particle){
        return particle.sprite.getV1();
    }

    public static class Random extends ScreenSpriteParticleData{

        @Override
        public void init(GenericScreenParticle particle){
            if(particle.spriteSet != null) particle.pickSprite(particle.spriteSet);
        }
    }

    public static class First extends ScreenSpriteParticleData{

        @Override
        public void init(GenericScreenParticle particle){
            if(particle.spriteSet != null) particle.pickSprite(0);
        }
    }

    public static class Last extends ScreenSpriteParticleData{

        @Override
        public void init(GenericScreenParticle particle){
            if(particle.spriteSet != null) particle.pickSprite(particle.spriteSet.sprites.size() - 1);
        }
    }

    public static class Value extends ScreenSpriteParticleData{

        public int value;

        public Value(int value){
            this.value = value;
        }

        @Override
        public void init(GenericScreenParticle particle){
            if(particle.spriteSet != null){
                if(value >= 0 && value < particle.spriteSet.sprites.size()){
                    particle.pickSprite(value);
                }
            }
        }
    }

    public static class WithAge extends ScreenSpriteParticleData{

        @Override
        public void init(GenericScreenParticle particle){
            if(particle.spriteSet != null) particle.pickSprite(0);
        }

        @Override
        public void tick(GenericScreenParticle particle){
            if(particle.spriteSet != null) particle.pickSprite(particle.age * particle.spriteSet.sprites.size() / particle.lifetime);
        }
    }

    public static class Crumbs extends ScreenSpriteParticleData{

        public float offset;
        public float size;

        public Crumbs(float offset, float size){
            this.offset = offset;
            this.size = size;
        }


        @Override
        public float getU0(GenericScreenParticle particle){
            return particle.sprite.getU0() + ((particle.sprite.getU1() - particle.sprite.getU0()) * ((particle.uo * offset + size) / (offset + size)));
        }

        @Override
        public float getU1(GenericScreenParticle particle){
            return particle.sprite.getU0() + ((particle.sprite.getU1() - particle.sprite.getU0()) * ((particle.uo * offset) / (offset + size)));
        }

        @Override
        public float getV0(GenericScreenParticle particle){
            return particle.sprite.getV0() + ((particle.sprite.getV1() - particle.sprite.getV0()) * ((particle.vo * offset) / (offset + size)));
        }

        @Override
        public float getV1(GenericScreenParticle particle){
            return particle.sprite.getV0() + ((particle.sprite.getV1() - particle.sprite.getV0()) * ((particle.vo * offset + size) / (offset + size)));
        }
    }

    public static class CrumbsRandom extends Crumbs{

        public CrumbsRandom(float offset, float size){
            super(offset, size);
        }

        @Override
        public void init(GenericScreenParticle particle){
            if(particle.spriteSet != null) particle.pickSprite(particle.spriteSet);
        }
    }

    public static class CrumbsFirst extends Crumbs{

        public CrumbsFirst(float offset, float size){
            super(offset, size);
        }

        @Override
        public void init(GenericScreenParticle particle){
            if(particle.spriteSet != null) particle.pickSprite(0);
        }
    }

    public static class CrumbsLast extends Crumbs{

        public CrumbsLast(float offset, float size){
            super(offset, size);
        }

        @Override
        public void init(GenericScreenParticle particle){
            if(particle.spriteSet != null) particle.pickSprite(particle.spriteSet.sprites.size() - 1);
        }
    }

    public static class CrumbsValue extends Crumbs{

        public int value;

        public CrumbsValue(int value, float offset, float size){
            super(offset, size);
            this.value = value;
        }

        @Override
        public void init(GenericScreenParticle particle){
            if(particle.spriteSet != null){
                if(value >= 0 && value < particle.spriteSet.sprites.size()){
                    particle.pickSprite(value);
                }
            }
        }
    }

    public static class CrumbsWithAge extends Crumbs{

        public CrumbsWithAge(float offset, float size){
            super(offset, size);
        }

        @Override
        public void init(GenericScreenParticle particle){
            if(particle.spriteSet != null) particle.pickSprite(0);
        }

        @Override
        public void tick(GenericScreenParticle particle){
            if(particle.spriteSet != null) particle.pickSprite(particle.age * particle.spriteSet.sprites.size() / particle.lifetime);
        }
    }
}
