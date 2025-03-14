package pro.komaru.tridot.client;

import net.minecraft.client.*;
import net.minecraftforge.event.*;
import pro.komaru.tridot.api.Utils;
import pro.komaru.tridot.common.ServerTickHandler;

public class ClientTick {

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

    public float time() {
        return ticksInGame + partialTicks;
    }
}
