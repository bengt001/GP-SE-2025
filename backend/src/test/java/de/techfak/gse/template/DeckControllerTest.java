package de.techfak.gse.template;


import de.techfak.gse.template.domain.*;
import de.techfak.gse.template.web.controller.DeckController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    private AutoCloseable closeable;

    @Mock
    private DeckServiceImpl deckService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this); // <2>
    }

    @Test
    void getAllDecks() {
        assertThat(deckService).isNotNull();
        assertThat(userService).isNotNull();
        when(deckService.getAllDecks()).thenReturn(DECKS); // <3>
        DeckController deckController = new DeckController(deckService, userService); // <4>

        List<Deck> decks = deckController.getAllDecks();

        assertThat(decks.size()).isEqualTo(3);
        assertThat(decks.get(0).getId()).isEqualTo(1);
        assertThat(decks.get(1).getAuthor_id()).isEqualTo(2);
        assertThat(decks.get(2).getVisibility()).isEqualTo(true);
    }

    @Test
    void getDeckById() {
        assertThat(deckService).isNotNull();
        assertThat(userService).isNotNull();
        when(deckService.getDeckById(1)).thenReturn(Optional.of(DECK));
        DeckController deckController = new DeckController(deckService, userService);

        Deck deck = deckController.getDeckById(1);

        assertThat(deck.getId()).isEqualTo(1);
    }

    @Test
    void getCards() {
        assertThat(deckService).isNotNull();
        assertThat(userService).isNotNull();
        when(deckService.getCards(1)).thenReturn(CARDS);
        DeckController deckController = new DeckController(deckService, userService);

        List<Card> cards = deckController.getCards(1);

        assertThat(cards.size()).isEqualTo(3);
        assertThat(deckService.getCards(1).get(1)).isEqualTo(CARDS.get(1));
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }
}
