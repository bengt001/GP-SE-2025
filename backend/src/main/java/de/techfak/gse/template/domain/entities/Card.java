package de.techfak.gse.template.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entity representing a card.
 */
@Getter
@Setter
@Entity
public class Card implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cardId;
    @Lob
    private String content;
    @Column
    private String cardType;
    @Column
    private String title;

    //JsonIgnore
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "deckId")
    private Deck deck;

    /**
     * Instantiates a new Card.
     */
    protected Card() {
    }

    /**
     * New card with specified content.
     *
     * @param content  the content
     * @param cardType the card type
     * @param deck     the deck
     * @param title    the title
     */
    public Card(String content, String cardType, Deck deck, String title) {
        this.content = content;
        this.cardType = cardType;
        this.deck = deck;
        this.title = title;
    }

}


