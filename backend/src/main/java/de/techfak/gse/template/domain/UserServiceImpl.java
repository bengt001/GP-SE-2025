package de.techfak.gse.template.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** loads User by ID (email) */
    @Override
    public Usr loadUserByUsername(final String email) throws UsernameNotFoundException {
        return userRepository.findById(email)
        .orElseThrow(() -> new UsernameNotFoundException(email + " not found."));
    }

    @Override
    public Usr createUser(final String username, final String email,
                          final String password, final String... roles) {
        String encodedPassword = passwordEncoder.encode(password);
        final Usr usr = new Usr(username, email, encodedPassword);
        for (final String role : roles) {
            usr.addRole(role);
        }

        Usr saved = userRepository.save(usr);
        return saved;
    }
}
