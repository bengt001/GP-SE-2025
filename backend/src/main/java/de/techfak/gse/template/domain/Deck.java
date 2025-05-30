package de.techfak.gse.template.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a deck.
 */
@Getter
@Setter
@Entity
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long deckId;
    @Column
    private Integer authorId;
    @Column
    private LocalDate publishDate;
    @Column
    private Boolean visibility;

    @OneToMany(mappedBy = "deck")
    private List<Card> cards;

    @ManyToMany
    @JoinTable(
            name = "deck_user",
            joinColumns = @JoinColumn(name = "deck_Id"),
            inverseJoinColumns = @JoinColumn(name = "user_Id")
    )
    private List<Usr> users = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "deckField", joinColumns = @JoinColumn(name = "deckId"))
    @Column(name = "fieldOfLaw")
    private List<String> fieldOfLaw;

    protected Deck() {

    }

    /**
     * New deck with specified content.
     *
     * @param visibility
     * @param fieldOfLaw
     */
    public Deck(Boolean visibility, List<String> fieldOfLaw) {
        this.visibility = visibility;
        this.fieldOfLaw = fieldOfLaw;
        this.publishDate = LocalDate.now();
    }

    public void updateDate() {
        this.publishDate = LocalDate.now();
    }

    public boolean isEmpty() {
        return cards == null || cards.isEmpty();
    }
}
