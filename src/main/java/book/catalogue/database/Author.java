package book.catalogue.database;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Author {

	@Id
	@GeneratedValue
	private Long id;

	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@OneToMany(mappedBy = "author")
	@JsonIgnore
	private List<AuthorBook> authorBooks;

	public Author() {
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
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<AuthorBook> getAuthorBooks() {
		return authorBooks;
	}

	public void setAuthorBooks(List<AuthorBook> authorBooks) {
		this.authorBooks = authorBooks;
	}

}
