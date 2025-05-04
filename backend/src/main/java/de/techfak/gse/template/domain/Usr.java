package de.techfak.gse.template.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
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
    @Column
    private LocalDate creation_date;
    @OneToMany(mappedBy = "user_id")
    private List<CardRating> ratings;

    protected Usr() {}

    public Usr(String username, final String displayName, final String email, final LocalDate creation_date) {
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.creation_date = creation_date;
    }
}