package de.techfak.gse.template.domain;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Stapel {
    @Id
    private Long stapel_id;
    @Column
    private Integer author_id;
    @Embedded
    private Date publish_date;
    @Column
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