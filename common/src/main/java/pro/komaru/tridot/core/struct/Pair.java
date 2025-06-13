package pro.komaru.tridot.core.struct;

import pro.komaru.tridot.core.struct.func.Mapf;

import java.util.Objects;
import java.util.Optional;

public class Pair<A,B> {
    public A a;
    public B b;

    private boolean hasSelected = false;
    private byte selected = 0;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }
    public Pair() {
        this(null, null);
    }
    public static <A,B> Pair<A,B> of(A a, B b) {
        return new Pair<>(a, b);
    }
    public static <A,B> Pair<A,B> of() {
        return new Pair<>();
    }
    public Pair<A,B> set1(A a) {
        this.a = a;
        return this;
    }
    public Pair<A,B> set2(B b) {
        this.b = b;
        return this;
    }
    public A get1() {
        return a;
    }
    public B get2() {
        return b;
    }
    public <C> Pair<C,B> map1(Mapf<A,C> mapper) {
        return new Pair<>(mapper.get(a, 0), b);
    }
    public <C> Pair<A,C> map2(Mapf<B,C> mapper) {
        return new Pair<>(a, mapper.get(b, 0));
    }
    public <C,D> Pair<C,D> map(Mapf<Pair<A,B>, Pair<C,D>> mapper) {
        return mapper.get(this, 0);
    }
    public Pair<A,B> modify(Mapf<Pair<A,B>, Pair<A,B>> mapper) {
        return mapper.get(this, 0);
    }
    public boolean empty() {
        return empty1() && empty2();
    }
    public boolean empty1() {
        return a == null;
    }
    public boolean empty2() {
        return b == null;
    }
    public boolean has1() {
        return !empty1();
    }
    public boolean has2() {
        return !empty2();
    }
    public boolean hasBoth() {
        return has1() && has2();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair<?, ?> obj2)) return false;
        return (Objects.equals(a, obj2.a)) &&
               (Objects.equals(b, obj2.b));
    }
    public int hashCode() {
        return Objects.hash(a, b);
    }
    public String toString() {
        return a + ", " + b;
    }
    public Pair<A,B> copy() {
        var obj = new Pair<>(a, b);
        obj.hasSelected = hasSelected;
        obj.selected = selected;
        return obj;
    }

    public boolean selectable() {
        return hasSelected;
    }
    public boolean selected() {
        return selectable() && selected != 0;
    }
    public void select1() {
        hasSelected = true;
        selected = 1;
    }
    public void select2() {
        hasSelected = true;
        selected = 2;
    }
    public Optional<Object> getSelected() {
        if (!selectable()) {
            return Optional.empty();
        }
        return switch (selected) {
            case 1 -> Optional.ofNullable(a);
            case 2 -> Optional.ofNullable(b);
            default -> Optional.empty();
        };
    }

}
