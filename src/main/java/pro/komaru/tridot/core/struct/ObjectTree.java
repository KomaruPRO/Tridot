package pro.komaru.tridot.core.struct;

import pro.komaru.tridot.utilities.func.Boolf;
import pro.komaru.tridot.utilities.func.Cons;

public class ObjectTree<T>  {
    protected Seq<T> childObjects = Seq.with();
    protected String name = "";
    protected Seq<ObjectTree<T>> childTrees = Seq.with();

    private ObjectTree() {
    };

    public Seq<T> allDeep() {
        Seq<T> all = Seq.with(children());
        for (ObjectTree<T> tree : trees()) {
            all.add(tree.allDeep());
        }
        return all;
    }

    public static <T> ObjectTree<T> of(String _name,Seq<T> children) {
        return new ObjectTree<>() {{
            name = _name;
            childObjects = children.copy();
        }};
    }
    public Seq<T> children() {
        return childObjects;
    };
    public Seq<T> deepChildren() {
        Seq<T> thisChildren = children().copy();
        Seq<T> treesChildren = Seq.with();
        trees().each(t -> treesChildren.add(t.deepChildren()));
        thisChildren.addAll(treesChildren);
        return thisChildren;
    }
    public Seq<ObjectTree<T>> trees() {
        return childTrees;
    }

    public static void safeClear(ObjectTree<?> tree) {
        if(tree != null) tree.clear();
    }

    public ObjectTree<T> create(String name, Seq<T> children) {
        ObjectTree<T> tree = ObjectTree.of(name,children);
        childTrees.remove((t) -> t.is(name));
        childTrees.add(tree);
        return tree;
    }
    public ObjectTree<T> create(String name) {
        return create(name, Seq.with());
    }

    public ObjectTree<T> tree(Boolf<ObjectTree<T>> filter) {
        return trees().find(filter);
    }
    public ObjectTree<T> tree(String name) {
        return tree(t -> t.is(name));
    }
    public ObjectTree<T> tree(String name,String sub) {
        ObjectTree<T> parentTree = tree(t -> t.is(name));
        if(parentTree == null) return null;
        return parentTree.tree(t -> t.is(sub));
    }
    public ObjectTree<T> treeDeep(String name) {
        ObjectTree<T> tree = tree(name);
        if(tree != null) return tree;
        for (ObjectTree<T> objTree : trees()) {
            ObjectTree<T> foundTree = objTree.treeDeep(name);
            if(foundTree != null) return foundTree;
        }
        return null;
    }
    public T child(Boolf<T> filter) {
        return children().find(filter);
    }

    private boolean is(String name) {
        return this.name.equals(name);
    }

    public <A> void each(Class<A> as, Cons<A> cons) {
        children().each(e -> {
            cons.get((A) e);
        });
    }

    public void clear() {
        children().clear();
        trees().clear();
    }

    public String name() {
        return name;
    }

}
