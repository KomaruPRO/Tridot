package pro.komaru.tridot.api.capabilities;

import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import pro.komaru.tridot.common.networking.packets.SynchronizeCapabilityPacket;
import pro.komaru.tridot.util.struct.capability.CapImpl;
import pro.komaru.tridot.util.struct.data.Var;
import pro.komaru.tridot.util.struct.func.Cons;

public class CapabilityUtils {
    public static SynchronizeCapabilityPacket syncPacket(ServerPlayer player, CapabilityEntry<?> entry) {
        Var<SynchronizeCapabilityPacket> var = new Var<>(null);
        getNoSync(player,(CapabilityEntry<CapImpl>) entry, i -> var.var = new SynchronizeCapabilityPacket(i,entry.id));
        return var.var;
    }
    public static <T> void getNoSync(Player player, CapabilityEntry<T> entry, Cons<T> impl) {
        player.getCapability(entry.instance.get()).ifPresent(impl::get);
    }
    public static <T> void get(Player player, CapabilityEntry<T> entry, Cons<T> impl) {
        player.getCapability(entry.instance.get()).ifPresent(e -> {
            impl.get(e);
            if(e instanceof CapImpl i && player instanceof ServerPlayer p) i.sync(p);
        });
    }
    public static <T> void get(Player player, Class<T> clazz, CapabilityEntry<T> entry, Cons<T> impl) {
        player.getCapability(entry.instance.get()).ifPresent(e -> {
            impl.get(e);
            if(e instanceof CapImpl i && player instanceof ServerPlayer p) i.sync(p);
        });
    }
}
