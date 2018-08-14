package book.catalogue.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.catalogue.database.Author;
import book.catalogue.repositories.AuthorRepository;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        authorRepository.findAll().forEach(authors::add);
        return authors;
    }

    public Author getAuthor(Long id) {
        return authorRepository.findOne(id);
    }

    public void addAuthor(Author author) {
        setNullForFirstNameIfEmpty(author);
        authorRepository.save(author);
    }

    public void updateAuthor(Author author, Long id) {
        author.setId(id);
        setNullForFirstNameIfEmpty(author);
        authorRepository.save(author);
    }

    public void deleteAuthor(Long id) {
        authorRepository.delete(id);
    }

    private void setNullForFirstNameIfEmpty(Author author) {
        String firstName = author.getFirstName();
        if (firstName != null && firstName.isEmpty()) {
            author.setFirstName(null);
        }
    }

}
