package de.techfak.gse.template.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.techfak.gse.template.domain.SraValues;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
   /*@Column
    private LocalDate nextRevision;
    @Column
    private int rating;
    @Embedded
    private SraValues sraValues;*/

    //JsonIgnore
    @ManyToOne
    @JoinColumn(name = "deckId")
    @JsonBackReference
    private Deck deck;

    protected Card() {
    }

    /**
     * New card with specified content.
     * The Values for the SRA (nextRevision, rating, sraValues) are set to the standard values for a new card.
     * This means to nextRevision: now, rating: -1, sraValues (0,0,250).
     *
     * @param content
     * @param cardType
     * @param deck
     */
    public Card(String content, String cardType, Deck deck) {
        this.content = content;
        this.cardType = cardType;
        this.deck = deck;
        /*this.nextRevision = LocalDate.now();
        this.rating = -1;
        this.sraValues = new SraValues();*/
    }

}


