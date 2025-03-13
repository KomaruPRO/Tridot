package pro.komaru.tridot.ocore;

import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;

import javax.annotation.*;

public class DamageHandler{
    public static ResourceKey<DamageType> register(String modId, String name){
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(modId, name));
    }

    public static DamageSource create(Level level, ResourceKey<DamageType> key, @Nullable Entity source, @Nullable Entity attacker){
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), source, attacker);
    }

    public static DamageSource create(Level level, ResourceKey<DamageType> key, @Nullable Entity source){
        return create(level, key, source, null);
    }

    public static DamageSource create(Level level, ResourceKey<DamageType> key){
        return create(level, key, null, null);
    }
}
