package book.catalogue.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import book.catalogue.database.Publisher;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {
    Optional<Publisher> findFirstByName(String name);
}
