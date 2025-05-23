package de.techfak.gse.template.domain;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing decks.
 */
public interface DeckService {
    List<Deck> getDecks();
    Optional<Deck> getDeck(String id);
    Deck addDeck(Boolean visibility, List<String> fieldOfLaw);
    Deck updateDeck(String id, Boolean visibility, List<String> fieldOfLaw);

}
