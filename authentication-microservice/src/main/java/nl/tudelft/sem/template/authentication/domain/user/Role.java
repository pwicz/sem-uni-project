package nl.tudelft.sem.template.authentication.domain.user;

import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

/**
 * A DDD value object representing a NetID in our domain.
 */
@EqualsAndHashCode
public class Role implements GrantedAuthority {
    private final transient String roleValue;

    public Role(String role) {
        // validate NetID
        this.roleValue = role;
    }

    @Override
    public String getAuthority() {
        return roleValue;
    }
}