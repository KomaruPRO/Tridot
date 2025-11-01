package pro.komaru.tridot.api.level.event;

import net.minecraft.resources.*;
import pro.komaru.tridot.util.struct.data.*;

import javax.annotation.*;

public class GameplayEvents {
    private static final Seq<GameplayEvent> events = Seq.with();

    public static Seq<GameplayEvent> getEvents(){
        return events;
    }

    @Nullable
    public static GameplayEvent get(ResourceLocation eventId){
        for(GameplayEvent event : getEvents()){
            if(event.getId().equals(eventId)) return event;
        }

        return null;
    }

    public static void add(GameplayEvent e) {
        events.add(e);
    }

    public static void remove(GameplayEvent e) {
        events.remove(e);
    }
}