package de.techfak.gse.template.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    //JsonIgnore
    @ManyToOne
    @JoinColumn(name = "deckId")
    @JsonBackReference
    private Deck deck;

    protected Card() {
    }

    /**
     * New card with specified content.
     *
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


