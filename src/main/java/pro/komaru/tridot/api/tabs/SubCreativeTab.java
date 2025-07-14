package pro.komaru.tridot.api.tabs;

import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import pro.komaru.tridot.*;

import java.util.*;
import java.util.function.*;

public class SubCreativeTab {
    public Component displayName;
    public Supplier<ItemStack> iconItemStack = () -> null;
    public boolean showTitle = true;
    public boolean canScroll = true;
    public Collection<ItemStack> displayItems = ItemStackLinkedSet.createTypeAndTagSet();
    public Set<ItemStack> displayItemsSearchTab = ItemStackLinkedSet.createTypeAndTagSet();
    public ResourceLocation backgroundLocation;
    public boolean hasSearchBar = false;
    public int searchBarWidth = 89;
    public ResourceLocation tabsImage = Tridot.ofTridot("textures/gui/subtab.png");
    public int labelColor = 4210752;
    public int slotColor = -2130706433;

    public boolean hasShowTitle = false;
    public boolean hasCanScroll = false;
    public boolean hasSearch = false;
    public boolean hasSearchBarWidth = false;
    public boolean hasLabelColor = false;
    public boolean hasSlotColor = false;
    public Supplier<Boolean> subShow = () -> true;

    public static SubCreativeTab create() {
        return new SubCreativeTab();
    }

    public SubCreativeTab title(Component title) {
        this.displayName = title;
        return this;
    }

    public SubCreativeTab icon(Supplier<ItemStack> icon) {
        this.iconItemStack = icon;
        return this;
    }

    public SubCreativeTab hideTab() {
        this.subShow = () -> false;
        return this;
    }

    public SubCreativeTab hideTitle() {
        this.hasShowTitle = true;
        this.showTitle = false;
        return this;
    }

    public SubCreativeTab noScrollBar() {
        this.hasCanScroll = true;
        this.canScroll = false;
        return this;
    }

    public SubCreativeTab withTitle() {
        this.hasShowTitle = true;
        this.showTitle = true;
        return this;
    }

    public SubCreativeTab witScrollBar() {
        this.hasCanScroll = true;
        this.canScroll = true;
        return this;
    }

    public SubCreativeTab withBackgroundLocation(ResourceLocation background) {
        this.backgroundLocation = background;
        return this;
    }

    public SubCreativeTab hideSearchBar() {
        this.hasSearch = true;
        this.hasSearchBar = false;
        return this;
    }

    public SubCreativeTab withSearchBar() {
        this.hasSearch = true;
        this.hasSearchBar = true;
        return this;
    }

    public SubCreativeTab withSearchBar(int searchBarWidth) {
        this.hasSearchBarWidth = true;
        this.searchBarWidth = searchBarWidth;
        return withSearchBar();
    }

    public SubCreativeTab withSearchBarWidth(int searchBarWidth) {
        this.hasSearchBarWidth = true;
        this.searchBarWidth = searchBarWidth;
        return this;
    }

    public SubCreativeTab withTabsImage(ResourceLocation tabsImage) {
        this.tabsImage = tabsImage;
        return this;
    }

    public SubCreativeTab withLabelColor(int labelColor) {
        this.hasLabelColor = true;
        this.labelColor = labelColor;
        return this;
    }

    public SubCreativeTab withSlotColor(int slotColor) {
        this.hasSlotColor = true;
        this.slotColor = slotColor;
        return this;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public ItemStack getIconItem() {
        return iconItemStack.get();
    }

    public boolean showTitle() {
        return showTitle;
    }

    public boolean canScroll() {
        return canScroll;
    }

    public Collection<ItemStack> getDisplayItems() {
        return displayItems;
    }

    public Collection<ItemStack> getSearchTabDisplayItems() {
        return displayItemsSearchTab;
    }

    public boolean contains(ItemStack stack) {
        return getSearchTabDisplayItems().contains(stack);
    }

    public ResourceLocation getBackgroundLocation() {
        return backgroundLocation;
    }

    public boolean hasSearchBar() {
        return hasSearchBar;
    }

    public int getSearchBarWidth() {
        return searchBarWidth;
    }

    public ResourceLocation getTabsImage() {
        return tabsImage;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public int getSlotColor() {
        return slotColor;
    }

    public boolean hasShowTitle() {
        return hasShowTitle;
    }

    public boolean hasCanScroll() {
        return hasCanScroll;
    }

    public boolean hasSearch() {
        return hasSearch;
    }

    public boolean hasSearchBarWidth() {
        return hasSearchBarWidth;
    }

    public boolean hasLabelColor() {
        return hasLabelColor;
    }

    public boolean hasSlotColor() {
        return hasSlotColor;
    }

    public SubCreativeTab addItem(ItemStack item) {
        addDisplayItem(item);
        addDisplaySearchItem(item);
        return this;
    }

    public SubCreativeTab addItem(ItemLike item) {
        addDisplayItem(item);
        addDisplaySearchItem(item);
        return this;
    }

    public SubCreativeTab addItems(Collection<ItemStack> items) {
        addDisplayItems(items);
        addDisplaySearchItems(items);
        return this;
    }

    public SubCreativeTab addDisplayItem(ItemStack item) {
        displayItems.add(item);
        return this;
    }

    public SubCreativeTab addDisplayItem(ItemLike item) {
        displayItems.add(new ItemStack(item));
        return this;
    }

    public SubCreativeTab addDisplayItems(Collection<ItemStack> items) {
        displayItems.addAll(items);
        return this;
    }

    public SubCreativeTab addDisplaySearchItem(ItemStack item) {
        displayItemsSearchTab.add(item);
        return this;
    }

    public SubCreativeTab addDisplaySearchItem(ItemLike item) {
        displayItemsSearchTab.add(new ItemStack(item));
        return this;
    }

    public SubCreativeTab addDisplaySearchItems(Collection<ItemStack> items) {
        displayItemsSearchTab.addAll(items);
        return this;
    }

    public void clearItems() {
        displayItems.clear();
        displayItemsSearchTab.clear();
    }
}