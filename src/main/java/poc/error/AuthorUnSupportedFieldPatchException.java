package poc.error;

import java.util.Set;

public class AuthorUnSupportedFieldPatchException extends RuntimeException {

    public AuthorUnSupportedFieldPatchException(Set<String> keys) {
        super("Field " + keys.toString() + " update is not allow.");
    }

}
