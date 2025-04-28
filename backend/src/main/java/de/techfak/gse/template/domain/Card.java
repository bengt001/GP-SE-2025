package de.techfak.gse.template.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Card {
    @Id
    private Long card_id;
    @Lob
    private String content;
    @Column
    private String card_type;

protected Card() {}

    public Card (Long card_id, String content, String card_type) {
        this.card_id = card_id;
        this.content = content;
        this.card_type = card_type;
    }

    public Long getCard_id() {
        return card_id;
    }

    public void setCard_id(Long card_id) {
        this.card_id = card_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }
}
