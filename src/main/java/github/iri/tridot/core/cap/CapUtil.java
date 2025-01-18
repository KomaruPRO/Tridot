package github.iri.tridot.core.cap;

import github.iri.tridot.core.net.packets.SynchronizeCapabilityPacket;
import github.iri.tridot.utilities.func.*;
import github.iri.tridot.utilities.struct.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;

public class CapUtil {
    public static SynchronizeCapabilityPacket syncPacket(ServerPlayer player, CapEntry<?> entry) {
        Var<SynchronizeCapabilityPacket> var = new Var<>(null);
        getNoSync(player,(CapEntry<CapImpl>) entry, i -> var.var = new SynchronizeCapabilityPacket(i,entry.id));
        return var.var;
    }
    public static <T> void getNoSync(Player player, CapEntry<T> entry, Cons<T> impl) {
        player.getCapability(entry.instance.get()).ifPresent(impl::get);
    }
    public static <T> void get(Player player, CapEntry<T> entry, Cons<T> impl) {
        player.getCapability(entry.instance.get()).ifPresent(e -> {
            impl.get(e);
            if(e instanceof CapImpl i && player instanceof ServerPlayer p) i.sync(p);
        });
    }
    public static <T> void get(Player player, Class<T> clazz, CapEntry<T> entry, Cons<T> impl) {
        player.getCapability(entry.instance.get()).ifPresent(e -> {
            impl.get(e);
            if(e instanceof CapImpl i && player instanceof ServerPlayer p) i.sync(p);
        });
    }
}
