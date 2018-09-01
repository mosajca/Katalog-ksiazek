package book.catalogue.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.catalogue.database.Category;
import book.catalogue.repositories.CategoryRepository;

@Service
public class CategoryService extends GenericService<Category> {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void addAllRecords(List<String[]> records) {
        Set<String> names = getAll().stream().map(Category::getName).collect(Collectors.toSet());
        categoryRepository.save(records.stream()
                .filter(array -> array.length == 1 && !names.contains(array[0]))
                .map(array -> new Category(array[0])).collect(Collectors.toList())
        );
    }

    @Override
    void setId(Category category, Long id) {
        category.setId(id);
    }

    @Override
    Object[] toArray(Category category) {
        return new Object[]{category.getName()};
    }

    @Override
    boolean canBeDeleted(Category category) {
        return nullOrEmpty(category.getBooks());
    }

    private boolean nullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

}
