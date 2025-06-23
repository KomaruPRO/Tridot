package pro.komaru.tridot.common.registry.item.armor;

import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraftforge.api.distmarker.*;
import pro.komaru.tridot.util.*;

import java.util.*;

@SuppressWarnings("removal")
public class EffectArmorItem extends SuitArmorItem{
    public EffectArmorItem(ArmorMaterial material, Type type, Properties settings){
        super(material, type, settings);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player){
        if(!level.isClientSide()){
            if(hasFullSuitOfArmorOn(player)){
                evaluateArmorEffects(player);
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flags){
        super.appendHoverText(stack, world, list, flags);
        if (stack.getItem() instanceof ArmorItem armor) {
            var effects = AbstractArmorRegistry.EFFECTS.get(armor.getMaterial());
            if (effects != null) {
                var component = Component.translatable("tooltip.tridot.applies_fullkit").withStyle(ChatFormatting.GRAY);
                for (int i = 0; i < effects.size(); i++) {
                    MobEffect effect = effects.get(i).instance().get().getEffect();
                    var effectName = effect.getDisplayName().getString();
                    component.append(Component.literal(effectName).withStyle(stack.getRarity().getStyleModifier()));
                    if (i < effects.size() - 1) {
                        component.append(Component.literal(", ").withStyle(ChatFormatting.GRAY));
                    }
                }

                list.add(component);
            }
        }
    }

    public void evaluateArmorEffects(Player player){
        for (var entry : AbstractArmorRegistry.EFFECTS.entrySet()) {
            ArmorMaterial material = entry.getKey();
            if (!hasCorrectArmorOn(material, player)) continue;

            for (var effectData : entry.getValue()) {
                if (effectData.condition().test(player)) {
                    MobEffect effect = effectData.instance().get().getEffect();
                    if (!player.hasEffect(effect)) {
                        player.addEffect(effectData.instance().get());
                    }
                }
            }
        }
    }
}