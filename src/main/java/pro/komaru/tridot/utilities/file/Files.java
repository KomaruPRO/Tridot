package pro.komaru.tridot.utilities.file;

import pro.komaru.tridot.core.struct.func.Cons2;
import pro.komaru.tridot.core.struct.data.Pair;
import pro.komaru.tridot.core.struct.data.Seq;

import java.io.*;
import java.nio.charset.*;

public class Files {
    public static <T> T parse(Class<T> clazz, byte[] body, String ext) {
        return parse(clazz,new String(body,StandardCharsets.UTF_8),ext);
    }
    public static <T> T parse(Class<T> clazz, String body, String ext) {
        return switch (ext) {
            case "json", "j" -> Jsonf.gson.fromJson(body, clazz);
            case "yaml", "yml", "y" -> Yamlf.yamlToJava(body, clazz);
            default -> null;
        };
    }
    public static <T> T parseList(Class<T> clazz, String body, String ext) {
        return switch (ext) {
            case "json", "j" -> Jsonf.gson.fromJson(body, clazz);
            case "yaml", "yml", "y" -> Yamlf.yamlListToJava(body, clazz);
            default -> null;
        };
    }
    public static void filesDeep(File dir, Cons2<Pair<File,String>,Pair<String,String>> consumer) {
        Seq<File> seq = filesDeep(dir);
        FilePredicate predicate = FilePredicate.ofStatic(dir);
        seq.each(f -> consumer.get(new Pair<>(f,read(f)),idExt(predicate,f)));
    }
    public static void filesDeepf(File dir, Cons2<File,Pair<String,String>> consumer) {
        Seq<File> seq = filesDeep(dir);
        FilePredicate predicate = FilePredicate.ofStatic(dir);
        seq.each(f -> consumer.get(f,idExt(predicate,f)));
    }
    public static InputStreamReader input(File f) throws FileNotFoundException
    { return new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8); }
    public static OutputStreamWriter output(File f) throws FileNotFoundException
    { return new OutputStreamWriter(new FileOutputStream(f),StandardCharsets.UTF_8); }
    public static FileInputStream inputStream(File f) throws FileNotFoundException
    { return new FileInputStream(f); }
    public static FileOutputStream outputStream(File f) throws FileNotFoundException
    { return new FileOutputStream(f); }
    public static String id(FilePredicate rootPredicate, File file) {
        String path = file.getAbsolutePath();
        String id = path
                .substring(rootPredicate.get().getAbsolutePath().length()+1)
                .replace("\\","/");
        id = removeExtension(id);
        return id;
    };
    public static Pair<String,String> idExt(FilePredicate rootPredicate, File file) {
        return new Pair<>(id(rootPredicate,file), ext(file.getName()));
    };
    public static String removeExtension(String name) {
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex != -1) name = name.substring(0, dotIndex);
        return name;
    }
    public static String ext(String name) {
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex != -1) return name.substring(dotIndex + 1);
        return "";
    }

    public static Seq<File> filesDeep(File dir) {
        File[] files = dir.listFiles();
        Seq<File> fileList = Seq.with();
        for (File file : files) {
            if(file.isDirectory()) {
                fileList.addAll(filesDeep(file));
                continue;
            }
            fileList.add(file);
        }
        return fileList;
    }

    public static Seq<File> dirs(File dir) {
        File[] dirs = dir.listFiles(File::isDirectory);
        Seq<File> dirList = Seq.with();
        for (File file : dirs) {
            dirList.add(file);
        }
        return dirList;
    }

    public static String read(File f) {
        try {
            InputStreamReader reader = input(f);
            int t;
            String readResult = "";
            while ((t = reader.read()) != -1) {
                readResult = readResult + (char) t;
            }
            return readResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] readBytes(File f) {
        try (InputStream stream = inputStream(f);
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            byte[] chunk = new byte[8192];
            int bytesRead;
            while ((bytesRead = stream.read(chunk)) != -1) {
                buffer.write(chunk, 0, bytesRead);
            }
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла: " + f.getName(), e);
        }
    }

    public static void write(File f, String str) {
        try {
            OutputStreamWriter writer = output(f);
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void write(File f, byte[] bytes) {
        try {
            OutputStream outputStream = new FileOutputStream(f);
            outputStream.write(bytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
