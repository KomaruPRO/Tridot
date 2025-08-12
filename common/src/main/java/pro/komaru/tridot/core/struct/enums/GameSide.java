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
        return this == BOTH || this == side || side == BOTH;
    }
    public boolean appliesStrict(GameSide side) {
        return this == side || side == BOTH;
    }
    public boolean appliesStricti(GameSide side) {
        return this == side || this == BOTH;
    }

    public String toStringColored() {
        return switch (this) {
            case CLIENT -> "\u001B[31m";
            case SERVER -> "\u001B[36m";
            case BOTH -> "\u001B[33m";
        } + name() + "\u001B[0m";
    }

    public boolean client() {return this == CLIENT || this == BOTH;}
    public boolean server() {return this == SERVER || this == BOTH;}
    public boolean both() {return this == BOTH;}
}
