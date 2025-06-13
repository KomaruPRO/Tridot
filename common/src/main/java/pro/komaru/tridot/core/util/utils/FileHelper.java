package pro.komaru.tridot.core.util.utils;

import pro.komaru.tridot.core.struct.Seq;
import pro.komaru.tridot.core.struct.func.Cons3;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileHelper {
    private static final FileHelper instance = new FileHelper();
    public static FileHelper get() {
        return instance;
    }
    public @Nullable InputStreamReader reader(File f)
    {
        if(!f.exists()) return null;
        return new InputStreamReader(inputStream(f), StandardCharsets.UTF_8);
    }
    public @Nullable OutputStreamWriter writer(File f)
    {
        if(!f.exists()) return null;
        return new OutputStreamWriter(outputStream(f), StandardCharsets.UTF_8);
    }
    public @Nullable FileInputStream inputStream(File f)
    {
        if(!f.exists()) return null;
        try {
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
    public @Nullable FileOutputStream outputStream(File f)
    {
        if(!f.exists()) return null;
        try {
            return new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public @Nullable String read(File f) {
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
    public boolean write(File f, String content) {
        try (OutputStreamWriter writer = writer(f)) {
            if (writer == null) return false;
            writer.write(content);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Seq<File> dirs(File dir) {
        File[] dirs = dir.listFiles(File::isDirectory);
        return Seq.with(dirs);
    }
    public Seq<File> walk(File dir, Cons3<File, String, String> callback) {
        if(!dir.isDirectory()) return Seq.empty();
        Seq<File> walked = walk(dir);
        for (File file : walked) {
            callback.get(file, lazyName(dir, file), ext(file));
        }
        return walked;
    }
    public Seq<File> walk(File dir) {
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
    public String lazyName(File root, File file) {
        if(file == null) return "";
        String path = file.getAbsolutePath();
        String rootPath = root.getAbsolutePath();
        if(path.startsWith(rootPath)) {
            return path.substring(rootPath.length() + 1);
        }
        return name(path).replace("\\", "/").trim();
    }
    public String name(String name) {
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex != -1) name = name.substring(0, dotIndex);
        return name;
    }
    public String name(File file) {
        if(file == null) return "";
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0) {
            return name.substring(0, dotIndex);
        }
        return name;
    }
    public String ext(File file) {
        if(file == null) return "";
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < name.length() - 1) {
            return name.substring(dotIndex + 1);
        }
        return "";
    }

}
