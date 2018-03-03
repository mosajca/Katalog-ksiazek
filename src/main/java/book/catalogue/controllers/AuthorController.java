package book.catalogue.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import book.catalogue.services.AuthorService;

@RestController
public class AuthorController {

	@Autowired
	private AuthorService authorService;

}
