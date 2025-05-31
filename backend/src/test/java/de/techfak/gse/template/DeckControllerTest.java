package de.techfak.gse.template;


import de.techfak.gse.template.domain.*;
import de.techfak.gse.template.web.exception.BadRequestException;
import de.techfak.gse.template.web.controller.DeckController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class DeckControllerTest {
    final Deck deck1 = new Deck(true, List.of("Test"), 1);
    final Deck deck2 = new Deck(true, List.of("Test"), 2);
    final Deck deck3 = new Deck(false, List.of("Test"), 1);

    {
        deck1.setDeckId(1L);
        deck1.updateDate();
        deck2.setDeckId(2L);
        deck2.updateDate();
        deck3.setDeckId(3L);
        deck3.updateDate();
    }
    final List<Deck> DECKS = List.of(deck1, deck2, deck3);

    final Deck DECK_SINGLE = new Deck(false, List.of("Test"), 5);

    {
        DECK_SINGLE.setDeckId(5L);
        DECK_SINGLE.setPublishDate(LocalDate.now());
    }
    final List<Card> CARDS = List.of(
            new Card("Tolle Karte", "Karte", DECK_SINGLE),
            new Card("Super Karte", "Karte", DECK_SINGLE),
            new Card("Blöde Karte", "Karte", DECK_SINGLE)
    );



    private AutoCloseable closeable;

    @Mock
    private DeckService deckService;

    @Mock
    private UserService userService;

    @Mock
    private CardService cardService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private DeckController deckController;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this); // <2>

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        assertThat(userService).isNotNull();
        assertThat(deckService).isNotNull();
    }

    @Test
    void getAllDecks() {
        when(deckService.getAllDecks()).thenReturn(DECKS); // <3>
        List<Deck> allDecks = deckController.getAllDecks();
        assertThat(allDecks).isEqualTo(DECKS);
    }

    @Test
    void getDeckById() {
        when(deckService.getDeckById(5L)).thenReturn(Optional.of(DECK_SINGLE));
        Deck deck = deckController.getDeckById(5L);
        assertThat(deck).isEqualTo(DECK_SINGLE);
    }

    @Test
    void getCards() {

        when(cardService.getCardsByDeckId(1L)).thenReturn(CARDS);
        List<Card> cards = deckController.getCards(1L);
        assertThat(cards).isEqualTo(CARDS);
    }


    /* --------------------------------------------------------------------------------------*/

    /**
     * Test for successful creation of new Deck Copy for the User
     * Test assumes that user is authenticated and the Deck exists
     */
    @Test
    void getNewDeck_userIsAuthenticated_templateExists() {

        String username = "testuser";
        long templateId = 1L;
        long newDeckId = 1L;
        int authorId = 2;

        Usr mockUser = mock(Usr.class);

        when(authentication.getName()).thenReturn(username);
        when(userService.loadUserByUsername(username)).thenReturn(mockUser);

        Deck aNewDeck = new Deck(true, List.of("Test"), authorId);
        aNewDeck.setDeckId(1L);
        aNewDeck.updateDate();
        when(deckService.getNewUserDeck(mockUser, templateId)).thenReturn(Optional.of(aNewDeck));

        Deck aResultingDeck = deckController.createNewDeck(templateId);

        assertThat(aResultingDeck).isNotNull();
        assertThat(aResultingDeck.getDeckId()).isEqualTo(newDeckId);

        verify(authentication).getName();
        verify(userService).loadUserByUsername(username);
        verify(deckService).getNewUserDeck(mockUser, templateId);

    }


    /**
     * Test for creating/copying a new Deck for a user,
     * when that deck does not exist or is empty
     */
    @Test
    void getNewDeck_whenDeckReturnsEmpty_templateDeckNotFound() {

        String username = "testuser";
        long templateId = 99L;

        Usr mockUser = mock(Usr.class);
        when(authentication.getName()).thenReturn(username);
        when(userService.loadUserByUsername(username)).thenReturn(mockUser);

        when(deckService.getNewUserDeck(mockUser, templateId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deckController.createNewDeck(templateId))
                .isInstanceOf(BadRequestException.class);

        verify(authentication).getName();
        verify(userService).loadUserByUsername(username);
        verify(deckService).getNewUserDeck(mockUser, templateId);

    }

    /**
     * Test Case for updating Card
     */
    @Test
    void updateCard_whenCardIsUpdated() {

    }

    /**
     * Test Case for updating Deck
     */
    @Test
    void updateDeck_whenDeckIsUpdated() {

    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }
}
