package de.techfak.gse.template.domain.implementation;

import de.techfak.gse.template.domain.entities.Card;
import de.techfak.gse.template.domain.entities.CardInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Card info card dto.
 */
@Setter
@Getter
public class CardInfoCardDTO {
    // Getters and setters
    private CardInfo cardInfo;
    private Card card;

    /**
     * Instantiates a new Card info card dto.
     *
     * @param cardInfo the card info
     * @param card     the card
     */
    public CardInfoCardDTO(CardInfo cardInfo, Card card) {
        this.cardInfo = cardInfo;
        this.card = card;
    }
}
