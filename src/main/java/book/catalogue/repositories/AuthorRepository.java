package book.catalogue.repositories;

import org.springframework.data.repository.CrudRepository;

import book.catalogue.database.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {

}
