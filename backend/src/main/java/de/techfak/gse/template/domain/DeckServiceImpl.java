package de.techfak.gse.template.domain;

import de.techfak.gse.template.web.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation for the deck service.
 */
@Service
public class DeckServiceImpl implements DeckService{
    @Override
    public Optional<Deck> getDeck(Long id) {
        return Optional.empty();
    }

    @Override
    public Deck addDeck(Boolean visibility, List<String> fieldOfLaw) {
        return null;
    }

    @Override
    public Deck updateDeck(Long id, Boolean visibility, List<String> fieldOfLaw) {
        return null;
    }

    @Override
    public List<Deck> getAllDecks() {
        return List.of();
    }

    @Override
    public Optional<Deck> getDeckById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Deck> getUserDecks(Usr usr) {
        return List.of();
    }

    @Override
    public Optional<Deck> getUserDeckById(Usr usr, long id) {
        return Optional.empty();
    }

    @Override
    public List<Card> getCards(long deckId) {
        return List.of();
    }

    @Override
    public Optional<Card> getCardByIdFromDeck(long deckId, long id) {
        return Optional.empty();
    }

    @Override
    public List<Card> getUserCards(Usr usr, long deckId) {
        return List.of();
    }

    @Override
    public Optional<Card> updateCard(Usr usr, long deckId, long cardId, Card updatedCard) {
        return Optional.empty();
    }

    @Override
    public Optional<Card> getUseCardById(Usr usr, long deckId, long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Deck> getNewUserDeck(Usr usr, long templateDeckId) {
        return Optional.empty();
    }

    @Override
    public Optional<Deck> updateDeck(Usr usr, long deckId, Deck updatedDeck) {
        return Optional.empty();
    }
}
