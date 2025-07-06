package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.service.UserService;
import de.techfak.gse.template.domain.entities.Usr;
import de.techfak.gse.template.web.command.UsrCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Rest Controller Für den Usr. Fängt REST Abfragen ab und führt die Methoden des UserService aus.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class UsrController {
    private final UserService userService;

    @Autowired
    public UsrController(UserService userService) {

        this.userService = userService;
    }

    @PostMapping("/register")
    public Usr createUser(@RequestBody UsrCmd usrCmd) {
        return userService.createUser(usrCmd.username(), usrCmd.email(),
                usrCmd.password(), usrCmd.username(), "ROLE_USER");
    }

    @GetMapping("/exists")
    public boolean exists(@RequestParam String email) {
        return userService.existsEmail(email);
    }

    /**
     * adds a deck with given Id to an User.
     * @param deckId Id of the Deck that is added
     */
    @PostMapping("/usr/decks/{deckId}/add")
    public void addDeck(@PathVariable Long deckId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        userService.addDeck(usr.getUserId(), deckId);
    }

    /**
     * deletes a deck from a user.
     * @param deckId Id of the deck that is deleted
     */
    @DeleteMapping("/usr/decks/{deckId}/delete")
    public void deleteDeck(@PathVariable Long deckId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        userService.deleteDeck(usr.getUserId(), deckId);
    }

    /**
     * gets the list of the names of the decks that the user has activee.
     * @return List of names of the active Decks
     */
    @GetMapping("/usr/activeDecks")
    public List<List<String>> activeDecks() {
        List<List<String>> result = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        result = userService.activeDeckNames(usr.getUserId());
        return result;
    }
}
