package github.iri.tridot.common.item;
import github.iri.tridot.common.interfaces.*;
import github.iri.tridot.common.network.*;
import github.iri.tridot.common.network.packets.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

public class CooldownHandler{
    public static void onCooldownEnd(ServerPlayer player, Item item) {
        if (item instanceof CooldownNotifyItem notifyItem) {
            PacketHandler.sendTo(player, new CooldownSoundPacket(notifyItem.getSoundEvent(), player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ()));
        }
    }
}