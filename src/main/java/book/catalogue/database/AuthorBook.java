package book.catalogue.database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@IdClass(AuthorBookPK.class)
@Entity
public class AuthorBook {
	@Id
	private Long authorId;
	@Id
	private Long bookId;

	@ManyToOne
	@JoinColumn(name = "authorId", updatable = false, insertable = false)
	private Author author;
	@ManyToOne
	@JoinColumn(name = "bookId", updatable = false, insertable = false)
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
