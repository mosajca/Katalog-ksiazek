package book.catalogue.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.catalogue.database.Publisher;
import book.catalogue.repositories.PublisherRepository;

@Service
public class PublisherService extends GenericService<Publisher> {

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        super(publisherRepository);
        this.publisherRepository = publisherRepository;
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
    void setId(Publisher publisher, Long id) {
        publisher.setId(id);
    }

    @Override
    Object[] toArray(Publisher publisher) {
        return new Object[]{publisher.getName()};
    }

    @Override
    boolean canBeDeleted(Publisher publisher) {
        return nullOrEmpty(publisher.getBooks());
    }

    private boolean nullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

}
