package poc.error;

public class AuthorNotFoundException extends RuntimeException{

    public AuthorNotFoundException(Long id) {
        super("Author id not found : " + id);
    }
}
