package pro.komaru.tridot.common.commands.arguments;

import com.mojang.brigadier.*;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.suggestion.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import pro.komaru.tridot.common.registry.item.skins.*;

import java.util.concurrent.*;

public class ItemSkinArgumentType implements ArgumentType<ItemSkin>{
    private static final DynamicCommandExceptionType UNKNOWN = new DynamicCommandExceptionType((obj) -> Component.translatable("commands.tridot.skin.unknown", obj));

    public static ItemSkinArgumentType skinArgument(){
        return new ItemSkinArgumentType();
    }

    public static ItemSkin getSkin(final CommandContext<?> context, final String name) {
        return context.getArgument(name, ItemSkin.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        for (ItemSkin itemSkin : SkinRegistryManager.getSkins()) {
            builder.suggest(String.valueOf(itemSkin.id()));
        }

        return builder.buildFuture();
    }

    @Override
    public ItemSkin parse(StringReader reader) throws CommandSyntaxException {
        ResourceLocation resourceLocation = ResourceLocation.read(reader);
        ItemSkin itemSkin = SkinRegistryManager.get(resourceLocation.toString());
        if (itemSkin == null) throw UNKNOWN.create(resourceLocation.toString());
        return itemSkin;
    }
}