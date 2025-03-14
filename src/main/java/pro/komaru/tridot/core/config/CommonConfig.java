package pro.komaru.tridot.core.config;

import net.minecraftforge.common.*;
import org.apache.commons.lang3.tuple.*;

public class CommonConfig{
    public static ForgeConfigSpec.ConfigValue<Boolean>
    PERCENT_ARMOR;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }

    public static final CommonConfig INSTANCE;
    public static final ForgeConfigSpec SPEC;

    public CommonConfig(ForgeConfigSpec.Builder builder) {
        PERCENT_ARMOR = builder.comment("When enabled armor is defined as percent (Default: true)").comment("keep in mind that Minecraft attributes are limited, so you'll need to download some mod that removes the limits, otherwise high tier armor will be a nonsense thanks to Mojang... that's the reason why percentage armor is implemented").define("PercentArmor", false);
    }
}