package de.techfak.gse.template;

import de.techfak.gse.template.domain.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Initialize Database initialisert unsere Datenbank mit Startdaten.
 * */
@Service
public class InitializeDatabase implements InitializingBean {
    /** test mail die vom Standartnutzer genutzt wird.*/
    String testEmail = "test@mail.com";
    private final UserService userService;

    @Autowired
    public InitializeDatabase(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public void afterPropertiesSet() {
        try {
            userService.loadUserByUsername(testEmail);
        } catch (UsernameNotFoundException ex) {
            final Usr usr = userService.createUser("test",
                    testEmail,
                    "{bcrypt}$2a$10$WoG5Z4YN9Z37EWyNCkltyeFr6PtrSXSLMeFWOeDUwcanht5CIJgPa", "ROLE_USER");
        }
    }
}
