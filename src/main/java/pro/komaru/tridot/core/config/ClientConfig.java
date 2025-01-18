package pro.komaru.tridot.core.config;

import net.minecraftforge.common.*;
import org.apache.commons.lang3.tuple.*;

public class ClientConfig{
    public static ForgeConfigSpec.ConfigValue<Boolean>
    BOSSBAR_TITLE, CUSTOM_BOSSBARS, ABILITY_OVERLAY,
    ITEM_PARTICLE, ITEM_GUI_PARTICLE,
    MENU_BUTTON;
    public static ForgeConfigSpec.ConfigValue<Integer>
    MENU_BUTTON_ROW, MENU_BUTTON_ROW_X_OFFSET, MENU_BUTTON_X_OFFSET, MENU_BUTTON_Y_OFFSET;
    public static ForgeConfigSpec.ConfigValue<Double>
    SCREENSHAKE_INTENSITY;
    public static ForgeConfigSpec.ConfigValue<String>
    PANORAMA;

    public ClientConfig(ForgeConfigSpec.Builder builder){
        builder.comment("Graphics").push("graphics");
            SCREENSHAKE_INTENSITY = builder.comment("Intensity of screenshake.").defineInRange("screenshakeIntensity", 1d, 0, 10d);
            ABILITY_OVERLAY = builder.comment("When enabled shows the overlay after using a weapon ability (Default: true)").comment("Reload Resourcepacks after turning this on (F3+T)").define("DashOverlay", true);
            CUSTOM_BOSSBARS = builder.comment("Custom bossbars").define("CustomBossbars", true);
            builder.comment("Particles").push("particles");
                ITEM_PARTICLE = builder.comment("Enable dropping items particles.").define("itemParticle", true);
                ITEM_GUI_PARTICLE = builder.comment("Enable items particles in GUI.").define("itemGuiParticle", true);
            builder.pop();
        builder.pop();

        builder.comment("Menu").push("menu");
            PANORAMA = builder.comment("Tridot Panorama.").define("panorama", "minecraft:vanilla");
            MENU_BUTTON = builder.comment("Enable Tridot menu button.").define("menuButton", false);
            MENU_BUTTON_ROW = builder.comment("Fluffy Fur menu button row.").defineInRange("menuButtonRow", 3, 0, 4);
            MENU_BUTTON_ROW_X_OFFSET = builder.comment("Fluffy Fur menu button X offset with row.").define("menuButtonRowXOffset", 4);
            MENU_BUTTON_X_OFFSET = builder.comment("Tridot menu button X offset.").define("menuButtonXOffset", 0);
            MENU_BUTTON_Y_OFFSET = builder.comment("Tridot menu button Y offset.").define("menuButtonYOffset", 0);
        builder.pop();
    }

    public static final ClientConfig INSTANCE;
    public static final ForgeConfigSpec SPEC;

    static{
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }
}
