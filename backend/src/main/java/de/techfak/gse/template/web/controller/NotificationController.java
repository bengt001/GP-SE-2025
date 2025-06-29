package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.Notification;
import de.techfak.gse.template.domain.NotificationService;
import de.techfak.gse.template.domain.UserService;
import de.techfak.gse.template.domain.Usr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    /**
     * API Endpoint to get all Notifications of a User.
     *
     * @return List of Notifications for one User.
     */
    @GetMapping("/notification")
    @Secured("ROLE_USER")
    public List<Notification> getNotifications() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usr user = userService.loadUserByUsername(auth.getName());
        return notificationService.getNotificationByUser(userService.loadUserById(user.getUserId()));
    }

    @GetMapping("/notification/status")
    public String getStatus() {
        return "Message send at " + LocalDateTime.now();
    }

    @PutMapping("/notification/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        boolean success = notificationService.markNotificationAsRead(id);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/notification/{id}/delete")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        boolean success = notificationService.deleteNotificationById(id);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
