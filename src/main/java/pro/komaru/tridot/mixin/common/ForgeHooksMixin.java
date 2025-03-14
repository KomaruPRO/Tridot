package pro.komaru.tridot.mixin.common;

import com.google.gson.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraftforge.common.*;
import net.minecraftforge.common.crafting.*;
import net.minecraftforge.common.crafting.conditions.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.*;

@Mixin(ForgeHooks.class)
public class ForgeHooksMixin{

    @Inject(method = "loadLootTable", at = @At("HEAD"), cancellable = true, remap = false)
    private static void tridot$loadLootTable(Gson gson, ResourceLocation name, JsonElement data, boolean custom, CallbackInfoReturnable<LootTable> cir){
        JsonObject json = data.getAsJsonObject();
        if(json.has(TridotLib.ID + ":conditions")){
            if(!CraftingHelper.processConditions(GsonHelper.getAsJsonArray(json, TridotLib.ID + ":conditions"), ICondition.IContext.EMPTY)){
                cir.setReturnValue(null);
            }
        }
    }
}