package de.techfak.gse.template.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(final NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> getNotificationByUser(Usr user) {
        final List<Notification> notifications = new ArrayList<>();
        List<Notification> temp = new ArrayList<>();
        notificationRepository.findAll().forEach(temp::add);

        for (Notification notification : temp) {
            if (notification.getUser().equals(user)) {
                notifications.add(notification);
            }
        }

        return notifications;
    }
}
