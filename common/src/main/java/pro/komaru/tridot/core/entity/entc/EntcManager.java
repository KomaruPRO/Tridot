package pro.komaru.tridot.core.entity.entc;

import lombok.RequiredArgsConstructor;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

@RequiredArgsConstructor
public class EntcManager<E extends Entity, D extends EntcDispatcher> {
    public final String uuid = UUID.randomUUID().toString();

    private final E entity;
    private final D dispatcher;

    public void init() {

    }
}
