package de.techfak.gse.template.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

public class Deck {
    private Long deck_id;
    private Integer author_id;
    private LocalDate publish_date;
    private Boolean visibility;

    protected Deck() {}

    public Deck(Long deck_id, Integer author_id, LocalDate publish_date, Boolean visibility) {
        this.deck_id = deck_id;
        this.author_id = author_id;
        this.publish_date = publish_date;
        this.visibility = visibility;
    }
    public Long getId() {
        return deck_id;
    }
    public void setId(Long id) {
        this.deck_id = id;
    }
    public Integer getAuthor_id() {
        return author_id;
    }
    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public LocalDate getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(LocalDate publish_date) {
        this.publish_date = publish_date;
    }
    public Boolean getVisibility() {
        return visibility;
    }
    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }
}
