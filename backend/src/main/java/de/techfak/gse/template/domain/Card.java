package de.techfak.gse.template.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Card {
    @Id
    private Long card_id;
    @Lob
    private String content;
    @Column
    private String card_type;
    @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;

    protected Card() {
    }

    public Card(String content, String card_type, Deck deck) {
        this.content = content;
        this.card_type = card_type;
        this.deck = deck;
    }
}


