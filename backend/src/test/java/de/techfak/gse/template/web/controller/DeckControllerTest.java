package de.techfak.gse.template.web.controller;


import de.techfak.gse.template.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class DeckControllerTest {
    final List<Deck> DECKS = List.of(new Deck((long) 1, 1, LocalDate.now(), true),
            new Deck((long) 2, 2, LocalDate.now(), false),
            new Deck((long) 3, 2, LocalDate.now(), true));
    final Deck DECK = new Deck((long) 1, 1, LocalDate.now(), true);

    final List<Card> CARDS = List.of(new Card((long) 1, "Tolle Karte", "Karte"),
            new Card((long) 2, "Super Karte", "Karte"),
            new Card((long) 3, "Blöde Karte", "Karte"));
    final Card CARD = new Card((long) 1, "Tolle Karte", "Karte");

    final Usr TESTUSR = new Usr("testuser", "test@mytest.com", "{bcrypt}$2a$10$WoG5Z4YN9Z37EWyNCkltyeFr6PtrSXSLMeFWOeDUwcanht5CIJgPa");

    private AutoCloseable closeable;

    @Mock
    private DeckServiceImpl deckService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        assertThat(deckService).isNotNull();
        assertThat(userService).isNotNull();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(deckService.getAllDecks()).thenReturn(DECKS);
        when(deckService.getDeckById(1)).thenReturn(Optional.of(DECK));
        when(deckService.getCards(1)).thenReturn(CARDS);
        when(deckService.getCardById(1, 1)).thenReturn(Optional.of(CARD));
        when(deckService.getUserDecks(TESTUSR)).thenReturn(DECKS);

        when(userService.loadUserByUsername("testuser")).thenReturn(TESTUSR);


        when(authentication.getName()).thenReturn("testuser");
        when(securityContext.getAuthentication()).thenReturn(authentication);

    }

    @Test
    void getAllDecks_success() {
        DeckController deckController = new DeckController(deckService, userService);
        List<Deck> decks = deckController.getAllDecks();

        assertThat(decks.size()).isEqualTo(3);
        assertThat(decks.get(0).getId()).isEqualTo(1);
        assertThat(decks.get(1).getAuthor_id()).isEqualTo(2);
        assertThat(decks.get(2).getVisibility()).isEqualTo(true);
    }

    @Test
    void getDeckById_success() {
        DeckController deckController = new DeckController(deckService, userService);

        Deck deck = deckController.getDeckById(1);

        assertThat(deck.getId()).isEqualTo(1);
    }

    @Test
    void getCards_success() {
        DeckController deckController = new DeckController(deckService, userService);

        List<Card> cards = deckController.getCards(1);

        assertThat(cards.size()).isEqualTo(3);
        assertThat(deckService.getCards(1).get(1)).isEqualTo(CARDS.get(1));

    }

    @Test
    void getCardById_success() {
        DeckController deckController = new DeckController(deckService, userService);

        Card card = deckController.getCardById(1, 1);

        assertThat(card).isNotNull();
        assertThat(card.getCard_id()).isEqualTo(1);
        assertThat(card.getContent()).isEqualTo("Tolle Karte");
    }

    @Test
    void getUserDecks_success() {
        DeckController deckController = new DeckController(deckService, userService);
        List<Deck> decks = deckController.getUserDecks();
        assertThat(decks.size()).isEqualTo(3);
        assertThat(decks.get(0).getId()).isEqualTo(1);
        assertThat(decks.get(1).getAuthor_id()).isEqualTo(2);
        assertThat(decks.get(2).getVisibility()).isEqualTo(true);
    }

    @Test
    void getUserDeckById_success() {
        DeckController deckController = new DeckController(deckService, userService);

        Deck deck = deckController.getDeckById(1);

        assertThat(deck.getId()).isEqualTo(1);
    }

    @Test
    void getUserCards_success() {
        DeckController deckController = new DeckController(deckService, userService);

        List<Card> cards = deckController.getCards(1);

        assertThat(cards.size()).isEqualTo(3);
        assertThat(deckService.getCards(1).get(1)).isEqualTo(CARDS.get(1));
    }

    @Test
    void getUserCardById_success() {
        DeckController deckController = new DeckController(deckService, userService);

        Card card = deckController.getCardById(1, 1);

        assertThat(card).isNotNull();
        assertThat(card.getCard_id()).isEqualTo(1);
        assertThat(card.getContent()).isEqualTo("Tolle Karte");
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }
}
