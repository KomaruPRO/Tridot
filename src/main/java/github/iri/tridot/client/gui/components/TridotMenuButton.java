package github.iri.tridot.client.gui.components;

import github.iri.tridot.client.gui.screen.*;
import github.iri.tridot.core.config.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.resources.language.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod.*;
import org.apache.commons.lang3.mutable.*;

import java.util.*;
import java.util.stream.*;

public class TridotMenuButton extends Button{
    public static ItemStack icon;

    public TridotMenuButton(int x, int y){
        super(x, y, 20, 20, Component.empty(), TridotMenuButton::click, DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick){
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        if(icon == null) icon = new ItemStack(Items.PINK_PETALS);
        guiGraphics.renderItem(icon, this.getX() + 2, this.getY() + 2);
    }

    public static void click(Button button){
        Minecraft.getInstance().setScreen(new TridotMenuScreen(Minecraft.getInstance().screen));
    }

    @Override
    public boolean isFocused(){
        return false;
    }

    public static class SingleMenuRow{
        public final String left, right;

        public SingleMenuRow(String left, String right){
            this.left = I18n.get(left);
            this.right = I18n.get(right);
        }

        public SingleMenuRow(String center){
            this(center, center);
        }
    }

    public static class MenuRows{
        public static final MenuRows MAIN_MENU = new MenuRows(Arrays.asList(
        new SingleMenuRow("menu.singleplayer"),
        new SingleMenuRow("menu.multiplayer"),
        new SingleMenuRow("fml.menu.mods", "menu.online"),
        new SingleMenuRow("narrator.button.language", "narrator.button.accessibility")
        ));

        protected final List<String> leftButtons, rightButtons;

        public MenuRows(List<SingleMenuRow> variants){
            leftButtons = variants.stream().map(r -> r.left).collect(Collectors.toList());
            rightButtons = variants.stream().map(r -> r.right).collect(Collectors.toList());
        }
    }

    @EventBusSubscriber(value = Dist.CLIENT)
    public static class OpenConfigButtonHandler{
        @SubscribeEvent
        public static void onGuiInit(ScreenEvent.Init event){
            if(ClientConfig.MENU_BUTTON.get()){
                Screen gui = event.getScreen();

                MenuRows menu = null;
                int rowIdx = 0, offsetX = 0, offsetFreeX = 0, offsetFreeY = 0;
                if(gui instanceof TitleScreen){
                    menu = MenuRows.MAIN_MENU;
                    rowIdx = ClientConfig.MENU_BUTTON_ROW.get();
                    offsetX = ClientConfig.MENU_BUTTON_ROW_X_OFFSET.get();
                    offsetFreeX = ClientConfig.MENU_BUTTON_X_OFFSET.get();
                    offsetFreeY = ClientConfig.MENU_BUTTON_Y_OFFSET.get();
                }

                if(rowIdx != 0 && menu != null){
                    boolean onLeft = offsetX < 0;
                    String target = (onLeft ? menu.leftButtons : menu.rightButtons).get(rowIdx - 1);

                    int offsetX_ = offsetX;
                    int offsetFreeX_ = offsetFreeX;
                    int offsetFreeY_ = offsetFreeY;
                    MutableObject<GuiEventListener> toAdd = new MutableObject<>(null);
                    event.getListenersList()
                    .stream()
                    .filter(w -> w instanceof AbstractWidget)
                    .map(w -> (AbstractWidget)w)
                    .filter(w -> w.getMessage()
                    .getString()
                    .equals(target))
                    .findFirst()
                    .ifPresent(w -> toAdd
                    .setValue(new TridotMenuButton(w.getX() + offsetX_ + (onLeft ? -20 : w.getWidth()) + offsetFreeX_, w.getY() + offsetFreeY_)));
                    if(toAdd.getValue() != null)
                        event.addListener(toAdd.getValue());
                }
            }
        }
    }
}
