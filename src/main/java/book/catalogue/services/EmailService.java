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

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private BookService bookService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PublisherService publisherService;

    public boolean sendPDF(String to) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Pliki PDF");
            helper.setText("Pliki PDF: autorzy, książki, kategorie, wydawnictwa.");
            helper.addAttachment("autorzy.pdf", getResourcePDF("Autorzy", authorService.getAllAuthors()));
            helper.addAttachment("książki.pdf", getResourcePDF("Książki", bookService.getAllBooks()));
            helper.addAttachment("kategorie.pdf", getResourcePDF("Kategorie", categoryService.getAllCategories()));
            helper.addAttachment("wydawnictwa.pdf", getResourcePDF("Wydawnictwa", publisherService.getAllPublishers()));
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
