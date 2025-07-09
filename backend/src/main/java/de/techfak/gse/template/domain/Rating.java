package de.techfak.gse.template.domain;

/**
 * Class Rating is an enum for the rating.
 */
public enum Rating {
    AGAIN,
    HARD,
    GOOD,
    EASY,
    NOT_LEARNED;

    /**
     * Returns the enum value of a string with the name of the enum.
     * @param rating String of the rating. It has to be AGAIN, HARD, GOOD, EASY, NOT_LEARNED.
     * @return Returns an enum or null if the string does not match an enum.
     */
    public static Rating getRating(String rating) {
        return switch (rating) {
            case "AGAIN" -> AGAIN;
            case "HARD" -> HARD;
            case "GOOD" -> GOOD;
            case "EASY" -> EASY;
            case "NOT_LEARNED" -> NOT_LEARNED;
            default -> null;
        };
    }

    /**
     * Compares the enums. The better the rating the higher the value.
     * NOT_LEARNED has the highest rating.
     * @param other second Rating to compare to.
     * @return boolean comparison.
     */
    public boolean isGreaterOrEqual(Rating other) {
        return this.ordinal() >= other.ordinal();
    }
}
