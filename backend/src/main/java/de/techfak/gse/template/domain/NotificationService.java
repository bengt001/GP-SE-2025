package de.techfak.gse.template.domain;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    List<Notification> getNotificationByUser(Usr user);
}
