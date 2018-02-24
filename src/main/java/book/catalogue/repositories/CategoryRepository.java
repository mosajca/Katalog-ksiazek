package book.catalogue.repositories;

import org.springframework.data.repository.CrudRepository;

import book.catalogue.database.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {

}
