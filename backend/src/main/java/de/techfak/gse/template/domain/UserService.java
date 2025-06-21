package de.techfak.gse.template.domain;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
* Interface für den UserService, extends die Klasse UserDetailsService.
* */
public interface UserService extends UserDetailsService {
    @Override
    Usr loadUserByUsername(String email) throws UsernameNotFoundException;

    Usr loadUserById(String id);

    Usr createUser(String username, String email, String password, String displayName, String... roles);

    boolean existsEmail(String email);

    String getFreeID();
}
