package de.techfak.gse.template.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

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
    @Embedded
    private LocalDate creation_date;
    @OneToMany(mappedBy = "user_id")
    private List<CardRating> ratings;

    protected Usr() {}

    public Usr(final Long user_id, String username, final String displayName, final String email, final LocalDate creation_date) {
        this.user_id = user_id;
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.creation_date = creation_date;
    }
    public void setId(final Long id) {
        this.user_id = id;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setCreation_date(final LocalDate creation_date) {
        this.creation_date = creation_date;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getCreation_date() {
        return creation_date;
    }

    public Long getId() {
        return user_id;
    }
}