package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.Notification;
import de.techfak.gse.template.domain.NotificationService;
import de.techfak.gse.template.domain.UserService;
import de.techfak.gse.template.domain.Usr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private NotificationService notificationService;
    private UserService userService;

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
        Usr usr = userService.loadUserByUsername(auth.getName());
        return notificationService.getNotificationByUser(userService.loadUserById(usr.getUserId()));
    }
}
