package pro.komaru.tridot.client.particle.data;

import pro.komaru.tridot.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import pro.komaru.tridot.client.particle.GenericParticle;

public class LightParticleData{
    public static final LightParticleData DEFAULT = new LightParticleData();
    public static final LightParticleData GLOW = new Glow();

    public int getLight(GenericParticle particle, Level level, float partialTicks){
        BlockPos blockpos = BlockPos.containing(particle.getPos().x(), particle.getPos().y(), particle.getPos().z());
        return level.hasChunkAt(blockpos) ? LevelRenderer.getLightColor(level, blockpos) : 0;
    }

    public static class Glow extends LightParticleData{

        @Override
        public int getLight(GenericParticle particle, Level level, float partialTicks){
            return 0xF000F0;
        }
    }

    public static class Value extends LightParticleData{

        public final int value;

        public Value(int value){
            this.value = value;
        }

        @Override
        public int getLight(GenericParticle particle, Level level, float partialTicks){
            return value;
        }
    }
}
