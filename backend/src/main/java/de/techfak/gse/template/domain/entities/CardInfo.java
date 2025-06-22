package de.techfak.gse.template.domain.entities;

import de.techfak.gse.template.domain.Rating;
import de.techfak.gse.template.domain.SraValues;
import jakarta.persistence.*;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity for description of the card rating.
 */
@Getter
@Setter
@Entity
public class CardInfo {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Usr userId;
    @ManyToOne
    private Card cardId;
    @ManyToOne
    private Deck deckId;
    @Column
    private LocalDate nextRepetition;
    @Lob
    private String editedContent;
    @Column
    private Rating rating;
    @Embedded
    private SraValues sraValues;

    protected CardInfo() {

    }

    /**
     * New card rating with specified content.
     *
     * @param userId
     * @param cardId
     * @param deckId
     * @param rating
     */
    public CardInfo(Usr userId, Card cardId, Deck deckId, Rating rating) {
        this.userId = userId;
        this.cardId = cardId;
        this.deckId = deckId;
        this.rating = rating;
        this.nextRepetition = LocalDate.now();
        this.sraValues =  new SraValues();
    }
}
