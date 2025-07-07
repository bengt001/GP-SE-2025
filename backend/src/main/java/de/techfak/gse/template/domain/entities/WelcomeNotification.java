package de.techfak.gse.template.domain.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Subclass of AbstractNotification for Notifications of Type "WELCOME".
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("WELCOME")
public class WelcomeNotification extends AbstractNotification {
    protected WelcomeNotification() {
    }

    public WelcomeNotification(Usr user) {
        super(user);
    }

    @Override
    public String getType() {
        return "WELCOME";
    }
}
