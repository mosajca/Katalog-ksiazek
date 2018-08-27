package book.catalogue.controllers;

import java.io.IOException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DataAccessException.class, IOException.class})
    public ResponseEntity<?> handle() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
