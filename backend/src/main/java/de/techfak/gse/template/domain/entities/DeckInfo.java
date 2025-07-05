package de.techfak.gse.template.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type Deck info.
 */
@Getter
@Setter
@Entity
public class DeckInfo {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Usr userId;
    @ManyToOne
    private Deck deckId;
    //Kirill: 0 false, 1 true, maybe
    @Column
    private Boolean visibility;

    /**
     * Relation to number of due Cards in Deck for one user on one day.
     */
    @OneToMany(mappedBy = "deckInfo",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DueDeckInfo> dueDeckInfos;


    /**
     * Instantiates a new Deck info.
     */
    protected DeckInfo() {

    }

    /**
     * Instantiates a new Deck info.
     *
     * @param userId     the user id
     * @param deckId     the deck id
     * @param visibility the visibility
     */
    public DeckInfo(Usr userId, Deck deckId, boolean visibility) {
        this.userId = userId;
        this.deckId = deckId;
        this.visibility = visibility;
    }
}
