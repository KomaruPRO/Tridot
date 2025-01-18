package github.iri.tridot.utilities;

public class OS {
    public static long before = 0;
    public static boolean mark = false;
    public static long mem() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
    public static long memMark() {
        if(!mark) {
            before = mem();
            mark = true;
        }
        else {
            long used = mem()-before;
            mark = false;
            return used;
        }
        return 0;
    }
}
