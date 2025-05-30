package pro.komaru.tridot.common.commands.parts;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.*;
import pro.komaru.tridot.api.command.*;
import pro.komaru.tridot.common.commands.arguments.*;
import pro.komaru.tridot.common.registry.item.skins.*;

public class CommandArguments extends CommandArgument{

    public CommandArguments(String name, ArgumentType type){
        super(name, type);
    }

    public static CommandArguments skin(String pName){
        return new CommandArguments(pName, ItemSkinArgumentType.skinArgument());
    }

    public ItemSkin getSkin(final CommandContext<?> context, final String name){
        return context.getArgument(name, ItemSkinArgumentType.getSkin(context, name).getClass());
    }
}
