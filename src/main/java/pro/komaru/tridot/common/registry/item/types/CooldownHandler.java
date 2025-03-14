package pro.komaru.tridot.common.registry.item.types;

import pro.komaru.tridot.api.interfaces.*;
import pro.komaru.tridot.api.networking.PacketHandler;
import pro.komaru.tridot.common.networking.packets.CooldownSoundPacket;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

public class CooldownHandler{
    public static void onCooldownEnd(ServerPlayer player, Item item){
        if(item instanceof CooldownNotifyItem notifyItem){
            PacketHandler.sendTo(player, new CooldownSoundPacket(notifyItem.getSoundEvent(), player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ()));
        }
    }
}