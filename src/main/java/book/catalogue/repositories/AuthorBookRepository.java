package book.catalogue.repositories;

import org.springframework.data.repository.CrudRepository;

import book.catalogue.database.AuthorBook;
import book.catalogue.database.AuthorBookPK;

public interface AuthorBookRepository extends CrudRepository<AuthorBook, AuthorBookPK> {

}
