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
    private final DeckRepository deckRepository;

    public DeckServiceImpl(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    @Override
    public Optional<Deck> getDeck(Long id) {
        return deckRepository.findById(id);
    }

    @Override
    public Deck addDeck(Boolean visibility, List<String> fieldOfLaw) {
        final Deck deck = new Deck(visibility, fieldOfLaw);
        return deckRepository.save(deck);
    }

    @Override
    public Deck updateDeck(Long id, Boolean visibility, List<String> fieldOfLaw) {
        Optional<Deck> deck = deckRepository.findById(id);
        if (deck.isPresent()) {
            deck.get().setVisibility(visibility);
            deck.get().setFieldOfLaw(fieldOfLaw);
            return deckRepository.save(deck.get());
        }
        return null;
    }

    @Override
    public List<Deck> getAllDecks() {
        List<Deck> decks = new ArrayList<>();
        deckRepository.findAll().forEach(decks::add);
        return decks;
    }

    @Override
    public Optional<Deck> getDeckById(long id) {
        return deckRepository.findById(id);
    }

    @Override
    public List<Deck> getUserDecks(Usr usr) {
        return usr.getDecks();
    }

    @Override
    public Optional<Deck> getUserDeckById(Usr usr, long id) {
        return deckRepository.findDeckByIdAndUserId(id,usr.getUserId());
    }

    @Override
    public List<Card> getCards(long deckId) {
        Optional<Deck> deck = getDeckById(deckId);
        if (deck.isPresent()) {
            return deck.get().getCards();
        }
        return List.of();
    }

    @Override
    public Optional<Card> getCardByIdFromDeck(long deckId, long id) {
        List<Card> cards = getCards(deckId);
        for (Card card : cards) {
            if(card.getCardId() == id) {
                return Optional.of(card);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Card> getUserCards(Usr usr, long deckId) {
        Optional<Deck> deck = getUserDeckById(usr,deckId);
        if (deck.isPresent()) {
            Deck d = deck.get();
            return d.getCards();
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Card> updateCard(Usr usr, long deckId, long cardId, Card updatedCard) {
        List<Card> cards = getUserCards(usr,deckId);
        for (Card card : cards) {
            if(card.getCardId() == cardId) {
                card.setCardId(cardId);
                card.setCardType(updatedCard.getCardType());
                card.setDeck(updatedCard.getDeck());
                card.setContent(updatedCard.getContent());
                return Optional.of(card);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Card> getUseCardById(Usr usr, long deckId, long id) {
        List<Card> cards = getUserCards(usr,deckId);
        for (Card card : cards) {
            if(card.getCardId() == id) {
                return Optional.of(card);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Deck> getNewUserDeck(Usr usr, long templateDeckId) {
        return Optional.empty();
    }

    @Override
    public Optional<Deck> updateDeck(Usr usr, long deckId, Deck updatedDeck) {
        Optional<Deck> deck = getUserDeckById(usr,deckId);
        if (deck.isPresent()) {
            deck.get().setVisibility(updatedDeck.getVisibility());
            deck.get().setFieldOfLaw(updatedDeck.getFieldOfLaw());
            return Optional.of(deckRepository.save(deck.get()));
        }
        return Optional.empty();
    }
}
