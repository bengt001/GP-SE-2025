package de.techfak.gse.template.domain.service;

import de.techfak.gse.template.domain.entities.DueDeckInfo;
import de.techfak.gse.template.domain.entities.AbstractNotification;
import de.techfak.gse.template.domain.entities.Usr;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Interface for Service that manages the notifications of users.
 */
public interface NotificationService {
    AbstractNotification getNotificationById(@PathVariable Long id);

    List<AbstractNotification> getNotificationByUser(Usr user);

    boolean deleteNotificationById(@PathVariable Long id);

    boolean deleteNotificationByUser(Usr user);

    boolean markNotificationAsRead(@PathVariable Long id);

    void sendWelcomeNote(Usr user);

    List<DueDeckInfo> getDueDeckInfos(AbstractNotification notification);

    void sendFriendRequestNote(Usr recipient, Usr requester);
}
