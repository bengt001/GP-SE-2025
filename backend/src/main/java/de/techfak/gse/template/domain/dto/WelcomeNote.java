package de.techfak.gse.template.domain.dto;

public class WelcomeNote extends AbstractGeneralNotes {

    public WelcomeNote(Long id, boolean read) {
        super(id, "Herzlich Willkommen!", read);
        this.type = "WELCOME";
        this.messages.add("Herzlich Willkommen zu LexArtes."
                + " Hier kannst du all deine Schemata, Definitionen und Probleme lernen.");
    }
}
