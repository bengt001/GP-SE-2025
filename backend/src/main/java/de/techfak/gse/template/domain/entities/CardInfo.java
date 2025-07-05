package de.techfak.gse.template.domain.entities;

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
    private LocalDate lastDateRated;
    @Lob
    private String editedContent;
    @Column
    private int rating;

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
    public CardInfo(Usr userId, Card cardId, Deck deckId, int rating) {
        this.userId = userId;
        this.cardId = cardId;
        this.deckId = deckId;
        this.rating = rating;
        this.lastDateRated = LocalDate.now();
    }
}
