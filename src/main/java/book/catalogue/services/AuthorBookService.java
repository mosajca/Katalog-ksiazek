package book.catalogue.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.catalogue.repositories.AuthorBookRepository;

@Service
public class AuthorBookService {

	@Autowired
	private AuthorBookRepository authorBookRepository;

}
