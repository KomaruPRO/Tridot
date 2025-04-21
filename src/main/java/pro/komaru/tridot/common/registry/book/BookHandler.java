package pro.komaru.tridot.common.registry.book;

import java.util.*;

public class BookHandler{
    public static List<Book> books = new ArrayList<>();

    public static void register(Book book) {
        books.add(book);
    }

    public static List<Book> getBooks() {
        return books;
    }
}