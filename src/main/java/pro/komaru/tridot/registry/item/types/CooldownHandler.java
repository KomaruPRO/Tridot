package pro.komaru.tridot.registry.item.types;

import pro.komaru.tridot.core.interfaces.*;
import pro.komaru.tridot.core.net.*;
import pro.komaru.tridot.core.net.packets.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import pro.komaru.tridot.core.interfaces.CooldownNotifyItem;
import pro.komaru.tridot.core.net.PacketHandler;
import pro.komaru.tridot.core.net.packets.CooldownSoundPacket;

public class CooldownHandler{
    public static void onCooldownEnd(ServerPlayer player, Item item){
        if(item instanceof CooldownNotifyItem notifyItem){
            PacketHandler.sendTo(player, new CooldownSoundPacket(notifyItem.getSoundEvent(), player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ()));
        }
    }
}