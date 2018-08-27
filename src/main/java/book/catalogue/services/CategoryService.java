package book.catalogue.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public void addCategories(List<String[]> records) {
        Set<String> names = getAllCategories().stream().map(Category::getName).collect(Collectors.toSet());
        categoryRepository.save(records.stream()
                .filter(array -> array.length == 1 && !names.contains(array[0]))
                .map(array -> new Category(array[0])).collect(Collectors.toList())
        );
    }

    public void updateCategory(Category category, Long id) {
        category.setId(id);
        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.delete(id);
    }

}
