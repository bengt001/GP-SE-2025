
package de.techfak.gse.template.domain.implementations;

import de.techfak.gse.template.domain.repositories.UserRepository;
import de.techfak.gse.template.domain.service.UserService;
import de.techfak.gse.template.domain.service.XpService;
import de.techfak.gse.template.domain.entities.Usr;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
    * Service der das XpService Interface implementiert. Enthält methoden um mit Usern zu iteragieren.
 */
@Service
public class XpServiceImpl implements XpService {

    private static final double MULTIPLIER_RATING_HIGH = 1.0;
    private static final double MULTIPLIER_RATING_MEDIUM_HEIGH = 0.75;
    private static final double MULTIPLIER_RATING_MEDIUM_LOW = 0.5;
    private static final double MULTIPLIER_RATING_LOW = 0.25;
    private static final double MULTIPLIER_DEFAULT = 0.0;

    private static final int RATING_HIGH = 4;
    private static final int RATING_MEDIUM_HIGH = 3;
    private static final int RATING_MEDIUM_LOW = 2;
    private static final int RATING_LOW = 1;

    private static final int BASE_XP_DEFINITIONEN = 10;
    private static final int BASE_XP_PROBLEME = 10;
    private static final int BASE_XP_PER_ITEM_SCHEMA = 5;
    private static final int DEFAULT_BASE_XP = 0;

    private final UserService userService;
    @Autowired
    private UserRepository userRepository;

    public XpServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public int calculateXp(String cardType, int uncoveredItems, int rating) {

        int baseXp = switch (cardType.toLowerCase()) {
            case "definitionen" -> BASE_XP_DEFINITIONEN;
            case "probleme" -> BASE_XP_PROBLEME;
            case "schema" -> BASE_XP_PER_ITEM_SCHEMA * uncoveredItems;
            default -> DEFAULT_BASE_XP;
        };

        return (int) (baseXp * getRatingMultiplier(rating));
    }

    /**
     * Gibt einen Multiplier Wert abhängig vom Rating aus.
     * @param rating das Rating der Karte
     * @return Multiplier Faktor
     */
    public double getRatingMultiplier(int rating) {
        return switch (rating) {
            case RATING_HIGH -> MULTIPLIER_RATING_HIGH;
            case RATING_MEDIUM_HIGH -> MULTIPLIER_RATING_MEDIUM_HEIGH;
            case RATING_MEDIUM_LOW -> MULTIPLIER_RATING_MEDIUM_LOW;
            case RATING_LOW -> MULTIPLIER_RATING_LOW;
            default -> MULTIPLIER_DEFAULT;
        };
    }

    @Override
    @Transactional
    public int addXp(String userId, String cardType, int uncoveredItems, int rating) {
        int gainedXp = calculateXp(cardType, uncoveredItems, rating);

        Optional<Usr> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            System.out.println("[XP-DEBUG] userId: " + userId);
            Usr user = optUser.get();
            System.out.println("[XP-DEBUG] user: " + user.getUserId());
            System.out.println("[XP-DEBUG] gainedXp: " + gainedXp);
            user.addXp(gainedXp);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }


        //Usr user = userService.loadUserByID(userId);
        //user.addXp(gainedXp);

        //userService.saveUser(user); // Methode muss existieren

        return gainedXp;
    }

}
