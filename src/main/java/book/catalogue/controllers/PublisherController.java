package book.catalogue.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import book.catalogue.database.Book;
import book.catalogue.database.Publisher;
import book.catalogue.services.PublisherService;
import book.catalogue.utils.CSV;
import book.catalogue.utils.PDF;

@RestController
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @GetMapping("/publishers")
    public List<Publisher> getAllPublishers() {
        return publisherService.getAllPublishers();
    }

    @GetMapping(value = "/publishers/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getAllPublishersPDF(HttpServletResponse response) {
        response.addHeader("Content-Disposition", "inline; filename=\"publishers.pdf\"");
        return new PDF("Wydawnictwa", publisherService.getAllPublishers()).generate();
    }

    @GetMapping(value = "/publishers/csv", produces = "text/csv; charset=utf-8")
    public byte[] getAllPublishersCSV(HttpServletResponse response) {
        response.addHeader("Content-Disposition", "attachment; filename=\"publishers.csv\"");
        return CSV.toStringCSV(publisherService.getAllPublishersRecords()).getBytes(StandardCharsets.UTF_8);
    }

    @GetMapping("/publishers/{id}")
    public Publisher getPublisher(@PathVariable Long id) {
        return publisherService.getPublisher(id);
    }

    @GetMapping("/publishers/{id}/books")
    public List<Book> getAllBooks(@PathVariable Long id) {
        return publisherService.getPublisher(id).getBooks();
    }

    @PostMapping("/publishers")
    public void addPublisher(@RequestBody Publisher publisher) {
        publisherService.addPublisher(publisher);
    }

    @PostMapping("/publishers/csv")
    public void importCSV(@RequestParam MultipartFile file) throws IOException {
        publisherService.addPublishers(CSV.fromStringCSV(new String(file.getBytes(), StandardCharsets.UTF_8)));
    }

    @PutMapping("/publishers/{id}")
    public void updatePublisher(@RequestBody Publisher publisher, @PathVariable Long id) {
        publisherService.updatePublisher(publisher, id);
    }

    @DeleteMapping("/publishers/{id}")
    public void deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
    }

    @DeleteMapping("/publishers")
    public void deleteAllWithoutBooks() {
        publisherService.deleteAllWithoutBooks();
    }

}
