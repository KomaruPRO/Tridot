package pro.komaru.tridot.client.gfx.particle.type;

import net.minecraft.client.multiplayer.*;
import pro.komaru.tridot.client.gfx.particle.*;
import pro.komaru.tridot.client.gfx.particle.options.*;

public class ScreenParticleType<T extends ScreenParticleOptions> {
    public ParticleProvider<T> provider;

    public interface ParticleProvider<T extends ScreenParticleOptions> {
        ScreenParticle createParticle(ClientLevel pLevel, T options, double x, double y, double pXSpeed, double pYSpeed);
    }
}