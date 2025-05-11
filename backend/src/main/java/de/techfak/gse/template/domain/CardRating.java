package de.techfak.gse.template.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CardRating {
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
    @Column
    private int rating;

protected CardRating() {

}

    /**
     * @param user_id
     * @param cardId
     * @param deckId
     * @param rating
     */
    public CardRating(Usr user_id, Card cardId, Deck deckId, int rating) {
        this.userId = user_id;
        this.cardId = cardId;
        this.deckId = deckId;
        this.rating = rating;
        this.lastDateRated = LocalDate.now();
    }
}
