package pro.komaru.tridot.core.entity.entc.pipeline;

import pro.komaru.tridot.core.entity.entc.EntcComp;
import pro.komaru.tridot.core.struct.Pipeline;
import pro.komaru.tridot.core.struct.func.Func;

public class EntcCompPipeline {
    public static Pipeline<EntcComp, EntcCompPipelineContext> def(Func<EntcCompPipelineContext,EntcComp> constructor) {
        return new Pipeline<EntcComp,EntcCompPipelineContext>()
                .then((cc,ctx) -> constructor.get(ctx))
                ;
    }
}
