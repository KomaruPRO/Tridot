package pro.komaru.tridot.api.tabs;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

public class MultiCreativeTab extends CreativeModeTab {
    public ResourceLocation multiBackgroundLocation;
    public ResourceLocation subArrowsImage = MultiCreativeTabBuilder.STANDARD_SUB_ARROWS;

    public ArrayList<SubCreativeTab> subCreativeTabs = new ArrayList<>();
    public ArrayList<SubCreativeTab> sortedSubCreativeTabs = new ArrayList<>();

    public SubCreativeTab currentSubTab;
    public SubCreativeTab mainSubtab;

    public int scroll = 0;

    public MultiCreativeTab(MultiCreativeTabBuilder builder) {
        super(builder);
    }

    public static MultiCreativeTabBuilder builder() {
        return new MultiCreativeTabBuilder(Row.TOP, 0);
    }

    @Override
    public Component getDisplayName() {
        if (getCurrentSubTab() != null && getCurrentSubTab().getDisplayName() != null) {
            return super.getDisplayName().copy().append(": ").append(getCurrentSubTab().getDisplayName());
        }

        return super.getDisplayName();
    }

    @Override
    public ItemStack getIconItem() {
        if (mainSubtab != null && mainSubtab.getIconItem() != null) {
            return mainSubtab.getIconItem();
        }

        return super.getIconItem();
    }

    @Override
    public boolean showTitle() {
        if (getCurrentSubTab() != null && getCurrentSubTab().hasShowTitle()) {
            return getCurrentSubTab().showTitle();
        }

        return false;
    }

    @Override
    public boolean canScroll() {
        if (getCurrentSubTab() != null && getCurrentSubTab().hasCanScroll()) {
            return getCurrentSubTab().canScroll();
        }

        return super.canScroll();
    }

    @Override
    public Collection<ItemStack> getDisplayItems() {
        if (getCurrentSubTab() != null && getCurrentSubTab().getDisplayItems() != null) {
            return getCurrentSubTab().getDisplayItems();
        }

        return super.getDisplayItems();
    }

    @Override
    public Collection<ItemStack> getSearchTabDisplayItems() {
        if (getCurrentSubTab() != null && getCurrentSubTab().getSearchTabDisplayItems() != null) {
            return getCurrentSubTab().getSearchTabDisplayItems();
        }

        return super.getSearchTabDisplayItems();
    }

    @Override
    public boolean contains(ItemStack stack) {
        return getSearchTabDisplayItems().contains(stack);
    }

    @Override
    public ResourceLocation getBackgroundLocation() {
        if (getCurrentSubTab() != null && getCurrentSubTab().getBackgroundLocation() != null) {
            return getCurrentSubTab().getBackgroundLocation();
        }

        return multiBackgroundLocation;
    }

    @Override
    public boolean hasSearchBar() {
        if (getCurrentSubTab() != null && getCurrentSubTab().hasSearch()) {
            return getCurrentSubTab().hasSearchBar();
        }

        return super.hasSearchBar();
    }

    @Override
    public int getSearchBarWidth() {
        if (getCurrentSubTab() != null && getCurrentSubTab().hasSearchBarWidth()) {
            return getCurrentSubTab().getSearchBarWidth();
        }

        return super.getSearchBarWidth();
    }

    @Override
    public ResourceLocation getTabsImage() {
        return super.getTabsImage();
    }

    @Override
    public int getLabelColor() {
        if (getCurrentSubTab() != null && getCurrentSubTab().hasLabelColor()) {
            return getCurrentSubTab().getLabelColor();
        }

        return super.getLabelColor();
    }

    @Override
    public int getSlotColor() {
        if (getCurrentSubTab() != null && getCurrentSubTab().hasSlotColor()) {
            return getCurrentSubTab().getSlotColor();
        }

        return super.getSlotColor();
    }

    public MultiCreativeTab withSubArrowsImage(ResourceLocation image) {
        this.subArrowsImage = image;
        return this;
    }

    public ResourceLocation getSubArrowsImage() {
        return subArrowsImage;
    }

    public MultiCreativeTab addSubTab(SubCreativeTab subTab) {
        this.subCreativeTabs.add(subTab);
        if (getCurrentSubTab() == null) {
            setCurrentSubTab(subTab);
        }

        return this;
    }

    public MultiCreativeTab sortSubTabs() {
        sortedSubCreativeTabs.clear();
        for (SubCreativeTab subTab : getSubTabs()) {
            if (subTab.subShow.get()) {
                sortedSubCreativeTabs.add(subTab);
            }
        }

        if (scroll < 0) {
            scroll = 0;
        } else if (scroll > getSortedSubTabs().size() - 6 && getSortedSubTabs().size() > 6) {
            scroll = getSortedSubTabs().size() - 6;
        }

        if (!getSortedSubTabs().isEmpty()) {
            if (!getSortedSubTabs().contains(getCurrentSubTab())) {
                setCurrentSubTab(getSortedSubTabs().get(0));
            }
        }
        return this;
    }

    public MultiCreativeTab setCurrentSubTab(SubCreativeTab subTab) {
        this.currentSubTab = subTab;
        return this;
    }

    public ArrayList<SubCreativeTab> getSubTabs() {
        return subCreativeTabs;
    }

    public ArrayList<SubCreativeTab> getSortedSubTabs() {
        return sortedSubCreativeTabs;
    }

    public SubCreativeTab getCurrentSubTab() {
        return currentSubTab;
    }
}