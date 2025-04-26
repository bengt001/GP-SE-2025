package de.techfak.gse.template.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Usr implements UserDetails {

    private static final long serialVersionUID = 0L;

    private String username;

    @Id
    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    @SuppressWarnings("serial")
    private List<String> roles = new ArrayList<>();

    /** JPA constructor */
    protected Usr() {

    }

    public Usr(final String username, final String email, final String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(this.roles.toArray(new String[0]));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
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
