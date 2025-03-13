package pro.komaru.tridot.ocore.net.packets;

import net.minecraft.client.*;
import net.minecraft.client.sounds.*;
import net.minecraft.network.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.network.*;
import net.minecraftforge.registries.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.oclient.sound.*;

import java.util.function.*;

public class CooldownSoundPacket{
    private final double posX;
    private final double posY;
    private final double posZ;
    private final SoundEvent event;

    public CooldownSoundPacket(SoundEvent event, double posX, double posY, double posZ){
        this.event = event;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public static CooldownSoundPacket decode(FriendlyByteBuf buf){
        ResourceLocation soundID = buf.readResourceLocation();
        SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(soundID);
        return new CooldownSoundPacket(event, buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    @OnlyIn(Dist.CLIENT)
    public static void playSound(SoundEvent event){
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        if(TridotLibClient.COOLDOWN_SOUND != null && soundManager.isActive(TridotLibClient.COOLDOWN_SOUND)){
            return;
        }

        TridotLibClient.COOLDOWN_SOUND = new TridotSoundInstance(event, Minecraft.getInstance().player);
        soundManager.play(TridotLibClient.COOLDOWN_SOUND);
        if(!soundManager.isActive(TridotLibClient.COOLDOWN_SOUND)){
            TridotLibClient.COOLDOWN_SOUND = null;
        }
    }

    public static void handle(CooldownSoundPacket msg, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;
            playSound(msg.event);
        });

        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeResourceLocation(event.getLocation());
        buf.writeDouble(posX);
        buf.writeDouble(posY);
        buf.writeDouble(posZ);
    }
}