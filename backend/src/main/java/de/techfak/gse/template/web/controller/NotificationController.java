package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.Notification;
import de.techfak.gse.template.domain.NotificationService;
import de.techfak.gse.template.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private NotificationService notificationService;
    private UserService userService;

    @Autowired
    public NotificationController(NotificationService notificationService,  UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/notification")
    public List<Notification> showBlog() {
        return notificationService.getNotifications();
    }

    @GetMapping("/notification/{user:\\d+}")
    public List<Notification> showNotifications(@PathVariable("user") final String userid) {
        return notificationService.getNotificationByUser(userService.loadUserById(userid));
    }
}
