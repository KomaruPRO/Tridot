package pro.komaru.tridot.core.cap;

import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import pro.komaru.tridot.core.net.packets.*;
import pro.komaru.tridot.core.struct.capability.CapImpl;
import pro.komaru.tridot.core.struct.data.Var;
import pro.komaru.tridot.core.struct.func.Cons;

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
