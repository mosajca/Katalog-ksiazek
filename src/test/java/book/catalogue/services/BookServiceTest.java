package book.catalogue.services;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import book.catalogue.database.Author;
import book.catalogue.database.AuthorBook;
import book.catalogue.database.Book;
import book.catalogue.repositories.AuthorRepository;
import book.catalogue.repositories.BookRepository;
import book.catalogue.repositories.CategoryRepository;
import book.catalogue.repositories.PublisherRepository;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    private Book book;
    private BookService bookService;

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private PublisherRepository publisherRepository;

    @Before
    public void setUp() throws Exception {
        book = new Book("Title", (short) 2018, "");
        bookService = new BookService(authorRepository, bookRepository, categoryRepository, publisherRepository);
        when(bookRepository.save(any(Book.class))).then(i -> i.getArguments()[0]);
    }

    @Test
    public void testAddWithoutAuthors() {
        bookService.add(book);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testAddWithEmptyAuthors() {
        book.setBookAuthors(Collections.emptyList());
        bookService.add(book);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testAddWithAuthor() {
        AuthorBook authorBook = new AuthorBook();
        authorBook.setAuthor(new Author("FirstName", "LastName"));
        book.setBookAuthors(Collections.singletonList(authorBook));
        bookService.add(book);
        verify(bookRepository, times(2)).save(any(Book.class));
    }

}
