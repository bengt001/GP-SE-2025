package de.techfak.gse.template.web.controller;


import de.techfak.gse.template.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class UsrControllerTest {

    final Usr TESTUSR = new Usr("testuser", "test@mytest.com", "{bcrypt}$2a$10$WoG5Z4YN9Z37EWyNCkltyeFr6PtrSXSLMeFWOeDUwcanht5CIJgPa","TEST", "1");

    private AutoCloseable closeable;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        assertThat(userService).isNotNull();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);


        when(userService.existsEmail("test@mail.com")).thenReturn(true);


        when(authentication.getName()).thenReturn("testuser");
        when(securityContext.getAuthentication()).thenReturn(authentication);

    }

    @Test
    void exists_success() {
        UsrController usrController = new UsrController(userService);
        Boolean exists = usrController.exists("test@mail.com");

        assertThat(exists).isEqualTo(true);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }
}
