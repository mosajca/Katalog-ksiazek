package book.catalogue.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import book.catalogue.database.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Optional<Author> findFirstByFirstNameAndLastName(String firstName, String lastName);
}
