package pro.komaru.tridot.util.file.parsers;

import pro.komaru.tridot.common.compatibility.snakeyaml.DumperOptions;
import pro.komaru.tridot.common.compatibility.snakeyaml.LoaderOptions;
import pro.komaru.tridot.common.compatibility.snakeyaml.Yaml;
import pro.komaru.tridot.common.compatibility.snakeyaml.constructor.Constructor;
import pro.komaru.tridot.common.compatibility.snakeyaml.introspector.PropertyUtils;
import pro.komaru.tridot.common.compatibility.snakeyaml.representer.Representer;
import pro.komaru.tridot.util.file.Files;

import java.io.*;
import java.util.*;

public class Yamlf {
    public static Yaml yaml;
    static {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Representer representer = new Representer(options);
        representer.setPropertyUtils(new PropertyUtils());

        LoaderOptions load = new LoaderOptions();
        load.setTagInspector(tag -> true);

        yaml = new Yaml(new Constructor(load),representer,options);
    }


    public static <T> String yamlToJson(String s, Class<T> classOf) {
        return Jsonf.gson.toJson(yaml.loadAs(s,classOf));
    }
    public static <T> String yamlToJson(File f, Class<T> classOf) {
        try {
            return Jsonf.gson.toJson(yaml.loadAs(Files.inputStream(f),classOf));
        } catch (FileNotFoundException e) {
            return "{}";
        }
    }
    public static <T> T yamlToJava(String s, Class<T> classOf) {
        return Jsonf.gson.fromJson(yamlToJson(s, HashMap.class),classOf);
    }
    public static <T> T yamlToJava(File f, Class<T> classOf) {
        return Jsonf.gson.fromJson(yamlToJson(f, HashMap.class),classOf);
    }
    public static <T> T yamlListToJava(String s, Class<T> classOf) {
        return Jsonf.gson.fromJson(yamlToJson(s, Object[].class),classOf);
    }
    public static <T> T yamlListToJava(File f, Class<T> classOf) {
        return Jsonf.gson.fromJson(yamlToJson(f, Object[].class),classOf);
    }

    public static void javaToYaml(File f, Object obj) {
        try {
            String str = Jsonf.gson.toJson(obj);
            HashMap<?,?> map = Jsonf.gson.fromJson(str,HashMap.class);
            yaml.dump(map,Files.output(f));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
