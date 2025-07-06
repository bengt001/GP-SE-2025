package de.techfak.gse.template.domain.service;

import de.techfak.gse.template.domain.entities.Usr;
import de.techfak.gse.template.domain.entities.Usr;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
* Interface für den UserService, extends die Klasse UserDetailsService.
* */
public interface UserService extends UserDetailsService {
    @Override
    Usr loadUserByUsername(String email) throws UsernameNotFoundException;

    Usr loadUserByID(String userID) throws UsernameNotFoundException;

    Usr createUser(String username, String email, String password, String displayName, String... roles);

    boolean existsEmail(String email);

    String getFreeID();

    void addDeck(String userId, Long deckId);

    void deleteDeck(String userId, Long deckId);

    List<List<String>>  activeDeckNames(String userId);

    void saveUser(Usr user);
}
