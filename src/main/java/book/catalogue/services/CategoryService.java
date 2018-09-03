package book.catalogue.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.catalogue.database.Book;
import book.catalogue.database.Category;
import book.catalogue.repositories.CategoryRepository;
import book.catalogue.utils.Utils;

@Service
public class CategoryService extends GenericService<Category> {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }

    public List<Book> getAllBooksOfCategory(Long id) {
        return Optional.ofNullable(get(id)).map(Category::getBooks).orElse(null);
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
    Object[] toArray(Category category) {
        return new Object[]{category.getName()};
    }

    @Override
    boolean canBeDeleted(Category category) {
        return Utils.nullOrEmpty(category.getBooks());
    }

}
