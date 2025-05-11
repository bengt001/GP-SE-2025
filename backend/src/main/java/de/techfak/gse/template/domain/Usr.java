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
    private Long userId;
    @Column
    private String username;
    @Column
    private String displayName;
    @Column
    private String email;
    @Column
    private LocalDate creationDate;
    @OneToMany(mappedBy = "userId")
    private List<CardRating> ratings;

    protected Usr() {

    }

    /**
     * @param username
     * @param displayName
     * @param email
     * @param creationDate
     */
    public Usr(String username, final String displayName, final String email, final LocalDate creationDate) {
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.creationDate = creationDate;
    }
}
