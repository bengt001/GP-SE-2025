package de.techfak.gse.template.domain;

import de.techfak.gse.template.web.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the Card Service.
 */
@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Card addCard(String content, String cardType, Deck deck) {
        final Card card = new Card(content, cardType, deck);
        return cardRepository.save(card);
    }

    @Override
    public Card updateCard(Long id, String content, String cardType, Deck deck) {
        Optional<Card> card = cardRepository.findById(id);
        if (card.isPresent()) {
            card.get().setContent(content);
            card.get().setCardType(cardType);
            card.get().setDeck(deck);
            return cardRepository.save(card.get());
        }
        return null;
    }

    @Override
    public List<Card> getCards() {
        List<Card> cards = new ArrayList<>();
        cardRepository.findAll().forEach(cards::add);
        return cards;
    }

    @Override
    public Optional<Card> getCard(Long id) {
        return cardRepository.findById(id);
    }

    @Override
    public List<Card> getCardsByDeckId(Long deckId) {
        List<Card> cards = new ArrayList<>();
        List<Card> correctCards = new ArrayList<>();
        cardRepository.findAll().forEach(cards::add);
        for (Card card : cards) {
            if(card.getCardId().equals(deckId)) {
                correctCards.add(card);
            }
        }
        return correctCards;
    }
}
