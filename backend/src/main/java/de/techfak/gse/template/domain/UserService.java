package de.techfak.gse.template.domain;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
* Interface für den UserService, extends die Klasse UserDetailsService.
* */
public interface UserService extends UserDetailsService {
    @Override
    Usr loadUserByUsername(String email) throws UsernameNotFoundException;

    Usr createUser(String username, String email, String password, String displayName,String... roles);

    boolean exists_email(String email);

    String getFreeID();
}
