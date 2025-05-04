package de.techfak.gse.template.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service //<1>
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Card> getCards() {
        final List<Card> cards = new ArrayList<>();

        cardRepository.findAll().forEach(cards::add);  //<2>

        return cards;
    }

    @Override
    public Optional<Card> getCard(final String id) {
        final Long deckId = Long.valueOf(id); //<1>

        return cardRepository.findById(deckId); //<2>
    }
    @Override
    public Card addCard(final String content, final String card_type, final Deck deck) { //<1>
        final Card card = new Card(content, card_type, deck);
        return cardRepository.save(card);
    }

    @Override
    public Card updateCard(final String id, final String content, final String card_type, final Deck deck) { //<2>
        final Long cardId = Long.valueOf(id);
        final Card card = cardRepository.findById(cardId)
                .orElseThrow(); // hier muss eine Fehlermeldung rein

        card.setContent(content);
        card.setCard_type(card_type);
        card.setDeck(deck);

        return cardRepository.save(card);
    }
}
