package de.techfak.gse.template.domain.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for Notifiacitons.
 */
public abstract class AbstractGeneralNotes {
    /**
     * ID of Notification.
     */
    @Getter
    @Setter
    Long id;

    /**
     * Type of Notification.
     */
    @Getter
    @Setter
    String type;

    /**
     * List of messages, that will be displayed in v-card textbox.
     */
    @Getter
    @Setter
    List<String> messages;

    /**
     * String that will be displayed on v-card.
     */
    @Getter
    @Setter
    String title;

    /**
     * If note has been read.
     */
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
