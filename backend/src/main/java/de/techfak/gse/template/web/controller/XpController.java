/*package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.XpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/xp")
public class XpController {

    private final XpService xpService;

    @Autowired
    public XpController(XpService xpService) {
        this.xpService = xpService;
    }

    @PostMapping("/earn")
    public ResponseEntity<XpResponse> earnXp(@RequestBody XpEarnRequest request) {
        int gainedXp = xpService.addXp(
                request.getUserId(),
                request.getCardType(),
                request.getUncoveredItems(),
                request.getRating()
        );
        return ResponseEntity.ok(new XpResponse(gainedXp));
    }

    // einfache DTO-Klassen
    public static class XpEarnRequest {
        private String userId;
        private String cardType;
        private int uncoveredItems;
        private int rating;

        // Getter/Setter
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

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

 */
