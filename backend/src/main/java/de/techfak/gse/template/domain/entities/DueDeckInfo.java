package de.techfak.gse.template.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Table about the number of cards due in one Deck relating to one DueCardNote.
 */
@Getter
@Setter
@Entity
public class DueDeckInfo {
    /**
     * ID of DueDeckIno.
     */
    @Id
    @GeneratedValue
    private Long id;
    /**
     * Notification belonging to the due Cards in Deck.
     */
    @ManyToOne
    @JoinColumn(name = "notification_Id")
    private AbstractNotification notification;
    /**
     * DeckInfo of user with due Cards.
     */
    @ManyToOne
    @JoinColumn(name = "deck_Id")
    private Deck deck;

    /**
     * Number of due Cards in one Deck.
     */
    private int dueCardsCount;

    protected DueDeckInfo() {
    }

    /**
     * Constructor for DueDeckInfo.
     * @param notification Note the due Decks belong to
     * @param deck Deck with due Cards at the time the DueCardNote was generated
     * @param dueCardsCount number of due Cards in Deck
     */
    public DueDeckInfo(AbstractNotification notification, Deck deck, int dueCardsCount) {
        this.notification = notification;
        this.deck = deck;
        this.dueCardsCount = dueCardsCount;
    }
}
