package pro.komaru.tridot.client.event;

import net.minecraft.client.*;
import net.minecraftforge.event.*;
import pro.komaru.tridot.core.event.*;
import pro.komaru.tridot.utilities.*;

public class ClientTickHandler{

    public static int ticksInGame = 0;
    public static float partialTicks = 0;

    public static float getTotal(){
        return (float)ticksInGame + partialTicks;
    }

    public static void renderTick(TickEvent.RenderTickEvent event){
        partialTicks = event.renderTickTime;
    }

    public static void clientTickEnd(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.END){
            if(!Minecraft.getInstance().isPaused()){
                ticksInGame++;
                partialTicks = 0;
            }

            if(!Minecraft.getInstance().hasSingleplayerServer()){
                ServerTickHandler.tick++;
                Utils.Schedule.handleSyncScheduledTasks(ServerTickHandler.tick);
            }
        }
    }
}
