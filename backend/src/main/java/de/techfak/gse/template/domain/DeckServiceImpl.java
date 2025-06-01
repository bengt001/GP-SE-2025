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
public class DeckServiceImpl implements DeckService {
    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;

    @Autowired
    public DeckServiceImpl(DeckRepository deckRepository, CardRepository cardRepository) {
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Deck> getAllDecks() {
        final List<Deck> decks = new ArrayList<>();

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
        return deckRepository.findDeckByIdAndUserId(id, usr.getUserId());
    }

    @SuppressWarnings("checkstyle:TrailingComment")
    @Override
    public List<Card> getCards(long deckId) { //KIRILL: muss hier ein optional davor?
        return deckRepository.findById(deckId).map(Deck::getCards)
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<Card> getCardByIdFromDeck(long deckId, long cardId) {
        return cardRepository.findCardByIdAndDeckId(cardId, deckId);
    }

    @Override
    public List<Card> getUserCards(Usr usr, long deckId) {
        return getCards(deckId);
    }


    //KIRILL: I would never let the user touch the cards.
    //KIRILL: If this is meant to update card Rating this can be implemented
    @Override
    public Optional<Card> updateCard(Usr usr, long deckId, long cardId, Card updatedCard) {
        Optional<Card> tempCard = cardRepository.findCardByIdAndDeckId(cardId, deckId);
        return tempCard.map(card -> {
            card.setCardType(updatedCard.getCardType());
            card.setContent(updatedCard.getContent());
            return cardRepository.save(card);
        });
    }

    //KIRILL: users dont have unique cards?
    @Override
    public Optional<Card> getUseCardById(Usr usr, long deckId, long cardId) {
        return cardRepository.findCardByIdAndDeckId(cardId, deckId);
    }

    //KIRILL: I  dont even know what this is supposed to do.
    //KIRILL: Why would a user create a deck
    @Override
    public Optional<Deck> getNewUserDeck(Usr usr, long templateDeckId) {
        return getUserDeckById(usr, templateDeckId);
    }


    @Override
    public Optional<Deck> updateDeck(Usr usr, long deckId, Deck updatedDeck) {
        Optional<Deck> tempDeck = deckRepository.findById(deckId);
        return tempDeck.map(deck -> {
            deck.setVisibility(updatedDeck.getVisibility());
            deck.setFieldOfLaw(updatedDeck.getFieldOfLaw());
            deck.setCards(updatedDeck.getCards());
            return deckRepository.save(deck);
        });
    }

    //##########################################################################
    //Ab hier sind die OG Dennis Funktionen
    @Override
    public Optional<Deck> getDeck(final Long id) {
        return deckRepository.findById(id);
    }

    @Override
    public Deck addDeck(final Boolean visibility, final List<String> fieldOfLaw, int deckId) {
        final Deck deck = new Deck(visibility, fieldOfLaw, deckId);
        return deckRepository.save(deck);
    }

    @Override
    public Deck updateDeck(final Long id, final Boolean visibility, final List<String> fieldOfLaw) {
        final Long deckId = Long.valueOf(id);
        final Deck deck = deckRepository.findById(deckId)
                .orElseThrow(BadRequestException::new);
        // hier muss eine Fehlermeldung rein
        deck.setVisibility(visibility);
        deck.setFieldOfLaw(fieldOfLaw);

        return deckRepository.save(deck);
    }


    @Override
    public Optional<Deck> deleteDeck(long deckId) {
        Optional<Deck> tempDeck = deckRepository.findById(deckId);
        if (tempDeck.isPresent()) {
            deckRepository.delete(tempDeck.get());
            return tempDeck;
        }
        return Optional.empty();
    }

}
