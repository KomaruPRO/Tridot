package pro.komaru.tridot;

import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.*;
import pro.komaru.tridot.client.ClientEvents;
import net.minecraft.resources.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.event.lifecycle.*;
import pro.komaru.tridot.client.tooltip.*;
import pro.komaru.tridot.client.sound.LoopedSoundInstance;
import pro.komaru.tridot.client.sound.TridotSoundInstance;
import pro.komaru.tridot.client.compatibility.ShadersIntegration;

import static pro.komaru.tridot.TridotLib.*;

public class TridotLibClient{
    public static LoopedSoundInstance BOSS_MUSIC;
    public static TridotSoundInstance COOLDOWN_SOUND;
    public static TridotSoundInstance DUNGEON_MUSIC_INSTANCE;
    public static final ResourceLocation VANILLA_LOC = new ResourceLocation("textures/gui/bars.png");

    public static void clientSetup(final FMLClientSetupEvent event){
        ShadersIntegration.init();
    }

    public static void clientInit() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.register(new ClientEvents());
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void registerAttributeModifiers(FMLClientSetupEvent event){
            TooltipModifierHandler.register(new AttributeTooltipModifier(){
                public boolean isToolBase(AttributeModifier modifier, Player player, TooltipFlag flag){
                    return modifier.getId().equals(BASE_PROJECTILE_DAMAGE_UUID);
                }
            });
        }
    }
}
