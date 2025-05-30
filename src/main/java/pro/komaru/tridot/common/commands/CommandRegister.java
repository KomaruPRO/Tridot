package pro.komaru.tridot.common.commands;

import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import pro.komaru.tridot.*;

@Mod.EventBusSubscriber(modid = Tridot.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandRegister{

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent e){
        ModCommand.register(e.getDispatcher());
    }
}
