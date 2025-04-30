package de.techfak.gse.template.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRating {

    private Long id;
    private Usr user_id;
    private Card card_id;
    private Stapel stapel_id;
    private Date last_date_rated;
    private int rating;

    protected CardRating() {
    }

    public CardRating(Usr user_id, Card card_id, Stapel stapel_id, int rating) {
        this.user_id = user_id;
        this.card_id = card_id;
        this.stapel_id = stapel_id;
        this.rating = rating;
        this.last_date_rated = new Date();
    }
}