package pro.komaru.tridot.outilities.file;

import com.google.gson.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import pro.komaru.tridot.ocore.struct.func.Cons;

import java.io.*;
import java.lang.reflect.*;

import static pro.komaru.tridot.outilities.file.Files.*;
import static pro.komaru.tridot.outilities.file.TypeAdapters.*;

public class Jsonf {
    private static final GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping().setLenient().setPrettyPrinting()
            .registerTypeAdapter(ItemStack.class,           itemStackDeserializer())
            .registerTypeAdapter(ResourceLocation.class,    resourceLocationDeserializer());
    public static Gson gson = gsonBuilder.create();

    public static void updateGson(Cons<GsonBuilder> builder) {
        builder.get(gsonBuilder);
        gson = gsonBuilder.create();
    }

    public static <T> T jsonToJava(File f, Class<T> classOf) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        try {
            f.createNewFile();
            InputStreamReader reader = input(f);
            T data = gson.fromJson(reader,classOf);
            if(data == null) {
                data = classOf.getDeclaredConstructor().newInstance();
                javaToJson(f,data);
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return classOf.getDeclaredConstructor().newInstance();
        }
    }

    public static void javaToJson(File f, Object data) {
        try {
            f.createNewFile();
            OutputStreamWriter writer = output(f);
            gson.toJson(data,writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
