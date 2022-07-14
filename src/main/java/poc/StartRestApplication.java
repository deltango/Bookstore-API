package poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import poc.Entity.Author;
import poc.Entity.Book;
import poc.Repository.AuthorRepository;
import poc.Repository.BookRepository;

@SpringBootApplication
public class StartRestApplication {

    @Autowired BookRepository bookRepository;
    @Autowired AuthorRepository authorRepository;

    // start everything
    public static void main(String[] args) {
        SpringApplication.run(StartRestApplication.class, args);
    }

    @Profile("demo")
    @Bean
    CommandLineRunner initDatabase(BookRepository bookRepository, AuthorRepository authorRepository) {
        return args -> {
            Author author = new Author("Author 1", "01121986");
            authorRepository.save(author);
            Book book = new Book("Book 1", "ISBN1234", 1990, 12.34, "Fiction");
            bookRepository.save(book);
        };
    }


}