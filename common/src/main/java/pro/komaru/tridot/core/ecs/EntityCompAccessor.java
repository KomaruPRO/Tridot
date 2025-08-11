package pro.komaru.tridot.core.ecs;

public interface EntityCompAccessor {
    default EntityCompContainer componentContainer() {
        return (EntityCompContainer) this;
    }
}
