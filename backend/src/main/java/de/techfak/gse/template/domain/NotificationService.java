package de.techfak.gse.template.domain;

import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Notification getNotificationById(@PathVariable Long id);

    List<Notification> getNotificationByUser(Usr user);

    boolean deleteNotificationById(@PathVariable Long id);

    boolean deleteNotificationByUser(Usr user);

    boolean markNotificationAsRead(@PathVariable Long id);
}
