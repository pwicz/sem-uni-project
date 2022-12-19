package nl.tudelft.sem.template.example.domain;

import nl.tudelft.sem.template.example.authentication.AuthManager;

/**
 * Exception to indicate that an account is not authorized to release a faculty.
 */
public class AccountNotAuthorizedException extends Throwable {
    static final long serialVersionUID = -3387516993124229948L;

    public AccountNotAuthorizedException(AuthManager authManager) {
        super();
    }
}
