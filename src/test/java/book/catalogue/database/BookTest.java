package book.catalogue.database;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookTest {

    private Book book;

    @Before
    public void setUp() throws Exception {
        book = new Book();
        book.setId(1L);
        book.setTitle("Title");
    }

    @Test
    public void testToStringIdTitle() {
        assertEquals("1. TYTUŁ: Title", book.toString());
    }

    @Test
    public void testToStringIdTitleEmptyAuthors() {
        book.setBookAuthors(getAuthorBookList());
        assertEquals("1. TYTUŁ: Title", book.toString());
    }

    @Test
    public void testToStringIdTitleOneAuthor() {
        book.setBookAuthors(getAuthorBookList(new Author("FirstName", "LastName")));
        assertEquals("1. TYTUŁ: Title, AUTOR: FirstName LastName", book.toString());
    }

    @Test
    public void testToStringIdTitleOneAuthorWithNullFirstName() {
        book.setBookAuthors(getAuthorBookList(new Author(null, "LastName")));
        assertEquals("1. TYTUŁ: Title, AUTOR: LastName", book.toString());
    }

    @Test
    public void testToStringIdTitleTwoAuthors() {
        book.setBookAuthors(getAuthorBookList(new Author(null, "LastName"), new Author("FirstName", "LastName")));
        assertEquals("1. TYTUŁ: Title, AUTOR: LastName, FirstName LastName", book.toString());
    }

    @Test
    public void testToStringIdTitlePublisher() {
        book.setPublisher(new Publisher("Publisher"));
        assertEquals("1. TYTUŁ: Title, WYDAWNICTWO: Publisher", book.toString());
    }

    @Test
    public void testToStringIdTitleYear() {
        book.setYear((short) 2018);
        assertEquals("1. TYTUŁ: Title, ROK: 2018", book.toString());
    }

    @Test
    public void testToStringIdTitleCategory() {
        book.setCategory(new Category("Category"));
        assertEquals("1. TYTUŁ: Title, KATEGORIA: Category", book.toString());
    }

    @Test
    public void testToStringIdTitleDescription() {
        book.setDescription("Description");
        assertEquals("1. TYTUŁ: Title, OPIS: Description", book.toString());
    }

    @Test
    public void testToStringIdTitlePublisherCategory() {
        book.setPublisher(new Publisher("Publisher"));
        book.setCategory(new Category("Category"));
        assertEquals("1. TYTUŁ: Title, WYDAWNICTWO: Publisher, KATEGORIA: Category", book.toString());
    }

    @Test
    public void testToStringAll() {
        book.setBookAuthors(getAuthorBookList(new Author("FirstName", "LastName")));
        book.setPublisher(new Publisher("Publisher"));
        book.setYear((short) 2018);
        book.setCategory(new Category("Category"));
        book.setDescription("Description");
        assertEquals("1. TYTUŁ: Title, AUTOR: FirstName LastName, " +
                "WYDAWNICTWO: Publisher, ROK: 2018, KATEGORIA: Category, OPIS: Description", book.toString());
    }

    private List<AuthorBook> getAuthorBookList(Author... authors) {
        List<AuthorBook> list = new ArrayList<>();
        for (Author author : authors) {
            AuthorBook ab = new AuthorBook();
            ab.setAuthor(author);
            list.add(ab);
        }
        return list;
    }

}
