package de.techfak.gse.template.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Usr {

    private Long user_id;
    private String username;
    private String displayName;
    private String email;
    private Date creation_date;
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