package book.catalogue.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import book.catalogue.services.GenericService;
import book.catalogue.utils.CSV;
import book.catalogue.utils.PDF;

public abstract class GenericController<T> {

    private GenericService<T> genericService;
    private String name;
    private String title;

    GenericController(GenericService<T> genericService, String name, String title) {
        this.genericService = genericService;
        this.name = name;
        this.title = title;
    }

    @GetMapping("{id}")
    public T get(@PathVariable Long id) {
        return genericService.get(id);
    }

    @GetMapping
    public List<T> getAll() {
        return genericService.getAll();
    }

    @GetMapping(value = "pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPDF(HttpServletResponse response) {
        response.addHeader("Content-Disposition", "inline; filename=\"" + name + ".pdf\"");
        return new PDF(title, genericService.getAll()).generate();
    }

    @GetMapping(value = "csv", produces = "text/csv; charset=utf-8")
    public byte[] getCSV(HttpServletResponse response) {
        response.addHeader("Content-Disposition", "attachment; filename=\"" + name + ".csv\"");
        return CSV.toStringCSV(genericService.getAllRecords()).getBytes(StandardCharsets.UTF_8);
    }

    @PostMapping
    public void add(@RequestBody T t) {
        genericService.add(t);
    }

    @PostMapping("csv")
    public void importCSV(@RequestParam MultipartFile file) throws IOException {
        genericService.addAllRecords(CSV.fromStringCSV(new String(file.getBytes(), StandardCharsets.UTF_8)));
    }

    @PutMapping("{id}")
    public void update(@RequestBody T t, @PathVariable Long id) {
        genericService.update(t, id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        genericService.delete(id);
    }

    @DeleteMapping
    public void deleteAllThatCanBeDeleted() {
        genericService.deleteAllThatCanBeDeleted();
    }

}
