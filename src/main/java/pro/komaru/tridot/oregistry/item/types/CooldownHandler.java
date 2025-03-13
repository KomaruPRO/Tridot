package pro.komaru.tridot.oregistry.item.types;

import pro.komaru.tridot.ocore.interfaces.*;
import pro.komaru.tridot.ocore.net.*;
import pro.komaru.tridot.ocore.net.packets.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

public class CooldownHandler{
    public static void onCooldownEnd(ServerPlayer player, Item item){
        if(item instanceof CooldownNotifyItem notifyItem){
            PacketHandler.sendTo(player, new CooldownSoundPacket(notifyItem.getSoundEvent(), player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ()));
        }
    }
}