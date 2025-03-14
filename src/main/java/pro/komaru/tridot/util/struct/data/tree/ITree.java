package pro.komaru.tridot.util.struct.data.tree;

import pro.komaru.tridot.util.struct.data.Seq;
import pro.komaru.tridot.util.struct.func.Boolf;

public interface ITree<A> {
    Seq<A> children();
    Seq<A> childrenDeep();

    A child(Boolf<A> filter);


    /**
     * Deepfinding a child using a filter in either BFS or DFS
     * @param filter filter function
     * @param depthSearch if true, then it uses DFS instead of BFS
     * @return child object
     */
    A childDeep(Boolf<A> filter, boolean depthSearch);

    default A childDeep(Boolf<A> filter) {
        return childDeep(filter,false);
    };
    default A child(Boolf<A> filter, boolean deep, boolean depthSearch) {
        return deep ? childDeep(filter,depthSearch) : child(filter);
    }
    default A child(Boolf<A> filter, boolean deep) {
        return child(filter,deep,false);
    }

    /**
     * Clears all tree
     */
    void clean();
}
