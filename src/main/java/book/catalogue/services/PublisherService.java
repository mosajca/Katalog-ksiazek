package book.catalogue.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.catalogue.database.Publisher;
import book.catalogue.repositories.PublisherRepository;

@Service
public class PublisherService {

    @Autowired
    private PublisherRepository publisherRepository;

    public List<Publisher> getAllPublishers() {
        List<Publisher> publishers = new ArrayList<>();
        publisherRepository.findAll().forEach(publishers::add);
        return publishers;
    }

    public List<Object[]> getAllPublishersRecords() {
        List<Object[]> records = new ArrayList<>();
        publisherRepository.findAll().forEach(p -> records.add(new Object[]{p.getName()}));
        return records;
    }

    public Publisher getPublisher(Long id) {
        return publisherRepository.findOne(id);
    }

    public void addPublisher(Publisher publisher) {
        publisherRepository.save(publisher);
    }

    public void addPublishers(List<String[]> records) {
        Set<String> names = getAllPublishers().stream().map(Publisher::getName).collect(Collectors.toSet());
        publisherRepository.save(records.stream()
                .filter(array -> array.length == 1 && !names.contains(array[0]))
                .map(array -> new Publisher(array[0])).collect(Collectors.toList())
        );
    }

    public void updatePublisher(Publisher publisher, Long id) {
        publisher.setId(id);
        publisherRepository.save(publisher);
    }

    public void deletePublisher(Long id) {
        publisherRepository.delete(id);
    }

}
