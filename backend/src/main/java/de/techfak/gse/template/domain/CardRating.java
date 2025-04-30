package de.techfak.gse.template.domain;

import jakarta.persistence.*;
import org.apache.catalina.User;

import java.time.LocalDate;

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
    @Embedded
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

    public Deck getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(Deck deck_id) {
        this.deck_id = deck_id;
    }

    public LocalDate getLast_date_rated() {
        return last_date_rated;
    }

    public void setLast_date_rated(LocalDate last_date_rated) {
        this.last_date_rated = last_date_rated;
    }
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void updateDate() {
    this.last_date_rated = LocalDate.now();
    }
}