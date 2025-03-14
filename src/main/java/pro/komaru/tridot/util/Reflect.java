package pro.komaru.tridot.util;

import pro.komaru.tridot.core.struct.data.Seq;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;

public class Reflect {
    public static Seq<Class<?>> findClasses(String packageName, String simpleClassName) throws ClassNotFoundException, IOException {
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = ClassLoader.getSystemClassLoader().getResources(path);
        Seq<Class<?>> matchingClasses = Seq.with();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();
            if ("file".equals(protocol)) {
                File directory = new File(resource.getFile());
                matchingClasses.addAll(findClassesInDirectory(directory, packageName, simpleClassName));
            } else if ("jar".equals(protocol)) {
                String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                matchingClasses.addAll(findClassesInJar(jarPath, packageName, simpleClassName));
            }
        }
        return matchingClasses;
    }

    private static Seq<Class<?>> findClassesInDirectory(File directory, String packageName, String simpleClassName) throws ClassNotFoundException {
        Seq<Class<?>> classes = Seq.with();
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    classes.addAll(findClassesInDirectory(file, packageName + "." + file.getName(), simpleClassName));
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().replace(".class", "");
                    Class<?> clazz = Class.forName(className);
                    if (clazz.getSimpleName().equals(simpleClassName)) {
                        classes.add(clazz);
                    }
                }
            }
        }
        return classes;
    }

    private static Seq<Class<?>> findClassesInJar(String jarPath, String packageName, String simpleClassName) throws ClassNotFoundException, IOException {
        Seq<Class<?>> classes = Seq.with();
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            String path = packageName.replace('.', '/');
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith(path) && entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace('/', '.').replace(".class", "");
                    Class<?> clazz = Class.forName(className);
                    if (clazz.getSimpleName().equals(simpleClassName)) {
                        classes.add(clazz);
                    }
                }
            }
        }
        return classes;
    }
}
