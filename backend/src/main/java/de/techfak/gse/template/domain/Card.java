package de.techfak.gse.template.domain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {
    private Long card_id;
    private String content;
    private String card_type;

    protected Card() {}

    public Card (Long card_id, String content, String card_type) {
        this.card_id = card_id;
        this.content = content;
        this.card_type = card_type;
    }
}