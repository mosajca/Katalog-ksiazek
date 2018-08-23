package book.catalogue.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import book.catalogue.database.Book;
import book.catalogue.database.Category;
import book.catalogue.services.CategoryService;
import book.catalogue.utils.CSV;
import book.catalogue.utils.PDF;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping(value = "/categories/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getAllCategoriesPDF(HttpServletResponse response) {
        response.addHeader("Content-Disposition", "inline; filename=\"categories.pdf\"");
        return new PDF("Kategorie", categoryService.getAllCategories()).generate();
    }

    @GetMapping(value = "/categories/csv", produces = "text/csv; charset=utf-8")
    public byte[] getAllCategoriesCSV(HttpServletResponse response) {
        response.addHeader("Content-Disposition", "attachment; filename=\"categories.csv\"");
        return CSV.toByteArrayCSV(categoryService.getAllCategoriesRecords());
    }

    @GetMapping("/categories/{id}")
    public Category getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    @GetMapping("/categories/{id}/books")
    public List<Book> getAllBooks(@PathVariable Long id) {
        return categoryService.getCategory(id).getBooks();
    }

    @PostMapping("/categories")
    public void addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
    }

    @PutMapping("/categories/{id}")
    public void updateCategory(@RequestBody Category category, @PathVariable Long id) {
        categoryService.updateCategory(category, id);
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

}
