package book.catalogue.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.catalogue.database.Category;
import book.catalogue.repositories.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        categoryRepository.findAll().forEach(categories::add);
        return categories;
    }

    public List<Object[]> getAllCategoriesRecords() {
        List<Object[]> records = new ArrayList<>();
        categoryRepository.findAll().forEach(c -> records.add(new Object[]{c.getName()}));
        return records;
    }

    public Category getCategory(Long id) {
        return categoryRepository.findOne(id);
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public void updateCategory(Category category, Long id) {
        category.setId(id);
        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.delete(id);
    }

}
