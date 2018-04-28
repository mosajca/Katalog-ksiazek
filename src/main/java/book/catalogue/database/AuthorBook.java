package book.catalogue.database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@IdClass(AuthorBookPK.class)
@Entity
public class AuthorBook {

	@Id
	@JsonIgnore
	private Long authorId;

	@Id
	@JsonIgnore
	private Long bookId;

	@ManyToOne
	@JoinColumn(name = "authorId", updatable = false, insertable = false)
	private Author author;

	@ManyToOne
	@JoinColumn(name = "bookId", updatable = false, insertable = false)
	@JsonIgnore
	private Book book;

	public AuthorBook() {
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

}
