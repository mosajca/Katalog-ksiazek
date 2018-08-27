package book.catalogue.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import book.catalogue.database.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findFirstByName(String name);
}
