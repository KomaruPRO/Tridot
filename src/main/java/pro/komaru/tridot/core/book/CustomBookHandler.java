package pro.komaru.tridot.core.book;

import java.util.*;

public class CustomBookHandler{
    public static List<CustomBook> books = new ArrayList<>();

    public static void register(CustomBook book){
        books.add(book);
    }

    public static List<CustomBook> getBooks(){
        return books;
    }
}
