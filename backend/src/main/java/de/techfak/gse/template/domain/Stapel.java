package de.techfak.gse.template.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Stapel {
    @Id
    private Long stapel_id;
    @Column
    private Integer author_id;
    @Embedded
    private Date publish_date;
    @Column
    private Boolean visibility;

protected Stapel() {}

    public Stapel(Long stapel_id, Integer author_id, Date publish_date, Boolean visibility) {
        this.stapel_id = stapel_id;
        this.author_id = author_id;
        this.publish_date = publish_date;
        this.visibility = visibility;
    }
    public Long getId() {
        return stapel_id;
    }
    public void setId(Long id) {
        this.stapel_id = id;
    }
    public Integer getAuthor_id() {
        return author_id;
    }
    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public Date getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(Date publish_date) {
        this.publish_date = publish_date;
    }
    public Boolean getVisibility() {
        return visibility;
    }
    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }
}
