package github.iri.tridot.core.cap;

import com.mojang.logging.*;
import github.iri.tridot.utilities.func.*;
import github.iri.tridot.utilities.struct.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;

@Mod.EventBusSubscriber()
public class CapManager {

    public static Seq<CapEntry<?>> caps = Seq.with();

    private static Var<String> tempMod = new Var<>("");
    public static void begin(String modId) {
        tempMod.var = modId;
    }
    public static <T> CapEntry<T> reg(String modId, String id, Prov<CapProvider> provider, Prov<Capability<T>> instance) {
        CapEntry<T> entry = new CapEntry<>();
        entry.capId = id;
        entry.modId = modId;
        entry.prov = provider;
        entry.instance = instance;
        entry.id = caps.size;
        if(caps.contains(entry))
            LogUtils.getLogger().warn("Existing capability register: {}:{}", tempMod, id);
        caps.addUnique(entry);
        return entry;
    }
    public static void close(String modId) {
        if(modId.equals(tempMod.var)) tempMod.var = null;
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player)
            for (CapEntry<?> cap : caps)
                event.addCapability(new ResourceLocation(cap.modId,cap.capId), cap.prov.get());
    }


    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        for (CapEntry<?> cap : caps) {
            Capability<?> CAP = cap.instance.get();
            event.getOriginal().reviveCaps();;
            event.getEntity().getCapability(CAP).ifPresent(k -> {
                event.getOriginal().getCapability(CAP).ifPresent(o -> {
                    INBTSerializable<CompoundTag> kSer = (INBTSerializable<CompoundTag>) k;
                    INBTSerializable<CompoundTag> oSer = (INBTSerializable<CompoundTag>) o;
                    kSer.deserializeNBT(oSer.serializeNBT());
                });
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        sync(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        sync(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        sync(event.getEntity());
    }

    public static void sync(Player player) {
        if(player instanceof ServerPlayer s)
            for (CapEntry<?> cap : caps)
                CapUtil.get(player,(CapEntry<CapImpl>) cap, i -> i.sync(s));
    }
}
