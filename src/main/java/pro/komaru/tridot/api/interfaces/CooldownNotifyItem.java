package pro.komaru.tridot.api.interfaces;

import net.minecraft.sounds.*;

/**
 * Implement this Interface to play a SoundEvent after the Item cooldown ending
 */
@FunctionalInterface
public interface CooldownNotifyItem{
    SoundEvent getSoundEvent();
}