package github.iri.tridot.util.client;

import com.google.common.collect.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.effect.*;

import java.util.*;

public class TooltipUtils{
    public static void addEffectsTooltip(ImmutableList<MobEffectInstance> effects, List<Component> pTooltips, float pDurationFactor, float chance){
        if(!effects.isEmpty()){
            if(chance > 0 && chance < 1){
                pTooltips.add(CommonComponents.EMPTY);
                pTooltips.add(Component.translatable("tooltip.sakurautil.with_chance", String.format("%.1f%%", chance * 100)).withStyle(ChatFormatting.GRAY).append(Component.literal(String.format("%.1f%%", chance * 100))));
            }else{
                pTooltips.add(CommonComponents.EMPTY);
                pTooltips.add(Component.translatable("tooltip.sakurautil.applies").withStyle(ChatFormatting.GRAY));
            }

            for(MobEffectInstance mobeffectinstance : effects){
                MutableComponent mutablecomponent = Component.translatable(mobeffectinstance.getDescriptionId());
                MobEffect mobeffect = mobeffectinstance.getEffect();
                if(mobeffectinstance.getAmplifier() > 0){
                    mutablecomponent = Component.literal(" ").append(Component.translatable("potion.withAmplifier", mutablecomponent, Component.translatable("potion.potency." + mobeffectinstance.getAmplifier())));
                }

                if(!mobeffectinstance.endsWithin(20)){
                    mutablecomponent = Component.literal(" ").append(Component.translatable("potion.withDuration", mutablecomponent, MobEffectUtil.formatDuration(mobeffectinstance, pDurationFactor)));
                }

                pTooltips.add(mutablecomponent.withStyle(mobeffect.getCategory().getTooltipFormatting()));
            }

            pTooltips.add(CommonComponents.EMPTY);
        }
    }

}
