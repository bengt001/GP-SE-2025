package de.techfak.gse.template.domain.dto;

/**
 * Class for Notifications of type "WELCOME".
 */
public class WelcomeNote extends AbstractGeneralNotes {

    /**
     * Constructor for WelcomeNotes.
     * @param id id of note
     * @param read read-status of note
     */
    public WelcomeNote(Long id, boolean read) {
        super(id, "Herzlich Willkommen!", read);
        this.type = "WELCOME";
        this.messages.add("Willkommen zu LexArtes."
                + " Hier kannst du all deine Schemata, Definitionen und Probleme lernen.");
        this.messages.add("Willkommen zu LexArtes.");
    }
}
