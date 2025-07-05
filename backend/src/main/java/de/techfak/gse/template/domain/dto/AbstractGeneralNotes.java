package de.techfak.gse.template.domain.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGeneralNotes {
    @Getter
    @Setter
    Long id;

    @Getter
    @Setter
    String type;

    @Getter
    @Setter
    List<String> messages;

    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    boolean read;


    /**
     * Constructor for AbstractGeneralNotes.
     * @param id id of note
     * @param title title of note
     * @param read if note has been read
     */
    public AbstractGeneralNotes(Long id, String title, boolean read) {
        this.id = id;
        this.title = title;
        this.read = read;
        this.messages = new ArrayList<>();
    }
}
