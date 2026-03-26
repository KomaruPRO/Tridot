package stellar.qrix.neoforge.infrastructure.ui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import stellar.qrix.neoforge.infrastructure.ui.enums.AlignmentDirection;

public class StackElement extends BaseElement<StackElement> {
    public StackElement(AlignmentDirection childrenAlignment) {
        this.childrenAlignment = childrenAlignment;
    }

    @Override
    public void renderElement(Minecraft mc, GuiGraphics gui, float pt) {
        
    }
}
