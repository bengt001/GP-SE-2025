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

    /**
     * Instantiates a new Deck.
     */
    protected Deck() {

    }

    /**
     * New deck with specified content.
     *
     * @param visibility the visibility
     * @param fieldOfLaw the field of law
     * @param authorId   the author id
     */
    public Deck(Boolean visibility, List<String> fieldOfLaw, int authorId) {
        this.visibility = visibility;
        this.fieldOfLaw = fieldOfLaw;
        this.authorId = authorId;
        this.publishDate = LocalDate.now();
    }

    /**
     * Instantiates a new Deck.
     *
     * @param visibility the visibility
     * @param fieldOfLaw the field of law
     */
    public Deck(Boolean visibility, List<String> fieldOfLaw) {
        this.visibility = visibility;
        this.fieldOfLaw = fieldOfLaw;
        this.publishDate = LocalDate.now();
    }

    /**
     * Update date.
     */
    public void updateDate() {
        this.publishDate = LocalDate.now();
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return cards == null || cards.isEmpty();
    }
}
