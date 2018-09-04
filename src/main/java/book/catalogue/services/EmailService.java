package book.catalogue.services;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import book.catalogue.utils.CSV;
import book.catalogue.utils.PDF;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final AuthorService authorService;
    private final BookService bookService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, AuthorService authorService, BookService bookService,
                        CategoryService categoryService, PublisherService publisherService) {
        this.javaMailSender = javaMailSender;
        this.authorService = authorService;
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.publisherService = publisherService;
    }

    public boolean sendEmail(String to) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Pliki PDF i CSV");
            helper.setText("Pliki PDF i CSV: autorzy, książki, kategorie, wydawnictwa.");

            helper.addAttachment("authors.pdf", getResourcePDF("Autorzy", authorService.getAll()));
            helper.addAttachment("books.pdf", getResourcePDF("Książki", bookService.getAll()));
            helper.addAttachment("categories.pdf", getResourcePDF("Kategorie", categoryService.getAll()));
            helper.addAttachment("publishers.pdf", getResourcePDF("Wydawnictwa", publisherService.getAll()));

            helper.addAttachment("authors.csv", getResourceCSV(authorService.getAllRecords()));
            helper.addAttachment("books.csv", getResourceCSV(bookService.getAllRecords()));
            helper.addAttachment("categories.csv", getResourceCSV(categoryService.getAllRecords()));
            helper.addAttachment("publishers.csv", getResourceCSV(publisherService.getAllRecords()));

            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private ByteArrayResource getResourcePDF(String title, List<?> objects) {
        return new ByteArrayResource(new PDF(title, objects).generate());
    }

    private ByteArrayResource getResourceCSV(List<Object[]> records) {
        return new ByteArrayResource(CSV.toStringCSV(records).getBytes(StandardCharsets.UTF_8));
    }

}
