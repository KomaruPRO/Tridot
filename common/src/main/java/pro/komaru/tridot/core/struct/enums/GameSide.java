package pro.komaru.tridot.core.struct.enums;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public enum GameSide {
    CLIENT,
    SERVER,
    BOTH;

    public static GameSide of(Entity entity) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null.");
        return of(entity.level());
    }
    public static GameSide of(Level level) {
        return of(level.isClientSide);
    }
    public static GameSide of(boolean isClient) {
        return isClient ? CLIENT : SERVER;
    }

    public boolean applies(GameSide side) {
        if (side == null) throw new IllegalArgumentException("GameSide cannot be null.");
        return this == BOTH || this == side || side == BOTH;
    }

    public boolean client() {return this == CLIENT || this == BOTH;}
    public boolean server() {return this == SERVER || this == BOTH;}
    public boolean both() {return this == BOTH;}
}
