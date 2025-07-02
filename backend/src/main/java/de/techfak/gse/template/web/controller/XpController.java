package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.UserService;
import de.techfak.gse.template.domain.Usr;
import de.techfak.gse.template.domain.XpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/xp")
public class XpController {

    private final XpService xpService;
    private final UserService userService;                                              //ZUM XP SPERICERHN TEST

    @Autowired
    public XpController(XpService xpService, UserService userService) {
        this.xpService = xpService;
        this.userService = userService;
    }

    @PostMapping("/earn")
    public ResponseEntity<XpResponse> earnXp(@RequestBody XpEarnRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());                           //ZUM XP SPERICERHN TEST

        int gainedXp = xpService.addXp(
                usr.getUserId(),
                request.getCardType(),
                request.getUncoveredItems(),
                request.getRating()
        );
        return ResponseEntity.ok(new XpResponse(gainedXp));
    }

    // einfache DTO-Klassen
    public static class XpEarnRequest {
        private String cardType;
        private int uncoveredItems;
        private int rating;

        // Getter/Setter

        public String getCardType() { return cardType; }
        public void setCardType(String cardType) { this.cardType = cardType; }

        public int getUncoveredItems() { return uncoveredItems; }
        public void setUncoveredItems(int uncoveredItems) { this.uncoveredItems = uncoveredItems; }

        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = rating; }
    }

    public static class XpResponse {
        private final int gainedXp;
        public XpResponse(int gainedXp) {
            this.gainedXp = gainedXp;
        }
        public int getGainedXp() {
            return gainedXp;
        }
    }
}

