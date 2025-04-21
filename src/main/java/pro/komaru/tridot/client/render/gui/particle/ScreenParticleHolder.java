package pro.komaru.tridot.client.render.gui.particle;

import pro.komaru.tridot.client.gfx.particle.*;
import pro.komaru.tridot.client.render.TridotRenderTypes.*;

import java.util.*;

public class ScreenParticleHolder {
    public final Map<ScreenParticleRenderType, ArrayList<ScreenParticle>> particles = new HashMap<>();
    public void tick() {
        particles.forEach((pair, particles) -> {
            Iterator<ScreenParticle> iterator = particles.iterator();
            while (iterator.hasNext()) {
                ScreenParticle particle = iterator.next();
                particle.tick();
                if (!particle.isAlive()) {
                    iterator.remove();
                }
            }
        });
    }

    public void addFrom(ScreenParticleHolder otherHolder) {
        particles.putAll(otherHolder.particles);
    }

    public boolean isEmpty() {
        return particles.values().stream().allMatch(ArrayList::isEmpty);
    }
}