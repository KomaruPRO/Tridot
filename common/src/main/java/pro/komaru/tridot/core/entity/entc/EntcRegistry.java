package pro.komaru.tridot.core.entity.entc;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import pro.komaru.tridot.core.entity.entc.pipeline.EntcCompPipeline;
import pro.komaru.tridot.core.entity.entc.pipeline.EntcCompPipelineContext;
import pro.komaru.tridot.core.struct.Pipeline;
import pro.komaru.tridot.core.struct.Seq;
import pro.komaru.tridot.core.struct.enums.GameSide;
import pro.komaru.tridot.core.struct.func.Func;

public class EntcRegistry {
    private static final Seq<EntcCompEntry> entries = Seq.empty();

    public static void register(String modid, String compid,
                                Func<EntcCompPipelineContext, EntcComp> constructor,
                                EntcCompProperties properties) {
        register(modid, compid,
                EntcCompPipeline.def(constructor), properties);
    }
    public static void register(String modid, String compid,
                                Pipeline<EntcComp, EntcCompPipelineContext> pipeline,
                                EntcCompProperties properties) {
        ResourceLocation id = new ResourceLocation(modid, compid);
        if(entries.contains(e -> e.getId().equals(id)))
            throw new IllegalArgumentException("Component " + id + " is already registered.");
        entries.add(new EntcCompEntry(
                        id, pipeline, properties
                ));
    }
    public static EntcCompEntry get(String modid, String compid) {
        return get(ResourceLocation.tryBuild(modid, compid));
    }
    public static EntcCompEntry get(ResourceLocation id) {
        return entries.select(e -> e.getId().equals(id))
                .firstOpt();
    }

    @RequiredArgsConstructor
    public static class EntcCompEntry {
        @Getter
        private final ResourceLocation id;
        private final Pipeline<EntcComp, EntcCompPipelineContext> pipeline;
        @Getter
        private final EntcCompProperties properties;
    }
    @Builder
    @Getter
    public static class EntcCompProperties {
        private boolean ignoreDepsSide;
        private boolean ignoreDepsPresence;

        private GameSide side;
        private Seq<ResourceLocation> dependencies;
    }
}
