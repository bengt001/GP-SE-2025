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
    @Column
    private String ueberschrift;
   /*@Column
    private LocalDate nextRevision;
    @Column
    private int rating;
    @Embedded
    private SraValues sraValues;*/

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
     * @param content      the content
     * @param cardType     the card type
     * @param deck         the deck
     * @param title        the title
     * @param ueberschrift the ueberschrift
     */
    public Card(String content, String cardType, Deck deck, String title, String ueberschrift) {
        this.content = content;
        this.cardType = cardType;
        this.deck = deck;
        this.title = title;
        this.ueberschrift = ueberschrift;
        /*this.nextRevision = LocalDate.now();
        this.rating = -1;
        this.sraValues = new SraValues();*/
    }

}


