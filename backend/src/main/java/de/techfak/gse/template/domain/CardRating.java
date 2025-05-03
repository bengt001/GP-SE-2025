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
    private Usr user_id;
    @ManyToOne
    private Card card_id;
    @ManyToOne
    private Deck deck_id;
    @Column
    private LocalDate last_date_rated;
    @Column
    private int rating;

protected CardRating() {}

    public CardRating(Usr user_id, Card card_id, Deck deck_id, int rating) {
        this.user_id = user_id;
        this.card_id = card_id;
        this.deck_id = deck_id;
        this.rating = rating;
        this.last_date_rated = LocalDate.now();
    }
}