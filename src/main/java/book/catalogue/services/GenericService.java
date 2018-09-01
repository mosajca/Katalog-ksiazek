package book.catalogue.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.repository.CrudRepository;

public abstract class GenericService<T> {

    private CrudRepository<T, Long> crudRepository;

    GenericService(CrudRepository<T, Long> crudRepository) {
        this.crudRepository = crudRepository;
    }

    public T get(Long id) {
        return crudRepository.findOne(id);
    }

    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        crudRepository.findAll().forEach(list::add);
        return list;
    }

    public List<Object[]> getAllRecords() {
        return getAll().stream().map(this::toArray).collect(Collectors.toList());
    }

    public void add(T t) {
        crudRepository.save(t);
    }

    public void update(T t, Long id) {
        setId(t, id);
        crudRepository.save(t);
    }

    public void delete(Long id) {
        crudRepository.delete(id);
    }

    public void deleteAllThatCanBeDeleted() {
        crudRepository.delete(getAll().stream().filter(this::canBeDeleted).collect(Collectors.toList()));
    }

    public abstract void addAllRecords(List<String[]> records);

    abstract void setId(T t, Long id);

    abstract Object[] toArray(T t);

    abstract boolean canBeDeleted(T t);

}
