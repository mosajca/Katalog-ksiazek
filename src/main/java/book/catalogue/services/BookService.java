package book.catalogue.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.catalogue.repositories.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

}
