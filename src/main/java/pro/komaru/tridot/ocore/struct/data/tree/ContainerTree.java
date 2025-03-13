package pro.komaru.tridot.ocore.struct.data.tree;

import pro.komaru.tridot.ocore.struct.data.Seq;
import pro.komaru.tridot.ocore.struct.func.Boolf;

import java.util.LinkedList;
import java.util.Queue;

@SuppressWarnings("rawtypes,unchecked")
public class ContainerTree<A> implements ITree<A> {
    private final Seq<A> children;
    private final Seq<ContainerTree<A>> containers;

    public ContainerTree<A> parent = null;

    public ContainerTree() {
        children = Seq.with();
        containers = Seq.with();
    }


    /**
     * Adds all elements as children of this container tree
     * @param all elements to add
     */
    @SafeVarargs
    public final ContainerTree<A> add(A... all) {
        children.add(all);

        return this;
    }
    /**
     * Adds element as a child of this container tree
     * @param child element to add
     */
    public ContainerTree<A> add(A child) {
        children.add(child);
        return this;
    }

    /**
     * Adds element as a subcontainer of this container tree
     * @param child element to add
     */
    public ContainerTree<A> add(ContainerTree<A> child) {
        containers.add(child);
        child.parent = this;
        return this;
    }

    /**
     * Adds all elements as children of this container tree if they don't exist yet
     * @param all elements to add
     */
    @SafeVarargs
    public final ContainerTree<A> addUnique(A... all) {
        for (A a : all) {
            addUnique(a);
        }
        return this;
    }
    /**
     * Adds element as a child of this container tree if it doesn't exist yet
     * @param child element to add
     */
    public ContainerTree<A> addUnique(A child) {
        children.addUnique(child);
        return this;
    }

    /**
     * Finds a child of this container using a filter function
     * @param filter filter function
     * @return child object
     */
    @Override
    public A child(Boolf<A> filter) {
        return children().find(filter);
    }
    /**
     * Finds a child in all containers of this tree using a filter function
     * @param filter filter function
     * @return child object
     */
    @Override
    public A childDeep(Boolf<A> filter,boolean depthSearch) {
        A child = children().find(filter);
        if(depthSearch) {
            if (child == null) {
                for (ITree<A> container : containers) {
                    A other = container.childDeep(filter);
                    if (other != null) {
                        return other;
                    }
                }
            }
        } else {
            Queue<ContainerTree<A>> queue = new LinkedList<>();
            queue.add(this);

            while (!queue.isEmpty()) {
                ContainerTree<A> current = queue.poll();
                A other = current.children().find(filter);
                if (other != null) {
                    return other;
                }
                queue.addAll(current.containers.list());
            }
        }
        return child;
    }

    /**
     * Finds a child in all containers of this tree using a filter function, in a BFS method
     * @param filter filter function
     * @return child object
     */
    @Override
    public A childDeep(Boolf<A> filter) {
        return childDeep(filter,false);
    }

    /**
     * Clears all tree
     */
    @Override
    public void clean() {
        children().clear();
        containers.each(e -> e.parent = null); //bye-bye!
        containers().clear();
    }


    /**
     * @return Children Seq
     */
    @Override
    public Seq<A> children() {
        return children;
    }

    /**
     * @return Children of this tree and subcontainers' children
     */
    @Override
    public Seq<A> childrenDeep() {
        Seq<A> all = Seq.with();
        all.addAll(children());
        for (ITree<A> container : containers())
            all.addAll(container.childrenDeep());
        return all;
    }

    /**
     * @return Subcontainer Seq
     */
    public Seq<ContainerTree<A>> containers() {
        return containers;
    }

    /**
     * @return All containers of this tree
     */
    public Seq<ContainerTree<A>> containersDeep() {
        Seq<ContainerTree<A>> all = Seq.with();
        all.addAll(containers());
        for (ContainerTree<A> other : containers())
            all.addAll(other.containers);
        return all;
    }


    @Override
    public String toString() {
        String name = this instanceof NamedContTree<?> a ? a.name : "***";
        StringBuilder sb = new StringBuilder("\u001B[33m[" + name + "]\u001B[0m");
        sb.append("\n");
        for (ContainerTree<A> cont : containers) {
            Seq<String> lines = Seq.with(cont.toString().lines().map(e -> "  " + e).toList());
            if(!lines.isEmpty()) {
                lines.set(0,lines.get(0).replaceFirst(" ",""));
            }
            sb.append("\u001B[33m-\u001B[0m");
            sb.append(String.join("\n", lines));
            sb.append("\n");
        }
        for (A child : children) {
            Seq<String> lines = Seq.with(child.toString().lines().map(e -> "  " + e).toList());
            if(!lines.isEmpty()) {
                lines.set(0,lines.get(0).replaceFirst(" ",""));
            }
            sb.append("\u001B[37m*\u001B[0m");
            sb.append(String.join("\n", lines));
            sb.append("\n");
        }
        return sb.toString();
    }
}
