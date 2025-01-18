package pro.komaru.tridot.utilities.stash;

public class StashObject<T> {

    private T obj;
    private int id;

    public StashObject(T obj) {
        this.obj = obj;
        this.id = Stash.nextId();
    }

    public T get() {
        return this.obj;
    }
    public void set(T obj) {
        this.obj = obj;
    }
    public void setId(int id) {
        if(Stash.getStash().get(id) != null && Stash.getStash().get(id) != this) return;
        this.id = id;
    }
    public int getId() {
        return this.id;
    }

}
