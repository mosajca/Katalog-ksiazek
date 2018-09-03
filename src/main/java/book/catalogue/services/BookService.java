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
import book.catalogue.utils.Utils;

@Service
public class BookService extends GenericService<Book> {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;

    @Autowired
    public BookService(AuthorRepository authorRepository, BookRepository bookRepository,
                       CategoryRepository categoryRepository, PublisherRepository publisherRepository) {
        super(bookRepository);
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public void add(Book book) {
        List<AuthorBook> bookAuthors = book.getBookAuthors();
        if (bookAuthors != null && !bookAuthors.isEmpty()) {
            book.setBookAuthors(null);
            Long bookId = bookRepository.save(book).getId();
            book.setBookAuthors(bookAuthors);
            update(book, bookId);
        } else {
            bookRepository.save(book);
        }
    }

    @Override
    public void update(Book book, Long id) {
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

    private String getAuthors(Book book) {
        return Optional.ofNullable(book.getBookAuthors()).filter(l -> !l.isEmpty())
                .map(l -> l.stream().map(AuthorBook::getAuthor)
                        .map(a -> new Object[]{a.getFirstName(), a.getLastName()}).collect(Collectors.toList())
                ).map(CSV::toStringCSV).orElse(null);
    }

    private Book arrayToBook(String[] array) {
        Book book = new Book(array[0], Utils.parseShort(array[2]), array[5]);
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
            ab.setAuthorId(authorRepository
                    .findFirstByFirstNameAndLastName(Utils.getOrDefaultIfEmpty(array[0], null), array[1])
                    .map(Author::getId).orElseGet(() -> authorRepository.save(new Author(array[0], array[1])).getId()));
            list.add(ab);
        }
        return list;
    }

    @Override
    public void addAllRecords(List<String[]> records) {
        records.stream().filter(array -> array.length == 6).map(this::arrayToBook).forEach(this::add);
    }

    @Override
    Object[] toArray(Book book) {
        return new Object[]{book.getTitle(), getAuthors(book), book.getYear(),
                Optional.ofNullable(book.getPublisher()).map(Publisher::getName).orElse(null),
                Optional.ofNullable(book.getCategory()).map(Category::getName).orElse(null),
                book.getDescription()
        };
    }

    @Override
    boolean canBeDeleted(Book book) {
        return true;
    }

}
