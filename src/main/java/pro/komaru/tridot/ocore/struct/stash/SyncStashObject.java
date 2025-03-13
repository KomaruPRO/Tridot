package pro.komaru.tridot.ocore.struct.stash;


import pro.komaru.tridot.ocore.struct.Structs;

public class SyncStashObject<T> {

    private T obj;
    private int id;

    public SyncStashObject(T obj) {
        this.obj = obj;
        this.id = SyncStash.nextId();
    }
    public SyncStashObject(int id, byte[] obj) {
        this.obj = (T) Structs.obj(Object.class,obj);
        this.id = id;
    }

    public T get() {
        return this.obj;
    }
    public void set(T obj) {
        this.obj = obj;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }

    public byte[] toBytes() {
        return Structs.obj(obj);
    }

}
