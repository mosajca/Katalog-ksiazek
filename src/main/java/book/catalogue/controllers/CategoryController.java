package book.catalogue.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import book.catalogue.database.Book;
import book.catalogue.database.Category;
import book.catalogue.services.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController extends GenericController<Category> {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        super(categoryService, "categories", "Kategorie");
        this.categoryService = categoryService;
    }

    @GetMapping("{id}/books")
    public List<Book> getAllBooks(@PathVariable Long id) {
        return categoryService.getAllBooksOfCategory(id);
    }

}
