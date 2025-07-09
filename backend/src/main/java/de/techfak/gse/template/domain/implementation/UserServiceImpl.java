package de.techfak.gse.template.domain.implementation;

import de.techfak.gse.template.domain.entities.Deck;
import de.techfak.gse.template.domain.repositories.DeckRepository;
import de.techfak.gse.template.domain.repositories.UserRepository;
import de.techfak.gse.template.domain.service.NotificationService;
import de.techfak.gse.template.domain.service.UserService;
import de.techfak.gse.template.domain.entities.Usr;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Service der das UserService Interface implementiert. Enthält methoden um mit Usern zu iteragieren.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    /**
     * String for not-Found message.
     */
    private final String strNotFound = " not found";
    /**
     * Service that handles all user Interaction.
     */
    private final UserRepository userRepository;
    /**
     * Service that handles passwordEncoder interactions.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * Service that handles notification interactions.
     */
    private final NotificationService notificationService;
    private final DeckRepository deckRepository;

    /**
     * Constructor for UserServiceImpl.
     * @param userRepository userRepository
     * @param passwordEncoder passwordEncoder
     * @param notificationService notificationService
     * @param deckRepository deck Repository to work with the database
     */
    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final PasswordEncoder passwordEncoder,
                           final NotificationService notificationService, final DeckRepository deckRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.deckRepository = deckRepository;
        this.notificationService = notificationService;
    }

    /**
     * loads User by ID.
     */
    @Override
    public Usr loadUserByUsername(final String email) throws UsernameNotFoundException {
        Iterable<Usr> users = userRepository.findAll();
        for (Usr user : users) {
            if (user.getEmail().equals(email)) {
                //Die Streak prüfen und ggf. aktualisieren
                checkAndUpdateStreak(user);
                return user;
            }
        }
        throw new UsernameNotFoundException(email + strNotFound);
    }

    @Override
    public Usr loadUserByID(final String userID) throws UsernameNotFoundException {
        return userRepository.findById(userID)
                .orElseThrow(() -> new UsernameNotFoundException(userID + strNotFound));
    }

    @Override
    public Usr createUser(final String username, final String email,
                          final String password, final String nickname, final String... roles) {
        System.out.println("User created" + username);
        String encodedPassword = passwordEncoder.encode(password);
        String userId = getFreeID();
        final Usr usr = new Usr(username, email, encodedPassword, nickname, userId);
        for (final String role : roles) {
            usr.addRole(role);
        }

        userRepository.save(usr);
        notificationService.sendWelcomeNote(usr);

        return usr;
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
    public void addDeck(String userId, Long deckId) {
        Optional<Usr>  userOpt = userRepository.findById(userId);
        Optional<Deck> deckOpt = deckRepository.findById(deckId);
        if (userOpt.isEmpty() || deckOpt.isEmpty()) {
            return;
        }

        Usr user = userOpt.get();
        Deck deck = deckOpt.get();

        boolean alreadyAdded = user.getDecks().stream()
                .anyMatch(d -> d.getDeckId().equals(deck.getDeckId()));
        if (alreadyAdded) {
            return;
        }

        boolean userInDeck = deck.getUsers().stream()
                .anyMatch(u -> u.getUserId().equals(user.getUserId()));
        if (userInDeck) {
            return;
        }
        user.getDecks().add(deck);
        deck.getUsers().add(user);

        userRepository.save(user);
        deckRepository.save(deck);

    }

    @Override
    public void deleteDeck(String userId, Long deckId) {
        Optional<Usr> userOpt = userRepository.findById(userId);
        Optional<Deck> deckOpt = deckRepository.findById(deckId);
        if (userOpt.isEmpty() || deckOpt.isEmpty()) {
            return;
        }

        Usr user = userOpt.get();
        Deck deck = deckOpt.get();

        user.getDecks().remove(deck);
        deck.getUsers().remove(user);

        userRepository.save(user);
        deckRepository.save(deck);
    }

    @Override
    public List<List<String>> activeDeckNames(String userId) {
        Optional<Usr> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return List.of();
        }

        Usr user = userOpt.get();
        List<Deck> decks = user.getDecks();
        List<List<String>> deckTuples = new ArrayList<>();
        Set<String> seenFields = new HashSet<>();

        System.out.println(decks.size());

        for (Deck d : decks) {
            if (d.getFieldOfLaw().size() <= 1) {
                continue;
            }
            String field = d.getFieldOfLaw().getLast();
            if (seenFields.contains(field)) {
                continue;
            }

            seenFields.add(field);

            String color = switch (d.getAuthorId()) {
                case 0 -> {
                    field = field + " (Broadcast)";
                    yield "#78B390";
                }
                case 1 -> {
                    field = field + " (Lexmea)";
                    yield "#03364D";
                }
                default -> {
                    field = field + " (Eigener Stapel)";
                    yield "#FF968B";
                }
            };

            deckTuples.add(List.of(field, color));
        }
        return deckTuples;
    }

    @Override
    public boolean existsEmail(String email) {
        for (Usr usr : userRepository.findAll()) {
            if (usr.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveUser(Usr user) {
        userRepository.save(user);
    }

    public Usr checkAndUpdateStreak(Usr user) {
        LocalDate today = LocalDate.now();
        LocalDate lastLogin = user.getLastLoginDate();

        if (lastLogin == null) {
            // Erster Login überhaupt
            user.setStreakCount(1);
        } else if (lastLogin.isEqual(today)) {
            // User hat sich heute schon eingeloggt → keine Änderung
        } else if (lastLogin.plusDays(1).isEqual(today)) {
            // Gestern eingeloggt → streak erhöhen
            user.setStreakCount(user.getStreakCount() + 1);
        } else {
            // Mehr als 1 Tag Pause → streak zurücksetzen
            user.setStreakCount(1);
        }

        user.setLastLoginDate(today);
        userRepository.save(user);

        return user;
    }
}
