
package de.techfak.gse.template.domain;


import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class XpServiceImpl implements XpService {


    private final UserService userService;
    @Autowired
    private UserRepository userRepository;

    public XpServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public int calculateXp(String cardType, int uncoveredItems, int rating) {


        System.out.println("[XP-DEBUG] cardType: " + cardType);
        System.out.println("[XP-DEBUG] uncoveredItems: " + uncoveredItems);
        System.out.println("[XP-DEBUG] rating: " + rating);


        int baseXp = switch (cardType.toLowerCase()) {
            case "definitionen", "probleme" -> 10;
            case "schema" -> 5 * uncoveredItems;
            default -> 0;
        };

        System.out.println("[XP-DEBUG] baseXp: " + baseXp);


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
