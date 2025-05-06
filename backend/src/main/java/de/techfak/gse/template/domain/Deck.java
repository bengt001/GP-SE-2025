package de.techfak.gse.template.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public Deck(Boolean visibility, List<String> field_of_law) {
        this.visibility = visibility;
        this.field_of_law = field_of_law;
        this.publish_date = LocalDate.now();
    }
    public void updateDate() {
        this.publish_date = LocalDate.now();
    }
}
