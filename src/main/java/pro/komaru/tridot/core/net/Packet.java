package pro.komaru.tridot.core.net;

import net.minecraft.network.*;
import net.minecraft.server.level.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.network.*;

import java.util.function.*;


public interface Packet {
    default void save(FriendlyByteBuf buf) {

    };
    default void handle(NetworkEvent.Context ctx, ServerPlayer sender) {

    };

    default void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> this::doOnClient);
            handle(ctx.get(),ctx.get().getSender());
        });
        ctx.get().setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    default void doOnClient() {

    };
}
