package de.techfak.gse.template.domain.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * Subclass of AbstractNotification for Notifications of Type "FRIENDREQUEST".
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("FRIENDREQUEST")
public class FriendRequestNotification extends AbstractNotification {

    /**
     * User who send the request.
     */
    @OneToOne
    private Usr requester;

    protected FriendRequestNotification() {
    }

    public FriendRequestNotification(Usr recipient, Usr requester) {
        super(recipient);
        this.requester = requester;
    }

    @Override
    public String getType() {
        return "FRIENDREQUEST";
    }
}
