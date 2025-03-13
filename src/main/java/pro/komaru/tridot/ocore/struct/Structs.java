package pro.komaru.tridot.ocore.struct;


import pro.komaru.tridot.ocore.struct.data.Seq;
import pro.komaru.tridot.ocore.struct.func.Cons;
import pro.komaru.tridot.ocore.struct.func.Func;
import pro.komaru.tridot.ocore.struct.func.Prov;

import java.io.*;
import java.util.*;

public class Structs {

    public static <A> Prov<A> nil(){
        return () -> null;
    }

    public static <A,B> A cast(B obj) {
        return (A) obj;
    }
    public static  <T> T or(T a, T b) {
        if(a == null) return b;
        return a;
    }
    public static String or(String a, String b) {
        if(a == null) return b;
        if(a.isBlank()) return b;
        return a;
    }
    public static <T,A> A safeGet(T a, Func<T,A> cb) {
        return safeGet(a,cb,nil());
    }
    public static <T,A> A safeGet(T a, Func<T,A> cb, Prov<A> def) {
        if(a != null) return cb.get(a);
        return def.get();
    }
    public static <T> void safeRun(T obj, Cons<T> cb) {
        if(obj != null) cb.get(obj);
    }
    public static <T> void safeRun(T obj, Runnable cb) {
        if(obj != null) cb.run();
    }
    public static <K,V> HashMap<K,V> map(Class<K> key, Class<V> val, Object... objs) {
        HashMap<K,V> map = new HashMap<>();
        for (int i = 0; i < objs.length; i+=2) {
            K k = (K) objs[i];
            V v = (V) objs[i+1];
            map.put(k,v);
        }
        return map;
    }
    public static <K,V> HashMap<K,V> map(Object... objs) {
        HashMap<K,V> map = new HashMap<>();
        for (int i = 0; i < objs.length; i+=2) {
            K k = (K) objs[i];
            V v = (V) objs[i+1];
            map.put(k,v);
        }
        return map;
    }
    public static <A> A[] pop(A[] def) {
        var n = Seq.with(def);
        n.slice();
        return n.toArray();
    }
    public static <K,V> HashMap<V,K> revMap(HashMap<K,V> mapOrig) {
        HashMap<V,K> map = new HashMap<>();
        mapOrig.forEach((k,v) -> map.put(v,k));
        return map;
    }

    /** Объект в массив байтов */
    public static byte[] obj(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object is null");
        }

        if (!(obj instanceof Serializable)) {
            throw new IllegalArgumentException("Object is not Serializable");
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /** Массив байтов в объект */
    public static <K> K obj(Class<K> cast, byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Bytes can't be null");
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (K) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
