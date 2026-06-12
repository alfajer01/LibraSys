package librasys.service;

import java.util.ArrayList;
import java.util.List;
import librasys.model.Book;

/**
 *
 * @author AmmarPasifiky
 */
public class BookService {

    private List<Book> books;

    public BookService() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        books.add(book);
    }

    public void updateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }

        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookId().equals(book.getBookId())) {
                books.set(i, book);
                return;
            }
        }

        throw new IllegalArgumentException("Book not found: " + book.getBookId());
    }

    public void removeBook(String bookId) {
        if (isBlank(bookId)) {
            throw new IllegalArgumentException("Book ID cannot be empty.");
        }

        boolean removed = books.removeIf(book -> book.getBookId().equals(bookId));
        if (!removed) {
            throw new IllegalArgumentException("Book not found: " + bookId);
        }
    }

    public List<Book> searchBook(String keyword) {
        if (keyword == null) {
            keyword = "";
        }

        String lowerKeyword = keyword.toLowerCase();
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getBookId().toLowerCase().contains(lowerKeyword)
                    || book.getTitle().toLowerCase().contains(lowerKeyword)
                    || book.getAuthor().toLowerCase().contains(lowerKeyword)) {
                result.add(book);
            }
        }
        return result;
    }

    public Book findBookById(String bookId) {
        if (isBlank(bookId)) {
            return null;
        }

        for (Book book : books) {
            if (book.getBookId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
