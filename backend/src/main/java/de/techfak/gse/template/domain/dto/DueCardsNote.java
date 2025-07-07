package de.techfak.gse.template.domain.dto;

/**
 * NotificationDTO for Notes of Type "DUECARDS".
 */
public class DueCardsNote extends AbstractGeneralNotes {

    public DueCardsNote(Long id, String title, boolean read) {
        super(id, title, read);
        this.type = "DUECARDS";
    }
}
