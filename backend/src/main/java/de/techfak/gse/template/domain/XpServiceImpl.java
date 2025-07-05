
package de.techfak.gse.template.domain;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
    * Service der das XpService Interface implementiert. Enthält methoden um mit Usern zu iteragieren.
 */
@Service
public class XpServiceImpl implements XpService {


    private final UserService userService;
    @Autowired
    private UserRepository userRepository;

    private static final double MULTIPLIER_RATING_4 = 1.0;
    private static final double MULTIPLIER_RATING_3 = 0.75;
    private static final double MULTIPLIER_RATING_2 = 0.5;
    private static final double MULTIPLIER_RATING_1 = 0.25;
    private static final double MULTIPLIER_DEFAULT = 0.0;

    private static final int RATING_4 = 4;
    private static final int RATING_3 = 3;
    private static final int RATING_2 = 2;
    private static final int RATING_1 = 1;

    private static final int BASE_XP_DEFINITIONEN = 10;
    private static final int BASE_XP_PROBLEME = 10;
    private static final int BASE_XP_PER_ITEM_SCHEMA = 5;
    private static final int DEFAULT_BASE_XP = 0;

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

    public double getRatingMultiplier(int rating) {
        return switch (rating) {
            case RATING_4 -> MULTIPLIER_RATING_4;
            case RATING_3 -> MULTIPLIER_RATING_3;
            case RATING_2 -> MULTIPLIER_RATING_2;
            case RATING_1 -> MULTIPLIER_RATING_1;
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
