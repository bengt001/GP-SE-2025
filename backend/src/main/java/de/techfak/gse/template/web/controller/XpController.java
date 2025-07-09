package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.service.UserService;
import de.techfak.gse.template.domain.entities.Usr;
import de.techfak.gse.template.domain.service.XpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/**
 * REST-Controller für XP-bezogene Aktionen der Lernanwendung.
 * Stellt Endpoints bereit, um Erfahrungspunkte (XP) für den aktuell eingeloggten Nutzer
 * zu speichern und entsprechende Informationen zurückzugeben.
 * Alle Endpoints dieses Controllers sind unter <code>/api/xp</code> erreichbar.
 */
@RestController
@RequestMapping("/api/xp")
public class XpController {

    private final XpService xpService;
    private final UserService userService;

    @Autowired
    public XpController(XpService xpService, UserService userService) {
        this.xpService = xpService;
        this.userService = userService;
    }

    /**
     * Verarbeitet eine POST-Anfrage zum Hinzufügen von XP für den aktuell eingeloggten Nutzer.
     *
     * @param request enthält Kartentyp, aufgedeckte Items und Bewertung
     * @return ResponseEntity mit der Anzahl der verdienten XP
     */
    @PostMapping("/earn")
    public ResponseEntity<XpResponse> earnXp(@RequestBody XpEarnRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());

        Integer gainedXp = xpService.addXp(
                usr.getUserId(),
                request.getCardType(),
                request.getUncoveredItems(),
                request.getRating()
        );
        return ResponseEntity.ok(new XpResponse(gainedXp));
    }

    /**
     * Request-DTO für das Hinzufügen von XP.
     * Enthält Kartentyp, Anzahl aufgedeckter Items und Bewertung.
     */
    public static class XpEarnRequest {
        private String cardType;
        private int uncoveredItems;
        private int rating;

        // Getter/Setter
        public String getCardType() {
            return cardType;
        }
        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public int getUncoveredItems() {
            return uncoveredItems;
        }
        public void setUncoveredItems(int uncoveredItems) {
            this.uncoveredItems = uncoveredItems;
        }

        public int getRating() {
            return rating;
        }
        public void setRating(int rating) {
            this.rating = rating;
        }
    }

    /**
     * Response-DTO für die zurückgegebenen XP.
     */
    public static class XpResponse {
        private final Integer gainedXp;
        public XpResponse(Integer gainedXp) {
            this.gainedXp = gainedXp;
        }
        public Integer getGainedXp() {
            return gainedXp;
        }
    }
}

