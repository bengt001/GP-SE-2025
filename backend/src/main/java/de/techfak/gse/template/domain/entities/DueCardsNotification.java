package de.techfak.gse.template.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Subclass of AbstractNotification for Notifications of Type "DUECARDS".
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("DUECARDS")
public class DueCardsNotification extends AbstractNotification {
    /**
     * Information of due decks and their number of due cards.
     */
    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DueDeckInfo> dueDecks;

    protected DueCardsNotification() {
    }

    public DueCardsNotification(Usr user) {
        super(user);
    }

    @Override
    public String getType() {
        return "DUECARDS";
    }
}
