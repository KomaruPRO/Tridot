package pro.komaru.tridot.ocore.struct.stash;

import pro.komaru.tridot.ocore.struct.data.Seq;

public class Stash {
    private static int lastId = 0;

    private static final Seq<StashObject<?>> stash = Seq.with();

    public static <T> int add(T obj) {
        StashObject<T> stashObject = new StashObject<>(obj);
        stash.setSize(Math.max(stash.size,stashObject.getId()+1));
        stash.set(stashObject.getId(),stashObject);
        return stashObject.getId();
    }
    public static <T> T get(int obj) {
        return (T) getStash().get(obj).get();
    }
    public static <T> T getAndDelete(int obj) {
        T stashObject = get(obj);
        stash.set(obj,null);
        return stashObject;
    }


    public static int nextId() {
        return lastId++;
    }

    public static Seq<StashObject<?>> getStash() {
        return stash;
    }
}
