package pro.komaru.tridot.core.config;

import net.minecraftforge.common.*;
import org.apache.commons.lang3.tuple.*;

public class ClientConfig{
    public static ForgeConfigSpec.ConfigValue<Boolean>
    BOSSBAR_TITLE, CUSTOM_BOSSBARS, ABILITY_OVERLAY,
    ITEM_PARTICLE, ITEM_GUI_PARTICLE;
    public static ForgeConfigSpec.ConfigValue<Double>
    SCREENSHAKE_INTENSITY;

    public ClientConfig(ForgeConfigSpec.Builder builder){
        builder.comment("Graphics").push("graphics");
            SCREENSHAKE_INTENSITY = builder.comment("Intensity of screenshake.").defineInRange("screenshakeIntensity", 1d, 0, 10d);
            ABILITY_OVERLAY = builder.comment("When enabled shows the overlay after using a weapon ability (Default: true)").comment("Reload Resourcepacks after turning this on (F3+T)").define("AbilityOverlay", true);
            CUSTOM_BOSSBARS = builder.comment("Custom bossbars").define("CustomBossbars", true);
            BOSSBAR_TITLE = builder.comment("Bossbar boss titles").define("BossbarTitles", true);

        builder.comment("Particles").push("particles");
                ITEM_PARTICLE = builder.comment("Enable dropping items particles.").define("itemParticle", true);
                ITEM_GUI_PARTICLE = builder.comment("Enable items particles in GUI.").define("itemGuiParticle", true);
            builder.pop();
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
