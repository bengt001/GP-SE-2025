package de.techfak.gse.template.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


/**
 * Class for Notifications in Databank.
 */
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Notification {
    /**
     * Unique id of Notification.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User the Notification belongs to.
     */
    @ManyToOne
    @JoinColumn(updatable = false)
    @JsonManagedReference
    private Usr user;

    /**
     * Date the notification was created.
     */
    private String creationDate;

    /**
     * Type of message.
     */
    private String type;

    /**
     * Information if the message has been read, default is 'false'.
     */
    private boolean read;

    /**
     * Information of due decks and their number of due cards.
     */
    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DueDeckInfo> dueDecks;

    protected Notification() {
    }

    /**
     * Constructor for Notification.
     * @param user User that gets the notification
     * @param type Type of notification
     */
    public Notification(final Usr user, final String type) {
        this.user = user;
        this.type = type;
        this.read = false;
        this.creationDate = LocalDate.now().toString();
    }
}
