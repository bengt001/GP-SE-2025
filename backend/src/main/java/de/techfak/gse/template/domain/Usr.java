package de.techfak.gse.template.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User Klasse implementiert UserDetails. Definiert die Attribute eines Nutzers und getter und setter.
 */
@Getter
@Setter
@Entity
public class Usr implements UserDetails {
    @Serial
    private static final long serialVersionUID = 0L;

    @Id
    @Column
    private String userId;

    @Column
    private String displayName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column
    private String email;

    @JsonIgnore
    private String password;

    @Column
    private LocalDate creationDate;

    @OneToMany(mappedBy = "userId")
    private transient List<CardRating> ratings;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usr_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @SuppressWarnings("serial")
    private List<String> roles = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    private List<Deck> decks = new ArrayList<>();

    /** JPA constructor. */
    protected Usr() {

    }

    /**
     * Konstruktor des Nutzers (Usr).
     * @param username Nutzername des Nutzers.
     * @param email Email des Nutzers.
     * @param password Passwort des Nutzers.
     * @param displayName Der Display Name des Nutzers
     * @param userId Die Id des Nutzers
     */
    public Usr(final String username,
               final String email,
               final String password,
               final String displayName,
               final String userId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.creationDate = LocalDate.now();
        this.displayName = displayName;
        this.userId = userId;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(this.roles.toArray(new String[0]));
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
