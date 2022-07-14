package poc.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poc.Entity.Book;

import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {
    Set<Book> findBooksById(Long authorId);
}
