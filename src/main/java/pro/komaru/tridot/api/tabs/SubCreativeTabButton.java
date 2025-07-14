package pro.komaru.tridot.api.tabs;

import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import pro.komaru.tridot.client.*;

import java.util.*;

public class SubCreativeTabButton extends Button{
    public CreativeModeInventoryScreen screen;
    public MultiCreativeTab multiTab;
    public SubCreativeTab subTab;
    public int ry;
    public int rx;
    public int arrow = 0;

    public SubCreativeTabButton(CreativeModeInventoryScreen screen, MultiCreativeTab multiTab, SubCreativeTab subTab, int x, int y, int ry) {
        super(x, y, 20, 20, Component.empty(), SubCreativeTabButton::click, DEFAULT_NARRATION);
        this.screen = screen;
        this.multiTab = multiTab;
        this.subTab = subTab;
        this.rx = x;
        this.ry = ry;
        refreshSub();
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (arrow == 0) {
            guiGraphics.blit(subTab.getTabsImage(), this.getX(), this.getY(), isHoveredOrFocused() ? 24 : 0, 0, this.width, 20, 64, 64);
            int itemOffset = multiTab.getCurrentSubTab().equals(subTab) ? 4 : 3;
            if (subTab.getIconItem() != null) {
                guiGraphics.renderItem(subTab.getIconItem(), this.getX() + itemOffset, this.getY() + 2);
            } else if (multiTab.getIconItem() != null) {
                guiGraphics.renderItem(multiTab.getIconItem(), this.getX() + itemOffset, this.getY() + 2);
            }

            if (mouseX >= this.getX() && mouseY >= this.getY() && mouseX <= this.getX() + 22 && mouseY < this.getY() + 20) {
                List<Component> list = new ArrayList<>();
                if (subTab.getDisplayName() != null) {
                    list.add(subTab.getDisplayName());
                } else if (multiTab.getDisplayName() != null) {
                    list.add(multiTab.getDisplayName());
                }

                guiGraphics.renderTooltip(Minecraft.getInstance().font, list, Optional.empty(), mouseX, mouseY);
            }
        } else {
            guiGraphics.blit(multiTab.getSubArrowsImage(), this.getX(), this.getY(), isHoveredOrFocused() ? 20 : 0, arrow == 1 ? 20 : 0, 20, 20, 64, 64);
        }
    }

    public static void click(Button button) {
        if (button instanceof SubCreativeTabButton subButton) {
            if (subButton.arrow == 0) {
                subButton.multiTab.setCurrentSubTab(subButton.subTab);
                subButton.screen.scrollOffs = 0;
                subButton.screen.refreshCurrentTabContents(subButton.multiTab.getDisplayItems());
            } else {
                subButton.multiTab.scroll = subButton.multiTab.scroll + subButton.arrow;
            }

            for (SubCreativeTabButton sb : ClientEvents.subCreativeTabButtons) {
                if (sb.multiTab.equals(subButton.multiTab)) {
                    sb.refreshSub();
                }
            }
        }
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    public void refreshSub() {
        refreshSubVisible(CreativeModeInventoryScreen.selectedTab);
        if (multiTab.getCurrentSubTab().equals(subTab) && arrow == 0) {
            setX(rx - 4);
            setWidth(24);
        } else {
            setX(rx);
            setWidth(20);
        }

        if (multiTab.getSortedSubTabs().contains(subTab)) {
            int i = multiTab.getSortedSubTabs().indexOf(subTab);
            setY(ry + ((i - multiTab.scroll) * 22));
        }
    }

    public void refreshSubVisible(CreativeModeTab tab) {
        if (tab == multiTab) {
            if (multiTab.getSortedSubTabs().contains(subTab)) {
                int i = multiTab.getSortedSubTabs().indexOf(subTab);
                arrow = 0;
                visible = i - multiTab.scroll >= -1 && i - multiTab.scroll < 7;

                if (multiTab.scroll > 0) {
                    if (i - multiTab.scroll == -1) arrow = -1;
                }

                if (i - multiTab.scroll == 6) arrow = 1;
            }

            return;
        }

        visible = false;
    }
}