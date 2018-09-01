package book.catalogue.controllers;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import book.catalogue.services.EmailService;

@RestController
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(@RequestBody String json) {
        boolean ok;
        try {
            ok = emailService.sendPDF(new ObjectMapper().readTree(json).path("email").asText(""));
        } catch (IOException e) {
            ok = false;
        }
        return new ResponseEntity<>(ok ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}
