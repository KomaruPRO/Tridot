package github.iri.tridot.common.interfaces;

import net.minecraft.world.entity.item.*;
import net.minecraft.world.level.*;

public interface ParticleItemEntity {
    void spawnParticles(Level level, ItemEntity entity);
}