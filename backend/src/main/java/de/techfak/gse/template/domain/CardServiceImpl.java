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

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Card> getCards() {
        final List<Card> cards = new ArrayList<>();

        cardRepository.findAll().forEach(cards::add);

        return cards;
    }

    @Override
    public Optional<Card> getCard(Long id) {
        return cardRepository.findById(id);
    }

    @Override
    public Card addCard(final String content, final String cardType, final Deck deck) {
        final Card card = new Card(content, cardType, deck);
        return cardRepository.save(card);
    }

    @Override
    public Card updateCard(final Long id, final String content, final String cardType, final Deck deck) {
        final Card card = cardRepository.findById(id)
                .orElseThrow(BadRequestException::new);
        // hier muss eine Fehlermeldung rein

        card.setContent(content);
        card.setCardType(cardType);
        card.setDeck(deck);

        return cardRepository.save(card);
    }

    @Override
    public List<Card> getCardsByDeckId(Long deckId) {
        List<Card> result = new ArrayList<>();

        for (Card card : cardRepository.findAll()) {
            if (card.getDeck() != null && deckId.equals(card.getDeck().getDeckId())) {
                result.add(card);
            }
        }

        return result;
    }
}
