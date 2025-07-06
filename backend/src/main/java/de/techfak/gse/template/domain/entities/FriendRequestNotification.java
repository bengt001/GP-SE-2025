package de.techfak.gse.template.domain.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("FRIENDREQUEST")
public class FriendRequestNotification extends AbstractNotification {

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
