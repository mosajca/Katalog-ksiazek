package book.catalogue.database;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import book.catalogue.utils.Utils;

@Entity
public class Book implements IdInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private Short year;

    private String description;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "publisherId")
    private Publisher publisher;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book", orphanRemoval = true)
    private List<AuthorBook> bookAuthors;

    public Book() {
    }

    public Book(String title, Short year, String description) {
        this.title = Utils.getOrDefaultIfEmpty(title, "<puste>");
        this.year = year;
        this.description = Utils.getOrDefaultIfEmpty(description, null);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Utils.getOrDefaultIfEmpty(title, "<puste>");
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = Utils.getOrDefaultIfEmpty(description, null);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public List<AuthorBook> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(List<AuthorBook> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    @Override
    public String toString() {
        return (id + ". " + format("tytuł", title)
                + Optional.of(authorsToString()).filter(a -> !a.isEmpty()).map(a -> format("autor", a)).orElse("")
                + Optional.ofNullable(publisher).map(p -> format("wydawnictwo", p.getName())).orElse("")
                + Optional.ofNullable(year).map(y -> format("rok", y.toString())).orElse("")
                + Optional.ofNullable(category).map(c -> format("kategoria", c.getName())).orElse("")
                + Optional.ofNullable(description).map(d -> format("opis", d)).orElse(""))
                .replaceFirst("..$", "");
    }

    private String authorsToString() {
        return Optional.ofNullable(bookAuthors).map(Collection::stream).orElseGet(Stream::empty)
                .map(a -> getAuthorFirstAndLastName(a.getAuthor())).collect(Collectors.joining(", "));
    }

    private String getAuthorFirstAndLastName(Author author) {
        return Optional.ofNullable(author.getFirstName()).map(f -> f + ' ').orElse("") + author.getLastName();
    }

    private String format(String first, String second) {
        return first.toUpperCase() + ": " + second + ", ";
    }

}
