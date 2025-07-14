package pro.komaru.tridot.mixin.common;

import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.api.tabs.*;

@Mixin(CreativeModeTabs.class)
public abstract class CreativeModeTabsMixin {

    @Inject(at = @At("HEAD"), method = "buildAllTabContents")
    private static void fluffy_fur$buildAllTabContentsHead(CreativeModeTab.ItemDisplayParameters parameters, CallbackInfo ci) {
        for (CreativeModeTab tab : CreativeModeTabs.allTabs()) {
            if (tab instanceof MultiCreativeTab multiCreativeTab) {
                for (SubCreativeTab subTab : multiCreativeTab.getSubTabs()) {
                    subTab.clearItems();
                }
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "buildAllTabContents")
    private static void fluffy_fur$buildAllTabContentsTail(CreativeModeTab.ItemDisplayParameters parameters, CallbackInfo ci) {
        for (CreativeModeTab tab : CreativeModeTabs.allTabs()) {
            if (tab instanceof MultiCreativeTab multiCreativeTab) {
                multiCreativeTab.sortSubTabs();
            }
        }
    }
}