package de.techfak.gse.template.domain;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing cards.
 */
public interface CardService {

    Card addCard(String content, String cardType, Deck deck);

    Card updateCard(Long id, String content, String cardType, Deck deck);

    List<Card> getCards();

    Optional<Card> getCard(Long id);

    List<Card> getCardsByDeckId(Long deckId);
}
