package de.techfak.gse.template.domain.implementation;

/**
 * CardIdDeckIdPair as pair of cardId with the right deckId to handle in frontend.
 */
public class CardIdDeckIdPair {
    private Long cardId;
    private Long deckId;

    public CardIdDeckIdPair(Long cardId, Long deckId) {
        this.cardId = cardId;
        this.deckId = deckId;
    }

    public Long getCardId() {
        return cardId;
    }

    public Long getDeckId() {
        return deckId;
    }
}
