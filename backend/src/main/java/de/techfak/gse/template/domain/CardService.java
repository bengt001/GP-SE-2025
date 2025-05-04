package de.techfak.gse.template.domain;

import java.util.List;
import java.util.Optional;

public interface CardService {

    Card addCard(String content, String card_type, Deck deck);

    Card updateCard(String id, String content, String card_type, Deck deck);

    List<Card> getCards();

    Optional<Card> getCard(String id);
}