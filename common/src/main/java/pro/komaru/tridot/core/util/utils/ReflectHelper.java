package pro.komaru.tridot.core.util.utils;

import pro.komaru.tridot.core.struct.func.Prov;

import java.lang.reflect.*;

@SuppressWarnings("unchecked")
public class ReflectHelper {
    private static final ReflectHelper instance = new ReflectHelper();
    public static ReflectHelper get() {
        return instance;
    }

    public <T> T[] newArray(Class<T> type, int length) {
        return (T[]) java.lang.reflect.Array.newInstance(type, length);
    }

    public <T> T[] newArray(T[] oldType, int length){
        return (T[])java.lang.reflect.Array.newInstance(oldType.getClass().getComponentType(), length);
    }

    public boolean isWrapper(Class<?> type){
        return type == Byte.class || type == Short.class || type == Integer.class || type == Long.class || type == Character.class || type == Boolean.class || type == Float.class || type == Double.class;
    }

    public <T> Prov<T> cons(Class<T> type){
        try{
            Constructor<T> c = type.getDeclaredConstructor();
            c.setAccessible(true);
            return () -> {
                try{
                    return c.newInstance();
                }catch(Exception e){
                    throw new RuntimeException(e);
                }
            };
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public <T> T get(Field field){
        return get(null, field);
    }

    public <T> T get(Object object, Field field){
        try{
            return (T)field.get(object);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public <T> T get(Class<?> type, Object object, String name){
        try{
            Field field = type.getDeclaredField(name);
            field.setAccessible(true);
            return (T)field.get(object);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public <T> T get(Object object, String name){
        return get(object.getClass(), object, name);
    }

    public <T> T get(Class<?> type, String name){
        return get(type, null, name);
    }

    public void set(Class<?> type, Object object, String name, Object value){
        try{
            Field field = type.getDeclaredField(name);
            field.setAccessible(true);
            field.set(object, value);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void set(Object object, Field field, Object value){
        try{
            field.set(object, value);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void set(Object object, String name, Object value){
        set(object.getClass(), object, name, value);
    }

    public void set(Class<?> type, String name, Object value){
        set(type, null, name, value);
    }

    public <T> T invoke(Class<?> type, Object object, String name, Object[] args, Class<?>... parameterTypes){
        try{
            Method method = type.getDeclaredMethod(name, parameterTypes);
            method.setAccessible(true);
            return (T)method.invoke(object, args);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public <T> T invoke(Class<?> type, String name, Object[] args, Class<?>... parameterTypes){
        return invoke(type, null, name, args, parameterTypes);
    }

    public <T> T invoke(Class<?> type, String name){
        return invoke(type, name, null);
    }

    public <T> T invoke(Object object, String name, Object[] args, Class<?>... parameterTypes){
        return invoke(object.getClass(), object, name, args, parameterTypes);
    }

    public <T> T invoke(Object object, String name){
        return invoke(object, name, null);
    }

    public <T> T make(String type){
        try{
            Class<T> c = (Class<T>)Class.forName(type);
            return c.getDeclaredConstructor().newInstance();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}