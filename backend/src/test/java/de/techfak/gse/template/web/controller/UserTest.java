package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.entities.Usr;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    @Test
    void createUser() {
        Usr usr = new Usr("test", "test@mail.com", "{bcrypt}$2a$10$WoG5Z4YN9Z37EWyNCkltyeFr6PtrSXSLMeFWOeDUwcanht5CIJgPa", "test","1");

        assertThat(usr.getUsername()).isEqualTo("test@mail.com");
        assertThat(usr.getEmail()).isEqualTo("test@mail.com");
        assertThat(usr.getPassword()).isEqualTo("{bcrypt}$2a$10$WoG5Z4YN9Z37EWyNCkltyeFr6PtrSXSLMeFWOeDUwcanht5CIJgPa");
        assertThat(usr.getDisplayName()).isEqualTo("test");
    }
}
