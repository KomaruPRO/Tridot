package pro.komaru.tridot.core.event;

import net.minecraftforge.event.*;
import net.minecraftforge.event.server.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.javafmlmod.*;
import pro.komaru.tridot.util.*;

public class ServerTickHandler{

    public static int tick;

    public static void preInit(){
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(EventPriority.NORMAL, false, TickEvent.ServerTickEvent.class, ServerTickHandler::serverTick);
        eventBus.addListener(EventPriority.NORMAL, false, ServerStartingEvent.class, ServerTickHandler::serverStarting);
    }

    private static void serverTick(final TickEvent.ServerTickEvent serverTickEvent){
        if(serverTickEvent.phase == TickEvent.Phase.END){
            tick++;
            Utils.Schedule.handleSyncScheduledTasks(tick);
        }
    }

    private static void serverStarting(final ServerStartingEvent serverStartingEvent){
        Utils.Schedule.serverStartupTasks();
    }
}
