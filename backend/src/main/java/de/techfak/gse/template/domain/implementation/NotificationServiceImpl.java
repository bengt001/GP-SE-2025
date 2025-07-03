package de.techfak.gse.template.domain.implementation;

import de.techfak.gse.template.domain.entities.Notification;
import de.techfak.gse.template.domain.entities.Usr;
import de.techfak.gse.template.domain.repositories.UserRepository;
import de.techfak.gse.template.domain.service.NotificationService;
import de.techfak.gse.template.domain.repositories.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service that manages the notification of users.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    /**
     * Service that manages the notifications of user.
     */
    private final NotificationRepository notificationRepository;
    /**
     * Service that manages all user interactions.
     */
    private final UserRepository userRepository;

    @Autowired
    public NotificationServiceImpl(final NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Notification getNotificationById(@PathVariable Long id) {
        return notificationRepository.findById(id).orElse(null);
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

    /**
     * Sends daily message to user.
     * TODO: Change to midnight oder 6 am after SRA implemantation
     */
    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void sendNotificationToUsers() {
        List<Usr> users = (List<Usr>) userRepository.findAll();
        for (Usr user : users) {
            sendNotification(user);
        }
    }

    private void sendNotification(Usr user) {

        List<Notification> notifications = getNotificationByUser(user);

        if (notifications.size() < 5) {
            notificationRepository.save(new Notification(user, "ImplTest"));
        }
    }

    @Transactional
    @Override
    public boolean deleteNotificationById(Long id) {
        Optional<Notification> note = notificationRepository.findById(id);
        if (note.isPresent()) {
            Notification notification = note.get();
            notificationRepository.delete(notification);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteNotificationByUser(Usr user) {
        return false;
    }
    @Transactional
    @Override
    public boolean markNotificationAsRead(Long id) {
        Optional<Notification> note = notificationRepository.findById(id);
        if (note.isPresent()) {
            Notification notification = note.get();
            notification.setRead(true);
            notificationRepository.save(notification);
            return true;
        }
       return false;
    }

    public void sendWelcomeNote(Usr user) {
        notificationRepository.save(new Notification(user, "WELCOME"));
    }
}
