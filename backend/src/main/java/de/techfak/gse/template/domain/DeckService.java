package de.techfak.gse.template.domain;

import java.util.List;
import java.util.Optional;

/**
 * Interface for a DeckService.
 */
public interface DeckService {

    List<Deck> getAllDecks();

    Optional<Deck> getDeckById(long id);

    List<Deck> getUserDecks(Usr usr);

    Optional<Deck> getUserDeckById(Usr usr, long id);

    List<Card> getCards(long deckId);

    Optional<Card> getCardById(long deckId,long id);

    List<Card> getUserCards(Usr usr, long deckId);
    Optional<Card> updateCard(Usr usr, long deckId, long cardId);

    Optional<Card> getUseCardById(Usr usr, long deckId,long id);

    Optional<Deck> getNewUserDeck(Usr usr, long templateDeckId);

    Optional<Deck> updateDeck(Usr usr, long deckId);
}
