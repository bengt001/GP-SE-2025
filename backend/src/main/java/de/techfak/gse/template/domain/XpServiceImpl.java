
package de.techfak.gse.template.domain;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class XpServiceImpl implements XpService {


    private final UserService userService;

    public XpServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public int calculateXp(String cardType, int uncoveredItems, int rating) {
        int baseXp = switch (cardType.toLowerCase()) {
            case "definition", "problem" -> 10;
            case "schema" -> 5 * uncoveredItems;
            default -> 0;
        };

        return (int) (baseXp * getRatingMultiplier(rating));
    }

    private double getRatingMultiplier(int rating) {
        return switch (rating) {
            case 4 -> 1.0;
            case 3 -> 0.75;
            case 2 -> 0.5;
            case 1 -> 0.25;
            default -> 0.0;
        };
    }

    @Override
    public int addXp(String userId, String cardType, int uncoveredItems, int rating) {
        int gainedXp = calculateXp(cardType, uncoveredItems, rating);

        //Usr user = userService.loadUserByID(userId);
        //user.addXp(gainedXp);

        //userService.saveUser(user); // Methode muss existieren

        return gainedXp;
    }

}
