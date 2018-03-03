package book.catalogue.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import book.catalogue.services.AuthorBookService;

@RestController
public class AuthorBookController {

	@Autowired
	private AuthorBookService authorBookService;

}
