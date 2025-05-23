package de.techfak.gse.template.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a card.
 */
@Getter
@Setter
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cardId;
    @Lob
    private String content;
    @Column
    private String cardType;
    @ManyToOne
    @JoinColumn(name = "deckId")
    private Deck deck;

    protected Card() {
    }

    /**
     * New card with specified content.
     * @param content
     * @param cardType
     * @param deck
     */
    public Card(String content, String cardType, Deck deck) {
        this.content = content;
        this.cardType = cardType;
        this.deck = deck;
    }
}


