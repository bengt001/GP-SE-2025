package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.entities.Notification;
import de.techfak.gse.template.domain.entities.Usr;
import de.techfak.gse.template.domain.service.NotificationService;
import de.techfak.gse.template.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public List<Notification> getNotifications() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr user = userService.loadUserByUsername(auth.getName());
        return notificationService.getNotificationByUser(userService.loadUserById(user.getUserId()));
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
}
