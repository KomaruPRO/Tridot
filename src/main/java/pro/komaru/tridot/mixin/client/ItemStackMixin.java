package pro.komaru.tridot.mixin.client;

import com.google.common.collect.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.client.tooltip.*;

import java.util.*;

@Mixin(ItemStack.class)
public class ItemStackMixin{
    @Unique
    public AttributeModifier tridot$attributeModifier;

    @Unique
    public List<Component> tridot$componentList;

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;getId()Ljava/util/UUID;", ordinal = 0), index = 13)
    public AttributeModifier tridot$getTooltip(AttributeModifier value){
        this.tridot$attributeModifier = value;
        return value;
    }

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;getOperation()Lnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;", ordinal = 0), index = 16)
    public boolean tridot$getTooltip(boolean value, @Nullable Player player, TooltipFlag flag){
        if(player != null){
            for(AttributeTooltipModifier modifier : TooltipModifierHandler.getModifiers()){
                if(modifier.isToolBase(tridot$attributeModifier, player, flag)){
                    return true;
                }
            }
        }
        return value;
    }

    @ModifyVariable(method = "getTooltipLines", at = @At("STORE"))
    public Multimap<Attribute, AttributeModifier> tridot$getTooltip(Multimap<Attribute, AttributeModifier> map, @Nullable Player player, TooltipFlag flag){
        if(player != null){
            Multimap<Attribute, AttributeModifier> copied = LinkedHashMultimap.create();
            for(Map.Entry<Attribute, AttributeModifier> entry : map.entries()){
                Attribute key = entry.getKey();
                AttributeModifier modifier = entry.getValue();
                double amount = modifier.getAmount();
                AttributeModifier.Operation operation = modifier.getOperation();
                boolean flagAdd = false;

                for(AttributeTooltipModifier tooltipModifier : TooltipModifierHandler.getModifiers()){
                    if(tooltipModifier.isModifiable(key, modifier, player, flag)){
                        AttributeTooltipModifier.ModifyResult result = tooltipModifier.modify(modifier, amount, operation);
                        modifier = result.getModifier();
                        amount = result.getAmount();
                        operation = result.getOperation();
                        flagAdd = true;
                        break;
                    }
                }

                if(flagAdd){
                    copied.put(key, new AttributeModifier(modifier.getId(), modifier.getName(), amount, operation));
                }else{
                    copied.put(key, modifier);
                }
            }

            return copied;
        }

        return map;
    }

    @ModifyVariable(method = "getTooltipLines", at = @At("STORE"))
    public List<Component> tridot$getTooltip(List<Component> list, @Nullable Player player, TooltipFlag flag){
        tridot$componentList = list;
        return list;
    }

    @Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;getId()Ljava/util/UUID;"), method = "getTooltipLines")
    public void tridot$getTooltip(Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> cir){
        TooltipModifierHandler.attributeTooltipSize = tridot$componentList.size();
    }
}