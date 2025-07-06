package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.dto.AbstractGeneralNotes;
import de.techfak.gse.template.domain.entities.*;
import de.techfak.gse.template.domain.service.NotificationService;
import de.techfak.gse.template.domain.service.UserService;
import io.jsonwebtoken.lang.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class NotificationControllerTest {
    final Usr TESTUSR = new Usr("testuser", "test@mytest.com", "{bcrypt}$2a$10$WoG5Z4YN9Z37EWyNCkltyeFr6PtrSXSLMeFWOeDUwcanht5CIJgPa", "TEST", "1");
    final Deck DECK = new Deck(false, new ArrayList<>(Collections.singleton("Strafrecht AT")));
    final DeckInfo DECKINFO = new DeckInfo(TESTUSR, DECK, true);

    @Mock
    private NotificationService notificationService;
    @Mock
    private UserService userService;

    private NotificationController notificationController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        notificationController = new NotificationController(notificationService, userService);

        assertThat(userService).isNotNull();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);


        when(userService.existsEmail("test@mail.com")).thenReturn(true);


        when(authentication.getName()).thenReturn("testuser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void getNotifications() {
        when(userService.loadUserByUsername("testuser")).thenReturn(TESTUSR);
        when(userService.loadUserById(TESTUSR.getUserId())).thenReturn(TESTUSR);

        List<Notification> notifications = List.of(
                new Notification(TESTUSR, "WELCOME"),
                new Notification(TESTUSR, "DUECARDS")
        );

        List<DueDeckInfo> dueDeckInfos = List.of(
                new DueDeckInfo(notifications.get(1), DECKINFO, 7)
        );
        when(notificationService.getNotificationByUser(TESTUSR)).thenReturn(notifications);
        when(notificationService.getDueDeckInfos(notifications.get(1))).thenReturn(dueDeckInfos);

        List<AbstractGeneralNotes> notes = notificationController.getNotifications();

        assertThat(notes).isNotNull();
        assertThat(notes).hasSize(2);
        assertThat(notes).extracting("type").containsExactly("WELCOME", "DUECARDS");
    }

    @Test
    void successful_mark_as_read() {
        Notification notification = new Notification(TESTUSR, "WELCOME");
        notification.setId(1L);

        when(notificationService.markNotificationAsRead(notification.getId())).thenReturn(true);

        ResponseEntity<Void> responseEntity = notificationController.markAsRead(1L);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity).isEqualTo(ResponseEntity.ok().build());
    }

    @Test
    void unsuccessful_mark_as_read() {
        Notification notification = new Notification(TESTUSR, "WELCOME");
        notification.setId(1L);

        when(notificationService.markNotificationAsRead(notification.getId())).thenReturn(false);

        ResponseEntity<Void> responseEntity = notificationController.markAsRead(1L);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity).isEqualTo(ResponseEntity.notFound().build());
    }

    @Test
    void successful_delete_notification() {
        Notification notification = new Notification(TESTUSR, "WELCOME");
        notification.setId(1L);

        when(notificationService.deleteNotificationById(notification.getId())).thenReturn(true);

        ResponseEntity<Void> responseEntity = notificationController.deleteNotification(1L);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity).isEqualTo(ResponseEntity.ok().build());
    }

    @Test
    void unsuccessful_delete_notification() {
        Notification notification = new Notification(TESTUSR, "WELCOME");
        notification.setId(1L);

        when(notificationService.deleteNotificationById(notification.getId())).thenReturn(false);

        ResponseEntity<Void> responseEntity = notificationController.deleteNotification(1L);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity).isEqualTo(ResponseEntity.notFound().build());
    }
}

