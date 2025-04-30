package de.techfak.gse.template;

import de.techfak.gse.template.domain.Date;
import de.techfak.gse.template.domain.DeckServiceImpl;
import de.techfak.gse.template.domain.Stapel;
import de.techfak.gse.template.web.controller.DeckController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class DeckControllerTest {
    Date today = new Date(2025, 4,29);
    final List<Stapel> DECKS = List.of(new Stapel((long) 1, 1, today, true),
            new Stapel((long) 2, 2, today, false),
            new Stapel((long) 3, 2, today, true));
    final Stapel DECK = new Stapel((long) 1, 1, today, true);

    private AutoCloseable closeable;

    @Mock
    private DeckServiceImpl deckService; // <1>

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this); // <2>
    }

    @Test
    void getDecks() {
        assertThat(deckService).isNotNull();
        when(deckService.getDecks()).thenReturn(DECKS); // <3>
        DeckController deckController = new DeckController(deckService); // <4>

        List<Stapel> decks = deckController.getDecks();

        assertThat(decks.size()).isEqualTo(3);
        assertThat(decks.get(0).getStapel_id()).isEqualTo(1);
        assertThat(decks.get(1).getAuthor_id()).isEqualTo(2);
        assertThat(decks.get(2).getVisibility()).isEqualTo(true);
    }

    @Test
    void getDeckById() {
        assertThat(deckService).isNotNull();
        when(deckService.getDeckById(1)).thenReturn(Optional.of(DECK));
        DeckController deckController = new DeckController(deckService);

        Stapel deck = deckController.getDeckById(1);

        assertThat(deck.getStapel_id()).isEqualTo(1);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }
}
