package book.catalogue.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import book.catalogue.database.Book;
import book.catalogue.database.Publisher;
import book.catalogue.services.PublisherService;

@RestController
@RequestMapping("/publishers")
public class PublisherController extends GenericController<Publisher> {

    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        super(publisherService, "publishers", "Wydawnictwa");
        this.publisherService = publisherService;
    }

    @GetMapping("{id}/books")
    public List<Book> getAllBooks(@PathVariable Long id) {
        return Optional.ofNullable(publisherService.get(id)).map(Publisher::getBooks).orElse(null);
    }

}
