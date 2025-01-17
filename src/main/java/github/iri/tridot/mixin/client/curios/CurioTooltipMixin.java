package github.iri.tridot.mixin.client.curios;

import com.google.common.collect.*;
import github.iri.tridot.client.tooltip.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.extensibility.*;
import org.spongepowered.asm.mixin.injection.*;
import top.theillusivec4.curios.client.*;

import java.util.*;

@Mixin(ClientEventHandler.class)
public abstract class CurioTooltipMixin implements IMixinConfigPlugin{

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName){
        if(mixinClassName.startsWith("top.theillusivec4.curios.client.ClientEventHandler")){
            return ModList.get().isLoaded("curios");
        }
        return false;
    }

    @ModifyVariable(method = "onTooltip", at = @At("STORE"), remap = false)
    public Multimap<Attribute, AttributeModifier> tridot$getTooltip(Multimap<Attribute, AttributeModifier> multimap, ItemTooltipEvent event){
        if(event != null && multimap != null){
            Multimap<Attribute, AttributeModifier> copied = LinkedHashMultimap.create();
            for(Map.Entry<Attribute, AttributeModifier> entry : multimap.entries()){
                Attribute key = entry.getKey();
                if(key != null){
                    AttributeModifier modifier = entry.getValue();
                    double amount = modifier.getAmount();
                    boolean flagAdd = false;
                    AttributeModifier.Operation operation = modifier.getOperation();

                    for(AttributeTooltipModifier tooltipModifier : TooltipModifierHandler.getModifiers()){
                        if(tooltipModifier.isModifiable(key, modifier, event.getEntity(), event.getFlags())){
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
            }

            return copied;
        }
        return multimap;
    }
}
