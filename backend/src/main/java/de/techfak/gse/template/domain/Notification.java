package de.techfak.gse.template.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(updatable = false)
    @JsonManagedReference
    private Usr user;

    private String type;

    private boolean read;

    protected Notification() {}

    public Notification(Usr user, String type) {
        this.user = user;
        this.type = type;
        this.read = false;
    }

}
