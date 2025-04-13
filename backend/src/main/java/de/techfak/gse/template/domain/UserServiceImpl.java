package de.techfak.gse.template.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** loads User by ID (email) */
    @Override
    public User loadUserByUsername(final String email) throws UsernameNotFoundException {
        return userRepository.findById(email)
        .orElseThrow(() -> new UsernameNotFoundException("User Email " + email + " not found."));
    }
}
