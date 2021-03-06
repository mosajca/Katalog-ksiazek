package book.catalogue.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.catalogue.database.Author;
import book.catalogue.database.AuthorBook;
import book.catalogue.database.Book;
import book.catalogue.repositories.AuthorRepository;
import book.catalogue.utils.Utils;

@Service
public class AuthorService extends GenericService<Author> {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        super(authorRepository);
        this.authorRepository = authorRepository;
    }

    public List<Book> getAllBooksOfAuthor(Long id) {
        return Optional.ofNullable(get(id)).map(Author::getAuthorBooks)
                .map(list -> list.stream().map(AuthorBook::getBook).collect(Collectors.toList())).orElse(null);
    }

    @Override
    public void addAllRecords(List<String[]> records) {
        authorRepository.save(records.stream()
                .filter(array -> array.length == 2)
                .map(array -> new Author(array[0], array[1])).collect(Collectors.toList())
        );
    }

    @Override
    Object[] toArray(Author author) {
        return new Object[]{author.getFirstName(), author.getLastName()};
    }

    @Override
    boolean canBeDeleted(Author author) {
        return Utils.nullOrEmpty(author.getAuthorBooks());
    }

}
