package pro.komaru.tridot.common.commands;

import net.minecraft.commands.synchronization.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.registries.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.common.commands.arguments.*;

public class ModArgumentTypes{
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARG_TYPES = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, Tridot.ID);
    public static final RegistryObject<ArgumentTypeInfo<?, ?>> SKIN_ARG = ARG_TYPES.register("skin", () -> ArgumentTypeInfos.registerByClass(ItemSkinArgumentType.class, SingletonArgumentInfo.contextFree(ItemSkinArgumentType::skinArgument)));

    public static void register(IEventBus eventBus){
        ARG_TYPES.register(eventBus);
    }
}
