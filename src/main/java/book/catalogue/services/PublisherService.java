package book.catalogue.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.catalogue.database.Book;
import book.catalogue.database.Publisher;
import book.catalogue.repositories.PublisherRepository;
import book.catalogue.utils.Utils;

@Service
public class PublisherService extends GenericService<Publisher> {

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        super(publisherRepository);
        this.publisherRepository = publisherRepository;
    }

    public List<Book> getAllBooksOfPublisher(Long id) {
        return Optional.ofNullable(get(id)).map(Publisher::getBooks).orElse(null);
    }

    @Override
    public void addAllRecords(List<String[]> records) {
        Set<String> names = getAll().stream().map(Publisher::getName).collect(Collectors.toSet());
        publisherRepository.save(records.stream()
                .filter(array -> array.length == 1 && !names.contains(array[0]))
                .map(array -> new Publisher(array[0])).collect(Collectors.toList())
        );
    }

    @Override
    Object[] toArray(Publisher publisher) {
        return new Object[]{publisher.getName()};
    }

    @Override
    boolean canBeDeleted(Publisher publisher) {
        return Utils.nullOrEmpty(publisher.getBooks());
    }

}
