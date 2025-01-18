package pro.komaru.tridot.utilities;

import pro.komaru.tridot.utilities.func.Cons2Func;
import pro.komaru.tridot.core.struct.Seq;

import java.util.function.*;

public class StringPredicate {
    protected StringPredicate old;
    protected String next;

    protected Cons2Func<StringPredicate,String, Seq<String>> predicate;

    public static StringPredicate ofStatic(Supplier<StringPredicate> supp) {
        return new StringPredicate() {{
            old = null;
            next = null;
            predicate = (o,n) -> supp.get().get();
        }};
    }
    public static StringPredicate ofStatic(Seq<String> path) {
        return new StringPredicate() {{
            old = null;
            next = null;
            predicate = (StringPredicate o, String n) -> path;
        }};
    }
    public static StringPredicate ofStatic(String path) {
        return new StringPredicate() {{
            old = null;
            next = null;
            predicate = (StringPredicate o, String n) -> Seq.with(path);
        }};
    }
    public static StringPredicate ofPredicate(StringPredicate oldPredicate, String nextDir) {
        return new StringPredicate() {{
            old = oldPredicate;
            next = nextDir;
            predicate = (o,n) -> Seq.with(oldPredicate.get()).add(nextDir);
        }};
    }
    public StringPredicate next(String next) {
        return StringPredicate.ofPredicate(this,next);
    }
    public Seq<String> get() {
        Seq<String> f = predicate.get(old,next);
        return f;
    }
}
