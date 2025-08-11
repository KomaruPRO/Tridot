package pro.komaru.tridot.api.entity.comp;

import pro.komaru.tridot.core.ecs.EntityComp;
import pro.komaru.tridot.core.struct.enums.GameSide;

public class HelloWorldComp extends EntityComp {
    @Override
    public void onAdded() {
        System.out.println("Hello, World! This is a component on the component's " + side() + " / game's " + GameSide.of(getEntity()) + " side, from the " + getEntity().getClass().getSimpleName() + " entity.");
    }

    @Override
    public void onRemoved() {
        System.out.println("Goodbye, World! This component was removed from the " + getEntity().getClass().getSimpleName() + " entity.");
    }

    @Override public GameSide side() {return GameSide.BOTH;}
}
