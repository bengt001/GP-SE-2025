package de.techfak.gse.template.domain.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
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

    /**
     * Instantiates a new Card info.
     */
    protected CardInfo() {

    }


    /**
     * Instantiates a new Card info.
     *
     * @param userId the user id
     * @param cardId the card id
     * @param deckId the deck id
     * @param rating the rating
     */
    public CardInfo(Usr userId, Card cardId, Deck deckId, Rating rating) {
        this.userId = userId;
        this.cardId = cardId;
        this.deckId = deckId;
        this.rating = rating;
        this.nextRepetition = LocalDate.now();
        this.sraValues = new SraValues();
    }

    /**
     * Gets deck id value.
     *
     * @return the deck id value
     */
    @JsonProperty("deckId")
    public Long getDeckIdValue() {
        return deckId != null ? deckId.getDeckId() : null;
    }

    /**
     * Gets card id value.
     *
     * @return the card id value
     */
    @JsonProperty("cardId")
    public Long getCardIdValue() {
        return cardId != null ? cardId.getCardId() : null;
    }

    /**
     * Gets user id value.
     *
     * @return the user id value
     */
    @JsonProperty("userId")
    public String getUserIdValue() {
        return userId != null ? userId.getUserId() : null;
    }
}
