package poc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import poc.Entity.Author;
import poc.Entity.Book;
import poc.Repository.AuthorRepository;
import poc.Repository.BookRepository;
import poc.error.AuthorNotFoundException;
import poc.error.BookNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Set;

@RestController
@Validated
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    //Find
    @GetMapping("/authors")
    List<Author> findAll() {
        return authorRepository.findAll();
    }

    //Save
    @PostMapping("/authors")
    @ResponseStatus(HttpStatus.CREATED)
    Author newAuthor(@Valid @RequestBody Author newAuthor) {
        return authorRepository.save(newAuthor);
    }

    //Find
    @GetMapping("/authors/{id}")
    Author findOne(@PathVariable @Min(1) Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    //Save or update
    @PutMapping("/authors/{id}")
    Author saveOrUpdate(@RequestBody Author newAuthor, @PathVariable Long id) {

        return authorRepository.findById(id)
                .map(x -> {
                    x.setName(newAuthor.getName());
                    x.setBooks(newAuthor.getBooks());
                    return authorRepository.save(x);
                })
                .orElseGet(() -> {
                    newAuthor.setId(id);
                    return authorRepository.save(newAuthor);
                });
    }

    //Delete
    @DeleteMapping("/authors/{id}")
    void deleteAuthor(@PathVariable Long id) {
        authorRepository.deleteById(id);
    }

    @GetMapping("/books/{id}/authors")
    public ResponseEntity<Set<Author>> getAllAuthorsByBookId(@PathVariable(value = "id") Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        Set<Author> authors = authorRepository.findAuthorsById(id);
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @PostMapping("/authors/{id}/books")
    ResponseEntity<Book> addBook(@PathVariable(value = "id") Long id, @RequestBody Book bookRequest) {
        Book book = authorRepository.findById(id).map(author -> {
            long bookId = bookRequest.getId();

            // book exists
            if (bookId != 0L) {
                Book _book = bookRepository.findById(bookId)
                        .orElseThrow(() -> new BookNotFoundException(bookId));
                author.addBook(_book);
                authorRepository.save(author);
                return _book;
            }

            // add and create new author
            author.addBook(bookRequest);
            return bookRepository.save(bookRequest);
        }).orElseThrow(() -> new AuthorNotFoundException(id));
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @DeleteMapping("/authors/{id}/books/{bookId}")
    public ResponseEntity<HttpStatus> deleteBookFromAuthor(@PathVariable(value = "id") Long id, @PathVariable(value = "bookId") Long bookId) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));

        author.removeBook(bookId);
        authorRepository.save(author);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
