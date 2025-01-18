package pro.komaru.tridot.client.graphics.tooltip;

import java.util.*;

public class TooltipModifierHandler{
    public static int attributeTooltipSize = 0;
    public static List<AttributeTooltipModifier> modifiers = new ArrayList<>();

    public static void register(AttributeTooltipModifier modifier){
        modifiers.add(modifier);
    }

    public static List<AttributeTooltipModifier> getModifiers(){
        return modifiers;
    }

    public static int getAttributeTooltipSize(){
        return attributeTooltipSize;
    }
}
