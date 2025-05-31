package de.techfak.gse.template.web.controller;
import de.techfak.gse.template.domain.*;
import de.techfak.gse.template.web.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * DeckController to handle API requests.
 */

@RestController
@RequestMapping("/api")
public class DeckController {
    private final DeckService deckService;
    private final UserService userService;
    private final CardService cardService;
    /**
     * Constructor for the Deck Controller.
     *
     * @param deckService An implementation of the DeckService.
     * @param userService An implementation of the UserService
     */
    @Autowired
    public DeckController(DeckService deckService, UserService userService, CardService cardService) {
        this.deckService = deckService;
        this.userService = userService;
        this.cardService = cardService;
    }
    /**
     * API Endpoint to get all decks that are available.
     *
     * @return List of decks that are available
     */
    @GetMapping("/decks")
    public List<Deck> getAllDecks() {
        return deckService.getAllDecks();
    }

    /**
     * API Endpoint to get a deck by its id.
     *
     * @param deckId The deck id.
     * @return A Deck, if no deck with this id exists a BadRequestException.
     */

    @GetMapping("/decks/{deckId:\\d+}")
    public Deck getDeckById(@PathVariable("deckId") final Long deckId) {
        return deckService.getDeck(deckId).orElseThrow(BadRequestException::new);
    }

    /**
     * API Endpoint to get all cards from a deck.
     *
     * @param deckId from the deck to get the cards from.
     * @return List of Card with all cards in this deck.
     */
    @GetMapping("/decks/{deckId:\\d+}/cards")
    public List<Card> getCards(@PathVariable("deckId") final long deckId) {
        return cardService.getCardsByDeckId(deckId);
    }

    /**
     * API Endpoint to get a Card by its id.
     *
     * @param cardId of the card you want to get.
     * @return a single Card.
     */
    @GetMapping("/decks/{deckId:\\d+}/cards/{cardId:\\d+}")
    public Card getCardById(@PathVariable("cardId") final long cardId) {
        return cardService.getCard(cardId).orElseThrow(BadRequestException::new);
    }

    /**
     * API Endpoint to get all Decks of a User.
     *
     * @return List of Deck.
     */

    @GetMapping("/usr/decks")
    @Secured("ROLE_USER")
    public List<Deck> getUserDecks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        return deckService.getUserDecks(usr);
    }

    /**
     * API Endpoint to get a specific deck of a user.
     *
     * @param deckId of the deck that you want to get.
     * @return A Deck.
     */
    @GetMapping("/usr/decks/{deckId:\\d+}")
    @Secured("ROLE_USER")
    public Deck getUserDeckById(@PathVariable("deckId") final long deckId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        return deckService.getUserDeckById(usr, deckId).orElseThrow(BadRequestException::new);
    }

    /**
     * KIRILL: das macht so in meinem Kopf keinen Sinn, da Karten von Decks alle gleich sind.
     * KIRILL: Es Könnte um die CardRating gehen aber so check ich nicht.
     * <p>
     * API Endpoint to get all Cards from one of the users decks.
     *
     * @param deckId of the deck.
     * @return List of Card.
     */
    @GetMapping("/usr/decks/{deckId:\\d+}/cards")
    @Secured("ROLE_USER")
    public List<Card> getUserCards(@PathVariable("deckId") final long deckId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        return deckService.getUserCards(usr, deckId);
    }

    /**
     * API Endpoint to get a Card by id from a specific user deck.
     *
     * @param deckId of the deck the card is in.
     * @param cardId of the card you want to get.
     * @return a Card or a BadRequestException if the card does not exist.
     */
    @GetMapping("/usr/decks/{deckId:\\d+}/cards/{cardId:\\d+}")
    @Secured("ROLE_USER")
    public Card getUserCardById(@PathVariable("deckId") final long deckId, @PathVariable("cardId") final long cardId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        return deckService.getUseCardById(usr, deckId, cardId).orElseThrow(BadRequestException::new);
    }

    /**
     * Kirill: I dont understand what this function is even supposed to do.
     * Kirill: This just copies a deck, if you wanted to create a new deck, you would need to
     * do something else.
     * API Endpoint for creating a new user deck as POST-Request
     *
     * @param deckId the Deck ID for the Deck that shall be copied
     * @return a new Deck as exact copy of the requested deck
     */
    @PostMapping("/usr/decks/new/{deckId:\\d+}")
    @Secured("ROLE_USER")
    public Deck createNewDeck(@PathVariable final long deckId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        return deckService.getNewUserDeck(usr, deckId).orElseThrow(BadRequestException::new);
    }

    /**
     * API Endpoint for sending a updateCard Patch-Request.
     *
     * @param deckId the deckId for the to be updated card
     * @param cardId the cardId for the to be updated card
     * @param card   the updated Card
     * @return an updated Version of the Card
     */
    @PatchMapping("/usr/decks/{deckId:\\d+}/{cardId:\\d+}")
    @Secured("ROLE_USER")
    public Card updateCard(@PathVariable final long deckId, @PathVariable final long cardId,
                           @RequestBody final Card card) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        return deckService.updateCard(usr, deckId, cardId, card).orElseThrow(BadRequestException::new);
    }

    /**
     * API Endpoint for sending a updateDeck Patch-Request.
     *
     * @param deckId the deckId that shall be updated
     * @param deck   the updated version of the deck
     * @return an updated Version of the Deck
     */
    @PatchMapping("/usr/decks/{deckId:d\\+}")
    @Secured("ROLE_USER")
    public Deck updateDeck(@PathVariable final long deckId, @RequestBody final Deck deck) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr usr = userService.loadUserByUsername(auth.getName());
        return deckService.updateDeck(usr, deckId, deck).orElseThrow(BadRequestException::new);
    }
}

