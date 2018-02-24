package book.catalogue.repositories;

import org.springframework.data.repository.CrudRepository;

import book.catalogue.database.Publisher;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {

}
