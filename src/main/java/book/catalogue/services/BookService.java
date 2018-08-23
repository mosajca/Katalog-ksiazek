package book.catalogue.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.catalogue.database.AuthorBook;
import book.catalogue.database.Book;
import book.catalogue.database.Category;
import book.catalogue.database.Publisher;
import book.catalogue.repositories.BookRepository;
import book.catalogue.utils.CSV;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

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
            setNullForDescriptionIfEmpty(book);
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
        setNullForDescriptionIfEmpty(book);
        bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.delete(id);
    }

    private void setNullForDescriptionIfEmpty(Book book) {
        String description = book.getDescription();
        if (description != null && description.isEmpty()) {
            book.setDescription(null);
        }
    }

    private String getAuthors(Book book) {
        return Optional.ofNullable(book.getBookAuthors()).filter(l -> !l.isEmpty())
                .map(l -> l.stream().map(AuthorBook::getAuthor)
                        .map(a -> new Object[]{a.getFirstName(), a.getLastName()}).collect(Collectors.toList())
                ).map(CSV::toStringCSV).orElse(null);
    }

}
