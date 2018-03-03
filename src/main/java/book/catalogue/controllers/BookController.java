package book.catalogue.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import book.catalogue.services.BookService;

@RestController
public class BookController {

	@Autowired
	private BookService bookService;

}
