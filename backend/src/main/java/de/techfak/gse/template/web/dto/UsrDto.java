package de.techfak.gse.template.web.dto;

import de.techfak.gse.template.domain.entities.Usr;

/**
 * Data Transfer Object (DTO) für Benutzerdaten.
 * Enthält grundlegende Informationen über einen Nutzer, z. B. Benutzername,
 * E-Mail, Anzeigename und gesammelte XP.
 */
public class UsrDto {
    private String userId;
    private String username;
    private String email;
    private String displayName;
    private Integer totalXp;
    private Integer streakCount;
    private String profilePictureUrl;

    /**
     * Erstellt ein {@code UsrDto}-Objekt aus einer {@link Usr}-Entität.
     * @param user die Usr-Entität, aus der die Daten übernommen werden
     */
    public UsrDto(Usr user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.displayName = user.getDisplayName();
        this.totalXp = user.getTotalXp();
        this.streakCount = user.getStreakCount();
        this.profilePictureUrl = user.getProfilePictureData() != null
                ? "/api/profile/profile-picture/" + user.getEmail()
                : null;
    }

    // Getter und Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getTotalXp() {
        return totalXp;
    }

    public void setTotalXp(int totalXp) {
        this.totalXp = totalXp;
    }

    public Integer getStreakCount() {
        return streakCount;
    }

    public void setStreakCount(int streakCount) {
        this.streakCount = streakCount;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
