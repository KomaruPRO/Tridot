package github.iri.tridot.utilities.file;

import github.iri.tridot.utilities.func.*;

import java.io.*;
import java.util.function.*;

public class FilePredicate {
    protected FilePredicate old;
    protected String next;

    protected Cons2Func<FilePredicate,String, File> predicate;

    public static FilePredicate ofStatic(Supplier<FilePredicate> supp) {
        return new FilePredicate() {{
            old = null;
            next = null;
            predicate = (o,n) -> supp.get().get();
        }};
    }
    public static FilePredicate ofStatic(File path) {
        return new FilePredicate() {{
            old = null;
            next = null;
            predicate = (FilePredicate o, String n) -> path;
        }};
    }
    public static FilePredicate ofPredicate(FilePredicate oldPredicate, String nextDir) {
        return new FilePredicate() {{
            old = oldPredicate;
            next = nextDir;
            predicate = (o,n) -> new File(oldPredicate.get(),nextDir);
        }};
    }
    public static FilePredicateFunc ofPredicate(String next) {
        return (pred) -> ofPredicate(pred,next);
    }
    public static FilePredicateFunc ofPredicate(FilePredicateFunc old, String next) {
        return (pred) -> ofPredicate(old.get(pred),next);
    }

    public FilePredicate next(String next) {
        return FilePredicate.ofPredicate(this,next);
    }

    public File get() {
        File f = predicate.get(old,next);
        if(Files.ext(f.getName()).isBlank()) f.mkdir();
        return f;
    }
}

