package de.techfak.gse.template.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


/**
 * Class for Notifications in Databank.
 */
@Getter
@Setter
@Entity
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
