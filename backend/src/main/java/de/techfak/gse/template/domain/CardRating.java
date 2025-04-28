package de.techfak.gse.template.domain;

import jakarta.persistence.*;
import org.apache.catalina.User;
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
    private Stapel stapel_id;
    @Embedded
    private Date last_date_rated;
    @Column
    private int rating;

protected CardRating() {}

    public CardRating(Usr user_id, Card card_id, Stapel stapel_id, int rating) {
        this.user_id = user_id;
        this.card_id = card_id;
        this.stapel_id = stapel_id;
        this.rating = rating;
        this.last_date_rated = new Date();
    }



    public Usr getUser_id() {
        return user_id;
    }

    public void setUser_id(Usr user_id) {
        this.user_id = user_id;
    }

    public Card getCard_id() {
        return card_id;
    }

    public void setCard_id(Card card_id) {
        this.card_id = card_id;
    }

    public Stapel getStapel_id() {
        return stapel_id;
    }

    public void setStapel_id(Stapel stapel_id) {
        this.stapel_id = stapel_id;
    }

    public Date getLast_date_rated() {
        return last_date_rated;
    }

    public void setLast_date_rated(Date last_date_rated) {
        this.last_date_rated = last_date_rated;
    }
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}