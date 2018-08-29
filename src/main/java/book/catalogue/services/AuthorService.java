package book.catalogue.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.catalogue.database.Author;
import book.catalogue.database.AuthorBook;
import book.catalogue.database.Book;
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

    public List<Object[]> getAllAuthorsRecords() {
        List<Object[]> records = new ArrayList<>();
        authorRepository.findAll().forEach(a -> records.add(new Object[]{a.getFirstName(), a.getLastName()}));
        return records;
    }

    public Author getAuthor(Long id) {
        return authorRepository.findOne(id);
    }

    public List<Book> getAllBooksOfAuthor(Long id) {
        return Optional.ofNullable(authorRepository.findOne(id)).map(Author::getAuthorBooks)
                .map(Collection::stream).orElseGet(Stream::empty)
                .map(AuthorBook::getBook).collect(Collectors.toList());
    }

    public void addAuthor(Author author) {
        authorRepository.save(author);
    }

    public void addAuthors(List<String[]> records) {
        authorRepository.save(records.stream()
                .filter(array -> array.length == 2)
                .map(array -> new Author(array[0], array[1])).collect(Collectors.toList())
        );
    }

    public void updateAuthor(Author author, Long id) {
        author.setId(id);
        authorRepository.save(author);
    }

    public void deleteAuthor(Long id) {
        authorRepository.delete(id);
    }

    public void deleteAllWithoutBooks() {
        authorRepository.delete(getAllAuthors().stream()
                .filter(author -> nullOrEmpty(author.getAuthorBooks()))
                .collect(Collectors.toList()));
    }

    private boolean nullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

}
