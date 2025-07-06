package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.dto.AbstractGeneralNotes;
import de.techfak.gse.template.domain.dto.DueCardsNote;
import de.techfak.gse.template.domain.dto.FriendRequestNote;
import de.techfak.gse.template.domain.dto.WelcomeNote;
import de.techfak.gse.template.domain.entities.DueDeckInfo;
import de.techfak.gse.template.domain.entities.AbstractNotification;
import de.techfak.gse.template.domain.entities.FriendRequestNotification;
import de.techfak.gse.template.domain.entities.Usr;
import de.techfak.gse.template.domain.service.NotificationService;
import de.techfak.gse.template.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * API für Notifications.
 */
@RestController
@RequestMapping("/api")
public class NotificationController {

    /**
     * Service that sends notifications to users.
     */
    private final NotificationService notificationService;
    /**
     * Service that manages interactions with users.
     */
    private final UserService userService;


    /**
     * Constructor for NotificationController.
     * @param notificationService Service that handles Notifications
     * @param userService Service that handles User information
     */
    @Autowired
    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;

    }

    /**
     * API Endpoint to get all Notifications of a User.
     * @return List of Notifications for one User.
     */
    @GetMapping("/notification")
    @Secured("ROLE_USER")
    public List<AbstractGeneralNotes> getNotifications() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr user = userService.loadUserByUsername(auth.getName());

        List<AbstractNotification> notifications = notificationService.getNotificationByUser(
                userService.loadUserById(user.getUserId()));
        List<AbstractGeneralNotes> notificationsDTO = new ArrayList<>();

        for  (AbstractNotification note : notifications) {
            if (note.getType().equals("DUECARDS")) {
                List<DueDeckInfo> dueDeckInfo = notificationService.getDueDeckInfos(note);
                int allDueCards = 0;

                List<String> messages = new ArrayList<>();
                HashMap<String, Integer> dueCards = new HashMap<>();
                for (DueDeckInfo info : dueDeckInfo) {
                    String fieldOfLaw = info.getDeckInfo().getDeckId().getFieldOfLaw().getLast();
                    if (!dueCards.containsKey(fieldOfLaw)) {
                        dueCards.put(fieldOfLaw, info.getDueCardsCount());
                    } else {
                        dueCards.put(fieldOfLaw, dueCards.get(fieldOfLaw) + info.getDueCardsCount());
                    }
                    allDueCards += info.getDueCardsCount();
                }

                for (String fieldOfLaw : dueCards.keySet()) {
                    messages.add(fieldOfLaw + ": " + dueCards.get(fieldOfLaw) + " Karten sind fällig.");
                }

                if (!messages.isEmpty()) {
                    DueCardsNote dueCardsNote = new DueCardsNote(note.getId(), "Heute sind " + allDueCards
                            + " Karten fällig!", note.isRead());

                    dueCardsNote.setMessages(messages);
                    notificationsDTO.add(dueCardsNote);
                }

            } else if (note.getType().equals("WELCOME")) {
                notificationsDTO.add(new WelcomeNote(note.getId(), note.isRead()));
            } else if (note.getType().equals("FRIENDREQUEST")) {
                Usr requester = ((FriendRequestNotification) note).getRequester();
                notificationsDTO.add(new FriendRequestNote(note.getId(), note.isRead(), requester));
            }
        }
        return notificationsDTO;
    }

    /**
     * Marks a notification as read.
     * @param id id of the notifications that has been read
     * @return {@code 200 OK} if successfully marked as read, else {@code 404 Not Found}
     */
    @PutMapping("/notification/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        boolean success = notificationService.markNotificationAsRead(id);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletes a Notification.
     * @param id id of notification to be deleted
     * @return {@code 200 OK} if successfully deleted, else {@code 404 Not Found}
     */
    @DeleteMapping("/notification/{id}/delete")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        boolean success = notificationService.deleteNotificationById(id);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Returns all DueDeckInfo for one Note.
     * @param noteId Id of Notification
     * @return List of DueDeckInfo
     */
    @GetMapping("/notification/{noteId}/dueCards")
    public List<DueDeckInfo> getDueDeckInfos(@PathVariable Long noteId) {
        return notificationService.getDueDeckInfos(notificationService.getNotificationById(noteId));
    }
}
