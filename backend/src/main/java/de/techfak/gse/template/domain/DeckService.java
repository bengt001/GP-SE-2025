package de.techfak.gse.template.domain;

import java.util.Dictionary;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing decks.
 */
public interface DeckService {
    Optional<Deck> getDeck(Long id);

    Deck addDeck(Boolean visibility, List<String> fieldOfLaw, int userId);

    Deck updateDeck(Long id, Boolean visibility, List<String> fieldOfLaw);


    List<Deck> getAllDecks();

    Optional<Deck> getDeckById(long id);

    List<Deck> getUserDecks(Usr usr);

    Optional<Deck> getUserDeckById(Usr usr, long id);

    List<Card> getCards(long deckId);

    Optional<Card> getCardByIdFromDeck(long deckId, long id);

    List<Card> getUserCards(Usr usr, long deckId);

    Optional<Card> updateCard(Usr usr, long deckId, long cardId, Card updatedCard);

    Optional<Card> getUseCardById(Usr usr, long deckId, long id);

    Optional<Deck> getNewUserDeck(Usr usr, long templateDeckId);

    Optional<Deck> updateDeck(Usr usr, long deckId, Deck updatedDeck);

    Optional<Deck> deleteDeck(long deckId);

    Optional<CardInfo> rankCard(Usr usr, long deckId, long cardId, Rating rating);

    // Dictionary<Rating, Long> getDeckInfo(Usr usr, long deckId);
}
