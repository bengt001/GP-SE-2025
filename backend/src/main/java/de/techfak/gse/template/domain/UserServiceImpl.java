package de.techfak.gse.template.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service der das UserService Interface implementiert. Enthält methoden um mit Usern zu iteragieren.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public final String strNotFound = " not found";

    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** loads User by ID (email). */
    @Override
    public Usr loadUserByUsername(final String email) throws UsernameNotFoundException {
        return userRepository.findById(email)
        .orElseThrow(() -> new UsernameNotFoundException(email + strNotFound));
    }



    public Usr loadUserByID(final String userID) throws UsernameNotFoundException {
        return userRepository.findById(userID)
                .orElseThrow(() -> new UsernameNotFoundException(userID + strNotFound));
    }


    @Override
    public Usr createUser(final String username, final String email,
                          final String password,  final String nickname, final String... roles) {
        String encodedPassword = passwordEncoder.encode(password);
        String userId = getFreeID();
        final Usr usr = new Usr(username, email, encodedPassword, nickname, userId);
        for (final String role : roles) {
            usr.addRole(role);
        }
        return userRepository.save(usr);
    }

    @Override
    public String getFreeID() {
        Long counter = 0L;
        while (true) {
            try {
                loadUserByID(String.valueOf(counter));
                counter++;
            } catch (UsernameNotFoundException e) {
                break;
            }
        }
        return String.valueOf(counter);
    }

    @Override
    public boolean existsEmail(String email) {
        try {
            loadUserByUsername(email);
            return true;
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }
}
