package book.catalogue.database;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private List<AuthorBook> authorBooks;

    public Author() {
    }

    public Author(String firstName, String lastName) {
        this.firstName = changeIfEmpty(firstName, null);
        this.lastName = changeIfEmpty(lastName, "<puste>");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = changeIfEmpty(firstName, null);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = changeIfEmpty(lastName, "<puste>");
    }

    public List<AuthorBook> getAuthorBooks() {
        return authorBooks;
    }

    public void setAuthorBooks(List<AuthorBook> authorBooks) {
        this.authorBooks = authorBooks;
    }

    @Override
    public String toString() {
        return id + ". " + (firstName == null ? "" : firstName + ' ') + lastName;
    }

    private String changeIfEmpty(String name, String string) {
        return (name != null && name.isEmpty()) ? string : name;
    }

}
