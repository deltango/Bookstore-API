package poc.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poc.Entity.Author;

import java.util.Set;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Set<Author> findAuthorsById(Long bookId);
}
