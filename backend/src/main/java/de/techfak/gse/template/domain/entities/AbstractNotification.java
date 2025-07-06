package de.techfak.gse.template.domain.entities;

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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class AbstractNotification {
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
     * Information if the message has been read, default is 'false'.
     */
    private boolean read;

    protected AbstractNotification() {
    }

    /**
     * Constructor for Notification.
     * @param user User that gets the notification
     */
    public AbstractNotification(final Usr user) {
        this.user = user;
        this.read = false;
        this.creationDate = LocalDate.now().toString();
    }

    public abstract String getType();
}
