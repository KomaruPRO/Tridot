package pro.komaru.tridot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.komaru.tridot.api.entity.comp.AIComp;
import pro.komaru.tridot.core.entity.entc.EntcRegistry;
import pro.komaru.tridot.core.entity.entc.EntcRegistry.*;
import pro.komaru.tridot.core.struct.enums.GameSide;

public class TridotCommon {
    public static final String MOD_ID = "tridot";
    public static final String MOD_NAME = "Tridot";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        EntcRegistry.register(
                MOD_ID,"ai",
                ctx -> new AIComp(),
                EntcCompProperties.builder()
                        .side(GameSide.SERVER)
                        .build()
        );
    }
}