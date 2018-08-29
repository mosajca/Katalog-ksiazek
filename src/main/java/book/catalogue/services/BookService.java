package book.catalogue.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.catalogue.database.Author;
import book.catalogue.database.AuthorBook;
import book.catalogue.database.Book;
import book.catalogue.database.Category;
import book.catalogue.database.Publisher;
import book.catalogue.repositories.AuthorRepository;
import book.catalogue.repositories.BookRepository;
import book.catalogue.repositories.CategoryRepository;
import book.catalogue.repositories.PublisherRepository;
import book.catalogue.utils.CSV;

@Service
public class BookService {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PublisherRepository publisherRepository;

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        return books;
    }

    public List<Object[]> getAllBooksRecords() {
        List<Object[]> records = new ArrayList<>();
        bookRepository.findAll().forEach(b -> records.add(new Object[]{b.getTitle(), getAuthors(b), b.getYear(),
                Optional.ofNullable(b.getPublisher()).map(Publisher::getName).orElse(null),
                Optional.ofNullable(b.getCategory()).map(Category::getName).orElse(null), b.getDescription()
        }));
        return records;
    }

    public Book getBook(Long id) {
        return bookRepository.findOne(id);
    }

    public void addBook(Book book) {
        List<AuthorBook> bookAuthors = book.getBookAuthors();
        if (bookAuthors != null && !bookAuthors.isEmpty()) {
            book.setBookAuthors(null);
            Long bookId = bookRepository.save(book).getId();
            book.setBookAuthors(bookAuthors);
            updateBook(book, bookId);
        } else {
            bookRepository.save(book);
        }
    }

    public void updateBook(Book book, Long id) {
        book.setId(id);
        if (book.getBookAuthors() == null) {
            book.setBookAuthors(Collections.emptyList());
        }
        for (AuthorBook authorBook : book.getBookAuthors()) {
            authorBook.setBookId(id);
        }
        book.setBookAuthors(book.getBookAuthors().stream().distinct().collect(Collectors.toList()));
        bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.delete(id);
    }

    public void deleteAll() {
        bookRepository.deleteAll();
    }

    private String getAuthors(Book book) {
        return Optional.ofNullable(book.getBookAuthors()).filter(l -> !l.isEmpty())
                .map(l -> l.stream().map(AuthorBook::getAuthor)
                        .map(a -> new Object[]{a.getFirstName(), a.getLastName()}).collect(Collectors.toList())
                ).map(CSV::toStringCSV).orElse(null);
    }

    public void addBooks(List<String[]> records) {
        records.stream().filter(array -> array.length == 6).map(this::arrayToBook).forEach(this::addBook);
    }

    private Book arrayToBook(String[] array) {
        Book book = new Book(array[0], parseShort(array[2]), array[5]);
        if (!array[4].isEmpty()) {
            book.setCategory(categoryRepository.findFirstByName(array[4])
                    .orElseGet(() -> categoryRepository.save(new Category(array[4]))));
        }
        if (!array[3].isEmpty()) {
            book.setPublisher(publisherRepository.findFirstByName(array[3])
                    .orElseGet(() -> publisherRepository.save(new Publisher(array[3]))));
        }
        book.setBookAuthors(toAuthorBookList(CSV.fromStringCSV(array[1])));
        return book;
    }

    private List<AuthorBook> toAuthorBookList(List<String[]> authors) {
        List<AuthorBook> list = new ArrayList<>();
        for (String[] array : authors) {
            AuthorBook ab = new AuthorBook();
            ab.setAuthorId(authorRepository.findFirstByFirstNameAndLastName(nullIfEmpty(array[0]), array[1])
                    .map(Author::getId).orElseGet(() -> authorRepository.save(new Author(array[0], array[1])).getId()));
            list.add(ab);
        }
        return list;
    }

    private Short parseShort(String string) {
        try {
            return Short.parseShort(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String nullIfEmpty(String string) {
        return (string != null && string.isEmpty()) ? null : string;
    }

}
