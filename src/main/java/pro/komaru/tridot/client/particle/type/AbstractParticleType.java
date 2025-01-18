package pro.komaru.tridot.client.particle.type;

import com.mojang.brigadier.*;
import com.mojang.serialization.*;
import pro.komaru.tridot.client.particle.options.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import pro.komaru.tridot.client.particle.options.GenericParticleOptions;

public class AbstractParticleType<T extends GenericParticleOptions> extends ParticleType<T>{

    public AbstractParticleType(){
        super(false, new ParticleOptions.Deserializer<>(){
            @Override
            public T fromCommand(ParticleType<T> type, StringReader reader){
                return (T)new GenericParticleOptions(type);
            }

            @Override
            public T fromNetwork(ParticleType<T> type, FriendlyByteBuf buf){
                return (T)new GenericParticleOptions(type);
            }
        });
    }

    @Override
    public Codec<T> codec(){
        return genericCodec(this);
    }

    public static <K extends GenericParticleOptions> Codec<K> genericCodec(ParticleType<K> type){
        return Codec.unit(() -> (K)new GenericParticleOptions(type));
    }
}
