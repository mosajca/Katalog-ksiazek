package book.catalogue.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import book.catalogue.services.CategoryService;

@RestController
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

}
