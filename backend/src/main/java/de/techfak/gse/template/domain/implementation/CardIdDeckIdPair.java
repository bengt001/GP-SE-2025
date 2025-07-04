package de.techfak.gse.template.domain.implementation;

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
