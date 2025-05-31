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

    @Override
    public Card addCard(String content, String cardType, Deck deck) {
        return null;
    }

    @Override
    public Card updateCard(Long id, String content, String cardType, Deck deck) {
        return null;
    }

    @Override
    public List<Card> getCards() {
        return List.of();
    }

    @Override
    public Optional<Card> getCard(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Card> getCardsByDeckId(Long deckId) {
        return List.of();
    }
}
