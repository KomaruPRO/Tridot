package pro.komaru.tridot.core.struct.data.tree;

import pro.komaru.tridot.core.struct.Structs;
import pro.komaru.tridot.core.struct.data.Seq;

import java.util.HashMap;

public class NamedContTree<A> extends ContainerTree<A> implements INamedTree {
    public final String name;

    private final HashMap<String,Object> PATH_CACHE =
            new HashMap<>();

    public NamedContTree(String name) {
        this.name = name;
    }

    @Override
    public NamedContTree<A> add(A child) {
        return (NamedContTree<A>) super.add(child);
    }

    @Override
    public NamedContTree<A> add(ContainerTree<A> child) {
        return (NamedContTree<A>) super.add(child);
    }

    @Override
    public NamedContTree<A> addUnique(A child) {
        return (NamedContTree<A>) super.addUnique(child);
    }

    public NamedContTree<A> cd(boolean autoCreate, String... path) {
        if(path == null || path.length == 0) return this;
        if(path[0].equals("..")) return ((NamedContTree<A>) parent).cd(autoCreate,Structs.pop(path));
        for (NamedContTree<A> cont : namedContainers()) {
            if(cont.name.equals(path[0]))
                return cont.cd(autoCreate,Structs.pop(path));
        }
        if(autoCreate) {
            NamedContTree<A> newTree = new NamedContTree<>(path[0]);
            containers().add(newTree);
            newTree.parent = this;
            return newTree.cd(true,Structs.pop(path));
        }
        return null;
    }
    public NamedContTree<A> cd(boolean autoCreate,String path) {
        return cd(autoCreate,path.split("/"));
    }
    public NamedContTree<A> cd(String... path) {
        return cd(true,path);
    }
    public NamedContTree<A> cd(String path) {
        return cd(true,path);
    }

    public static void main(String[] args) {
        NamedContTree<String> main = new NamedContTree<>("main");
        main
                .cd("a")
                        .add("A section!")
                .cd("../b")
                        .add("B section!")
                .cd("../a/c")
                        .add("C section!")
                .cd("../..")
                        .add("End section!");
        System.out.println(main);
    }

    public Seq<NamedContTree<A>> namedContainers() {
        return Structs.cast(containers().select(e -> e instanceof NamedContTree));
    }

    @Override
    public String name() {
        return name;
    }
}
