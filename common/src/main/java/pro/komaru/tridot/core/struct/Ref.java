package pro.komaru.tridot.core.struct;

import pro.komaru.tridot.core.struct.func.Mapf;
import pro.komaru.tridot.core.struct.func.Prov;

public class Ref<T> implements Prov<T> {
    private boolean constant;
    private T value;

    private Ref(boolean constant, T value) {
        this.constant = constant;
        this.value = value;
    }

    public static Ref<Integer> of(int value) {
        return new Ref<>(false, value);
    }
    public static Ref<Boolean> of(boolean value) {
        return new Ref<>(false, value);
    }
    public static Ref<String> of(String value) {
        return new Ref<>(false, value);
    }
    public static Ref<Float> of(float value) {
        return new Ref<>(false, value);
    }

    public static <T> Ref<T> of() {
        return new Ref<>(false, null);
    }
    public static <T> Ref<T> of(T object) {
        return new Ref<>(false,object);
    }
    public static <T> Ref<T> of(Ref<T> object) {
        return of(object.value);
    }
    public static <T> Ref<T> ofImmutable(T object) {
        return new Ref<>(true, object);
    }
    public static <T> Ref<T> ofImmutable(Ref<T> object) {
        return ofImmutable(object.value);
    }

    public <U> Ref<U> map(Mapf<T,U> mapper) {
        return new Ref<>(constant, mapper.get(value,0));
    }
    public Ref<T> modify(Mapf<T,T> mapper) {
        if(constant) {
            throw new UnsupportedOperationException("Cannot modify an immutable reference");
        }
        value = mapper.get(value, 0);
        return this;
    }
    @Override
    public T get() {
        return value;
    }
    public T set(T value) {
        if(constant) {
            throw new UnsupportedOperationException("Cannot modify an immutable reference");
        }
        T oldValue = this.value;
        this.value = value;
        return oldValue;
    }
    public Ref<T> setTo(Ref<T> other) {
        other.set(this.value);
        return this;
    }

    public Ref<T> add(int num) {
        if(value instanceof Integer integer)
            ((Ref<Integer>) this).set(integer + num);
        else if(value instanceof Float f)
            ((Ref<Float>) this).set(f + num);
        else if(value instanceof Double d)
            ((Ref<Double>) this).set(d + num);
        else
            throw new UnsupportedOperationException("Cannot add to a non-numeric reference");
        return this;
    }
    public Ref<T> add() {
        return add(1);
    }

    public boolean present() {
        return value != null;
    }
    public boolean empty() {
        return value == null;
    }
    public boolean mutable() {
        return !constant;
    }
    public boolean immutable() {
        return constant;
    }
}
