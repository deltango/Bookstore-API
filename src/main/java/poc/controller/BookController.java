package poc.controller;

import org.springframework.http.ResponseEntity;
import poc.Entity.Author;
import poc.Entity.Book;
import poc.Repository.AuthorRepository;
import poc.Repository.BookRepository;
import poc.error.AuthorNotFoundException;
import poc.error.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Set;

@RestController
@Validated
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    // Find
    @GetMapping("/books")
    List<Book> findAll() {
        return bookRepository.findAll();
    }

    // Save
    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    Book newBook(@Valid @RequestBody Book newBook) {
        return bookRepository.save(newBook);
    }

    // Find
    @GetMapping("/books/{id}")
    Book findOne(@PathVariable @Min(1) Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    // Save or update
    @PutMapping("/books/{id}")
    Book saveOrUpdate(@RequestBody Book newBook, @PathVariable Long id) {

        return bookRepository.findById(id)
                .map(x -> {
                    x.setTitle(newBook.getTitle());
                    x.setAuthors(newBook.getAuthors());
                    return bookRepository.save(x);
                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return bookRepository.save(newBook);
                });
    }

    @DeleteMapping("/books/{id}")
    void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }

    @GetMapping("/authors/{id}/books")
    public ResponseEntity<Set<Book>> getAllBooksByAuthorId(@PathVariable(value = "id") Long id) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException(id);
        }
        Set<Book> books = bookRepository.findBooksById(id);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping("/books/{id}/authors")
    ResponseEntity<Author> addAuthor(@PathVariable(value = "id") Long id, @RequestBody Author authorRequest) {
        Author author = bookRepository.findById(id).map(book -> {
            long authorId = authorRequest.getId();

            // author exists
            if (authorId != 0L) {
                Author _author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new AuthorNotFoundException(authorId));
                book.addAuthor(_author);
                bookRepository.save(book);
                return _author;
            }

            // add and create new author
            book.addAuthor(authorRequest);
            return authorRepository.save(authorRequest);
        }).orElseThrow(() -> new BookNotFoundException(id));
        return new ResponseEntity<>(author, HttpStatus.CREATED);
    }

    @DeleteMapping("/books/{id}/authors/{authorId}")
    public ResponseEntity<HttpStatus> deleteAuthorFromBook(@PathVariable(value = "id") Long id, @PathVariable(value = "authorId") Long authorId) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        book.removeAuthor(authorId);
        bookRepository.save(book);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
