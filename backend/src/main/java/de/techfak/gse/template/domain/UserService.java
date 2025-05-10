package de.techfak.gse.template.domain;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
* Interface für den UserService, extends die Klasse UserDetailsService.
* */
public interface UserService extends UserDetailsService {
    @Override
    Usr loadUserByUsername(String username) throws UsernameNotFoundException;

    Usr createUser(String username, String email, String password, String... roles);

    boolean exists(String email);
}
