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

import javax.swing.plaf.synth.SynthTabbedPaneUI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class DeckControllerTest {
    final Deck deck1 = new Deck(true, List.of("Test"), 1);
    final Deck deck2 = new Deck(false, List.of("Test"), 2);
    final Deck deck3 = new Deck(true, List.of("Test"), 2);

    {
        deck1.setDeckId(1L);
        deck1.setPublishDate(LocalDate.now());

        deck2.setDeckId(2L);
        deck2.setPublishDate(LocalDate.now());

        deck3.setDeckId(3L);
        deck3.setPublishDate(LocalDate.now());
    }

    final List<Deck> DECKS = List.of(deck1, deck2, deck3);

    final Deck DECK = new Deck(true, List.of("Test"), 1);
    {
        DECK.setDeckId(1L);
        DECK.setPublishDate(LocalDate.now());
    }

    final Card card1 = new Card("Karte","Tolle Karte", deck1);
    final Card card2 = new Card( "Karte", "Super Karte", deck2);
    final Card card3 = new Card( "Karte","Blöde Karte", deck3);

    final List<Card> CARDS = List.of(card1, card2, card3);
    final Card CARD = card1;
    {
        CARD.setCardId(1L);
    }
    final Usr TESTUSR = new Usr("testuser", "test@mytest.com", "{bcrypt}$2a$10$WoG5Z4YN9Z37EWyNCkltyeFr6PtrSXSLMeFWOeDUwcanht5CIJgPa", "TEST", "1");

    private AutoCloseable closeable;

    @Mock
    private DeckServiceImpl deckService;

    @Mock
    private UserService userService;

    @Mock
    private CardService cardService;


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
        when(deckService.getCardByIdFromDeck(1, 1)).thenReturn(Optional.of(CARD));
        when(deckService.getUserDecks(TESTUSR)).thenReturn(DECKS);

        when(userService.loadUserByUsername("testuser")).thenReturn(TESTUSR);

        when(cardService.getCardsByDeckId(1L)).thenReturn(CARDS);
        when(cardService.getCard(1L)).thenReturn(Optional.of(CARD));


        when(authentication.getName()).thenReturn("testuser");
        when(securityContext.getAuthentication()).thenReturn(authentication);

    }

    @Test
    void getAllDecks_success() {
        DeckController deckController = new DeckController(deckService, userService, cardService);
        List<Deck> decks = deckController.getAllDecks();

        assertThat(decks.size()).isEqualTo(3);
        assertThat(decks.get(0).getDeckId()).isEqualTo(1);
        assertThat(decks.get(1).getAuthorId()).isEqualTo(2);
        assertThat(decks.get(2).getVisibility()).isEqualTo(true);
    }

    @Test
    void getDeckById_success() {
        DeckController deckController = new DeckController(deckService, userService, cardService);

        Deck deck = deckController.getDeckById(1L);
        assertThat(deck.getDeckId()).isEqualTo(1L);
    }

    @Test
    void getCards_success() {
        DeckController deckController = new DeckController(deckService, userService, cardService);

        List<Card> cards = deckController.getCards(1L);
        System.out.println(cards);
        assertThat(cards.size()).isEqualTo(3);
        assertThat(deckService.getCards(1).get(1)).isEqualTo(CARDS.get(1));

    }

    @Test
    void getCardById_success() {
        DeckController deckController = new DeckController(deckService, userService, cardService);

        Card card = deckController.getCardById(1L);

        assertThat(card).isNotNull();
        assertThat(card.getCardId()).isEqualTo(1);
        assertThat(card.getCardType()).isEqualTo("Tolle Karte");
    }

    @Test
    void getUserDecks_success() {
        DeckController deckController = new DeckController(deckService, userService, cardService);
        List<Deck> decks = deckController.getUserDecks();
        assertThat(decks.size()).isEqualTo(3);
        assertThat(decks.get(0).getDeckId()).isEqualTo(1);
        assertThat(decks.get(1).getAuthorId()).isEqualTo(2);
        assertThat(decks.get(2).getVisibility()).isEqualTo(true);
    }

    @Test
    void getUserDeckById_success() {
        DeckController deckController = new DeckController(deckService, userService, cardService);

        Deck deck = deckController.getDeckById(1L);

        assertThat(deck.getDeckId()).isEqualTo(1);
    }

    @Test
    void getUserCards_success() {
        DeckController deckController = new DeckController(deckService, userService, cardService);

        List<Card> cards = deckController.getCards(1);

        assertThat(cards.size()).isEqualTo(3);
        assertThat(deckService.getCards(1).get(1)).isEqualTo(CARDS.get(1));
    }

    @Test
    void getUserCardById_success() {
        DeckController deckController = new DeckController(deckService, userService, cardService);

        Card card = deckController.getCardById(1);

        assertThat(card).isNotNull();
        assertThat(card.getCardId()).isEqualTo(1);
        assertThat(card.getContent()).isEqualTo("Karte");
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }
}
