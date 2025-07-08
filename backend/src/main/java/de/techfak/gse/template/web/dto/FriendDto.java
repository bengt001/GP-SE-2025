package de.techfak.gse.template.web.dto;

public class FriendDto {
    private String email;
    private int streakCount;
    private int totalXp;

    public FriendDto() {}

    public FriendDto(String email, int streakCount, int totalXp) {
        this.email = email;
        this.streakCount = streakCount;
        this.totalXp = totalXp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStreakCount() {
        return streakCount;
    }

    public void setStreakCount(int streakCount) {
        this.streakCount = streakCount;
    }

    public int getTotalXp() {
        return totalXp;
    }

    public void setTotalXp(int totalXp) {
        this.totalXp = totalXp;
    }
}
