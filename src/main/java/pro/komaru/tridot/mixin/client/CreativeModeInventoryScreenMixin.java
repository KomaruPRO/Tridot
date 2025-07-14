package pro.komaru.tridot.mixin.client;

import net.minecraft.client.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.sounds.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.api.tabs.*;
import pro.komaru.tridot.client.*;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin {

    @Inject(at = @At("HEAD"), method = "selectTab")
    private void tridot$selectTab(CreativeModeTab tab, CallbackInfo ci) {
        for (SubCreativeTabButton sb : ClientEvents.subCreativeTabButtons) {
            sb.refreshSubVisible(tab);
        }
    }

    @Inject(at = @At("HEAD"), method = "mouseScrolled", cancellable = true)
    private void tridot$mouseScrolled(double mouseX, double mouseY, double delta, CallbackInfoReturnable<Boolean> cir) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) ((Object) this);
        int i = self.getGuiLeft();
        int j = self.getGuiTop();
        if (mouseX >= i - 26 && mouseY >= j && mouseX <= i && mouseY < j + 138) {
            if (CreativeModeInventoryScreen.selectedTab instanceof MultiCreativeTab multiCreativeTab) {
                if (multiCreativeTab.getSortedSubTabs().size() > 6) {
                    int add = (int) -delta;
                    multiCreativeTab.scroll = multiCreativeTab.scroll + add;
                    if (multiCreativeTab.scroll < 0) {
                        multiCreativeTab.scroll = 0;
                    } else if (multiCreativeTab.scroll > multiCreativeTab.getSortedSubTabs().size() - 6 && multiCreativeTab.getSortedSubTabs().size() > 6) {
                        multiCreativeTab.scroll = multiCreativeTab.getSortedSubTabs().size() - 6;
                    } else {
                        Minecraft.getInstance().player.playNotifySound(SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.NEUTRAL, 0.1f, 2.0f);
                    }
                    for (SubCreativeTabButton sb : ClientEvents.subCreativeTabButtons) {
                        if (multiCreativeTab.getSortedSubTabs().contains(sb.subTab)) {
                            sb.refreshSub();
                        }
                    }
                }

                cir.setReturnValue(true);
            }
        }
    }
}