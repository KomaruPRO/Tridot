package pro.komaru.tridot.core.entity.ecs;

import net.minecraft.world.entity.Entity;
import pro.komaru.tridot.core.struct.Seq;

import java.util.HashMap;
import java.util.Map;

/**
 * EntityCompRegistry is a registry for component initializers associated with specific entity types.
 * It allows registering component initializers for entities and retrieving them, including those from superclasses.
 */
public class EntityCompRegistry {
    private static Map<Class<? extends Entity>, PerEntityRegistry<?>> registries = new HashMap<>();

    /**
     * Registers a component initializer for a specific entity type.
     *
     * @param entityClass the class of the entity
     * @param initializer the component initializer to register
     * @param <T>         the type of the entity
     */
    public static <T extends Entity> void registerFor(Class<T> entityClass, EntityCompsInitializer initializer) {
        if (entityClass == null || initializer == null) {
            throw new IllegalArgumentException("Entity class and initializer cannot be null.");
        }
        PerEntityRegistry<T> registry;
        if(registries.containsKey(entityClass)) {
            registry = (PerEntityRegistry<T>) registries.get(entityClass);
        } else {
            registry = new PerEntityRegistry<>(entityClass);
            registries.put(entityClass, registry);
        }

        registry.add(initializer);
    }

    /**
     * Retrieves the component initializers for a specific entity type,
     * including all superclasses in the hierarchy.
     *
     * @param entityClass the class of the entity
     * @param <T>         the type of the entity
     * @return a sequence of component initializers for the specified entity type and its superclasses
     */
    @SuppressWarnings("unchecked")
    public static <T extends Entity> Seq<EntityCompsInitializer> getInitializersFor(Class<T> entityClass) {
        if (entityClass == null) return Seq.empty();

        Seq<EntityCompsInitializer> result = Seq.empty();
        Class<?> current = entityClass;

        while (current != null && Entity.class.isAssignableFrom(current)) {
            PerEntityRegistry<? extends Entity> registry = registries.get(current);
            if (registry != null) {
                result.addAll(registry.initializers);
            }
            current = current.getSuperclass();
        }

        return result;
    }

    private static class PerEntityRegistry<T extends Entity> {
        public Class<T> clazz;
        public Seq<EntityCompsInitializer> initializers;

        public PerEntityRegistry(Class<T> clazz) {
            this.clazz = clazz;
            this.initializers = Seq.empty();
        }
        public void add(EntityCompsInitializer initializer) {
            if(initializer == null) return;
            initializers = initializers.add(initializer);
        }
    }
}
