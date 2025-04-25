package de.techfak.gse.template;

import de.techfak.gse.template.domain.Usr;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    @Test
    void createUser() {
        Usr usr = new Usr("test", "test@mail.com", "password");

        assertThat(usr.getUsername()).isEqualTo("test");
        assertThat(usr.getEmail()).isEqualTo("test@mail.com");
        assertThat(usr.getPassword()).isEqualTo("password");
    }
}
