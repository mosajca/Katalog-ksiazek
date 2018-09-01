package book.catalogue.services;

import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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

    public boolean sendPDF(String to) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Pliki PDF");
            helper.setText("Pliki PDF: autorzy, książki, kategorie, wydawnictwa.");
            helper.addAttachment("autorzy.pdf", getResourcePDF("Autorzy", authorService.getAll()));
            helper.addAttachment("książki.pdf", getResourcePDF("Książki", bookService.getAll()));
            helper.addAttachment("kategorie.pdf", getResourcePDF("Kategorie", categoryService.getAll()));
            helper.addAttachment("wydawnictwa.pdf", getResourcePDF("Wydawnictwa", publisherService.getAll()));
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private ByteArrayResource getResourcePDF(String title, List<?> objects) {
        return new ByteArrayResource(new PDF(title, objects).generate());
    }

}
