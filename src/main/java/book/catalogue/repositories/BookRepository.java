package book.catalogue.repositories;

import org.springframework.data.repository.CrudRepository;

import book.catalogue.database.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

}
