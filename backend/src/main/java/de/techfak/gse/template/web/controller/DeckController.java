package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.*;
import de.techfak.gse.template.web.Exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api")
public class DeckController {

    private final DeckService deckService;
    private final UserService userService;

    @Autowired
    public DeckController(DeckService deckService, UserService userService) {
        this.deckService = deckService;
        this.userService = userService;
    }

    @GetMapping("/decks")
    public List<Deck> getAllDecks() {
        return deckService.getAllDecks();
    }

    @GetMapping("/decks/{id:\\d+}")
    public Deck getDeckById(@PathVariable("id") final long id) {
        return deckService.getDeckById(id).orElseThrow(BadRequestException::new);
    }

    @GetMapping("/decks/{id:\\d+}/cards")
    public List<Card> getCards(@PathVariable("id") final long id) {
        return deckService.getCards(id);
    }

    @GetMapping("/decks/{deckId:\\d+}/cards/{cardId:\\d+}")
    public Card getCardById(@PathVariable("deckId") final long deckId, @PathVariable("cardId") final long cardId) {
        return deckService.getCardById(deckId, cardId).orElseThrow(BadRequestException::new);
    }

    @GetMapping("/usr/decks")
    @Secured("ROLE_USER")
    public List<Deck> getUserDecks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        return deckService.getUserDecks(usr);
    }

    @GetMapping("/usr/decks/{id:\\d+}")
    @Secured("ROLE_USER")
    public Deck getUserDeckById(@PathVariable("id") final long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        return deckService.getUserDeckById(usr, id).orElseThrow(BadRequestException::new);
    }

    @GetMapping("/usr/decks/{deckId:\\d+}/cards")
    @Secured("ROLE_USER")
    public List<Card> getUserCards(@PathVariable("deckId") final long deckId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        return deckService.getUserCards(usr, deckId);
    }

    @GetMapping("/usr/decks/{deckId:\\d+}/cards/{cardId:\\d+}")
    @Secured("ROLE_USER")
    public Card getUserCardById(@PathVariable("deckId") final long deckId, @PathVariable("cardId") final long cardId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        return deckService.getUseCardById(usr, deckId, cardId).orElseThrow(BadRequestException::new);
    }

    @PostMapping("/usr/decks/new/{deckId:\\d+}")
    @Secured("ROLE_USER")
    public Deck createNewDeck(@PathVariable final long deckId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        return deckService.getNewUserDeck(usr, deckId).orElseThrow(BadRequestException::new);
    }
}
