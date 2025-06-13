package pro.komaru.tridot.core.util.utils;

import pro.komaru.tridot.core.struct.Seq;
import pro.komaru.tridot.core.struct.func.Cons3;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileHelper {
    public static @Nullable InputStreamReader reader(File f)
    {
        if(!f.exists()) return null;
        return new InputStreamReader(inputStream(f), StandardCharsets.UTF_8);
    }
    public static @Nullable OutputStreamWriter writer(File f)
    {
        if(!f.exists()) return null;
        return new OutputStreamWriter(outputStream(f), StandardCharsets.UTF_8);
    }
    public static @Nullable FileInputStream inputStream(File f)
    {
        if(!f.exists()) return null;
        try {
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
    public static @Nullable FileOutputStream outputStream(File f)
    {
        if(!f.exists()) return null;
        try {
            return new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static @Nullable String read(File f) {
        try (InputStreamReader reader = reader(f)) {
            if (reader == null) return null;
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, read);
            }
            return sb.toString();
        } catch (IOException e) {
            return null;
        }
    }
    public static boolean write(File f, String content) {
        try (OutputStreamWriter writer = writer(f)) {
            if (writer == null) return false;
            writer.write(content);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static Seq<File> dirs(File dir) {
        File[] dirs = dir.listFiles(File::isDirectory);
        return Seq.with(dirs);
    }
    public static Seq<File> walk(File dir, Cons3<File, String, String> callback) {
        if(!dir.isDirectory()) return Seq.empty();
        Seq<File> walked = walk(dir);
        for (File file : walked) {
            callback.get(file, lazyName(dir, file), ext(file));
        }
        return walked;
    }
    public static Seq<File> walk(File dir) {
        if(!dir.isDirectory()) return Seq.empty();
        File[] listed = dir.listFiles();
        Seq<File> walked = Seq.empty();
        for (File file : listed) {
            if(file.isDirectory()) {
                walked.addAll(walk(file));
                continue;
            }
            walked.add(file);
        }
        return walked;
    }
    public static String lazyName(File root, File file) {
        if(file == null) return "";
        String path = file.getAbsolutePath();
        String rootPath = root.getAbsolutePath();
        if(path.startsWith(rootPath)) {
            return path.substring(rootPath.length() + 1);
        }
        return name(path).replace("\\", "/").trim();
    }
    public static String name(String name) {
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex != -1) name = name.substring(0, dotIndex);
        return name;
    }
    public static String name(File file) {
        if(file == null) return "";
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0) {
            return name.substring(0, dotIndex);
        }
        return name;
    }
    public static String ext(File file) {
        if(file == null) return "";
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < name.length() - 1) {
            return name.substring(dotIndex + 1);
        }
        return "";
    }

}
