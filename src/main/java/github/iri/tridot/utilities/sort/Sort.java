package github.iri.tridot.utilities.sort;

import github.iri.tridot.utilities.*;
import github.iri.tridot.utilities.struct.*;

import java.util.*;

public class Sort{
    private static ThreadLocal<Sort> instance = Threads.local(Sort::new);

    private TimSort timSort;
    private ComparableTimSort comparableTimSort;

    /** Returns a Sort instance for convenience. Multiple threads must not use this instance at the same time. */
    public static Sort instance(){
        return instance.get();
    }

    public <T> void sort(Seq<T> a){
        if(comparableTimSort == null) comparableTimSort = new ComparableTimSort();
        comparableTimSort.doSort(a.items, 0, a.size);
    }

    public <T> void sort(T[] a){
        if(comparableTimSort == null) comparableTimSort = new ComparableTimSort();
        comparableTimSort.doSort(a, 0, a.length);
    }

    public <T> void sort(T[] a, int fromIndex, int toIndex){
        if(comparableTimSort == null) comparableTimSort = new ComparableTimSort();
        comparableTimSort.doSort(a, fromIndex, toIndex);
    }

    public <T> void sort(Seq<T> a, Comparator<? super T> c){
        if(timSort == null) timSort = new TimSort();
        timSort.doSort(a.items, c, 0, a.size);
    }

    public <T> void sort(T[] a, Comparator<? super T> c){
        if(timSort == null) timSort = new TimSort();
        timSort.doSort(a, c, 0, a.length);
    }

    public <T> void sort(T[] a, Comparator<? super T> c, int fromIndex, int toIndex){
        if(timSort == null) timSort = new TimSort();
        timSort.doSort(a, c, fromIndex, toIndex);
    }
}