package de.techfak.gse.template.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CardRating {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Usr user_id;

    @ManyToOne
    private Card card_id;

    @ManyToOne
    private Stapel stapel_id;

    @Embedded
    private Date last_date_rated;

    @Column
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