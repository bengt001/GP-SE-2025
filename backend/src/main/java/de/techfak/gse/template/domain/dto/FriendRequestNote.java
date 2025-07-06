package de.techfak.gse.template.domain.dto;

import de.techfak.gse.template.domain.entities.Usr;

/**
 * DTO for Notifications of type "FRIENDREQUEST"
 */
public class FriendRequestNote extends AbstractGeneralNotes {

    /**
     * Constructor for AbstractGeneralNotes.
     *
     * @param id    id of note
     * @param read  if note has been read
     */
    public FriendRequestNote(Long id, boolean read, Usr requester) {
        super(id, "Neue Freundschaftsanfrage!", read);
        this.messages.add(requester.getUsername() + " hat dir eine Freundschaftsanfrage geschickt!");
    }
}
