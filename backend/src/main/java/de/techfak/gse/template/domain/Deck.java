package de.techfak.gse.template.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Deck {
    @Id
    private Long deck_id;
    @Column
    private Integer author_id;
    @Column
    private LocalDate publish_date;
    @Column
    private Boolean visibility;

    @ElementCollection
    @CollectionTable(name = "deck_field", joinColumns = @JoinColumn(name = "deck_id"))
    @Column(name = "field_of_law")
    private List<String> field_of_law;

    protected Deck() {}

    public Deck(Long deck_id, Integer author_id, LocalDate publish_date, Boolean visibility) {
        this.deck_id = deck_id;
        this.author_id = author_id;
        this.publish_date = publish_date;
        this.visibility = visibility;
    }
}
