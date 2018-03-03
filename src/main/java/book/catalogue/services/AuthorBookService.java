package book.catalogue.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.catalogue.database.AuthorBook;
import book.catalogue.database.AuthorBookPK;
import book.catalogue.repositories.AuthorBookRepository;

@Service
public class AuthorBookService {

	@Autowired
	private AuthorBookRepository authorBookRepository;

	public List<AuthorBook> getAllAuthorBooks() {
		List<AuthorBook> authorBooks = new ArrayList<>();
		authorBookRepository.findAll().forEach(authorBooks::add);
		return authorBooks;
	}

	public AuthorBook getAuthorBook(AuthorBookPK id) {
		return authorBookRepository.findOne(id);
	}

	public void addAuthorBook(AuthorBook authorBook) {
		authorBookRepository.save(authorBook);
	}

	public void updateAuthorBook(AuthorBook authorBook) {
		authorBookRepository.save(authorBook);
	}

	public void deleteAuthorBook(AuthorBookPK id) {
		authorBookRepository.delete(id);
	}

}
