package pro.komaru.tridot.api.interfaces;

import net.minecraft.client.gui.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public interface IGuiRenderItem{
    void onGuiRender(GuiGraphics gfx, LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset);
}
