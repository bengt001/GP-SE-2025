package de.techfak.gse.template;

import de.techfak.gse.template.domain.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class InitializeDatabase implements InitializingBean {

    private final UserService userService;

    @Autowired
    public InitializeDatabase(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public void afterPropertiesSet() {
        try {
            userService.loadUserByUsername("test@mail.com");
        } catch (UsernameNotFoundException ex) {
            final Usr usr = userService.createUser("test",
                    "test@mail.com",
                    "password", "Martin", "ROLE_USER");
        }
    }
}
