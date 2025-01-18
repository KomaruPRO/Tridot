package github.iri.tridot.client.gui.components;

import github.iri.tridot.client.gui.screen.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.resources.language.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;

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

        public final List<String> leftButtons;
        public final List<String> rightButtons;

        public MenuRows(List<SingleMenuRow> variants){
            leftButtons = variants.stream().map(r -> r.left).collect(Collectors.toList());
            rightButtons = variants.stream().map(r -> r.right).collect(Collectors.toList());
        }
    }
}