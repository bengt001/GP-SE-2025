package de.techfak.gse.template.domain;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stapel {

    private Long stapel_id;
    private Integer author_id;
    private Date publish_date;
    private Boolean visibility;

    protected Stapel() {
    }

    public Stapel(Long stapel_id, Integer author_id, Date publish_date, Boolean visibility) {
        this.stapel_id = stapel_id;
        this.author_id = author_id;
        this.publish_date = publish_date;
        this.visibility = visibility;
    }
}