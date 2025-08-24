package pro.komaru.tridot.core.struct;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pro.komaru.tridot.core.struct.func.Func2;

import java.util.stream.IntStream;

public class Pipeline<T,C> {
    private final Seq<Func2<T,C,T>> steps = Seq.empty();

    public Pipeline(Pipeline<T, C> tcPipeline) {
        steps.addAll(tcPipeline.steps);
    }
    public Pipeline() {

    }

    public Pipeline<T,C> then(Func2<T,C,T> step) {
        steps.add(step);
        return this;
    }

    public PipelineResult<T, C> run(T input, C context) {
        T obj = input;
        for (Func2<T, C, T> fn : steps)
            obj = fn.get(obj, context);
        return new PipelineResult<>(obj, context);
    }

    public Seq<PipelineResult<T,C>> parallelRun(Seq<T> inputs, Seq<C> ctx) {
        if (inputs.size != ctx.size)
            throw new IllegalArgumentException("Input and context sequences must have the same size.");

        return Seq.with(
                IntStream.range(0, inputs.size)
                        .parallel()
                        .mapToObj(i -> run(inputs.getOrNull(i), ctx.get(i)))
                        .toList()
        );
    }
    public Seq<PipelineResult<T,C>> sequenceRun(Seq<T> inputs, Seq<C> context) {
        if (inputs.size != context.size)
            throw new IllegalArgumentException("Input and context sequences must have the same size.");

        return Seq.with(
                IntStream.range(0, inputs.size)
                        .sequential()
                        .mapToObj(i -> run(inputs.getOrNull(i), context.get(i)))
                        .toList()
        );
    }

    public Pipeline<T,C> copy() {
        return new Pipeline<>(this);
    }

    public static <T,C> Pipeline<T,C> create() {
        return new Pipeline<>();
    }

    @Getter
    @RequiredArgsConstructor
    public static class PipelineResult<T,C> {
        private final T value;
        private final C context;
    }
}
