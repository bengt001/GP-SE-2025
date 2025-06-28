package de.techfak.gse.template.domain.entities;

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
    @Column
    private String title;
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
     * The Values for the SRA (nextRevision, rating, sraValues) are set to the standard values for a new card.
     * This means to nextRevision: now, rating: -1, sraValues (0,0,250).
     * Kirill: I dont think thats true anymore
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
        /*this.nextRevision = LocalDate.now();
        this.rating = -1;
        this.sraValues = new SraValues();*/
    }

}


