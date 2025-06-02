package de.techfak.gse.template.domain;

import jakarta.persistence.Embeddable;

/**
 * Stores values needed for an SRA.
 * @param repetitions Number of repetitions.
 * @param interval Size of the interval in days.
 * @param easinessFactor The Easiness factor.
 */
@Embeddable
public record SraValues(int repetitions, int interval, float easinessFactor) {
    public static final int INIT_EASINESS_FACTOR = 250;
    public SraValues() {
        this(0, 0, INIT_EASINESS_FACTOR);
    }
}
