package de.techfak.gse.template.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Usr {
    @Id
    @Column
    private Long user_id;
    @Column
    private String username;
    @Column
    private String displayName;
    @Column
    private String email;
    @Embedded
    private Date creation_date;
    @OneToMany(mappedBy = "user_id")
    private List<CardRating> ratings;

    protected Usr() {
    }

    public Usr(final Long user_id, String username, final String displayName, final String email, final Date creation_date) {
        this.user_id = user_id;
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.creation_date = creation_date;
    }
}