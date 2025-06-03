package de.techfak.gse.template.domain;

import jakarta.persistence.Embeddable;

/**
 * This class stores the values used by an SRA.
 * @param repetitions Number of repetitions, initially 0.
 * @param interval Size of the learning interval in days, initially 0.
 * @param easinessFactor Easiness Factor, initially 250.
 */
@Embeddable
public record SraValues(int repetitions, int interval, float easinessFactor) {
    public static final int EASINESS_FACTOR_INIT = 250;

    /**
     * Constructor for the SRA Values.
     * By Default the are initialized with 0, 0, 250.
     */
    public SraValues() {
        this(0, 0, EASINESS_FACTOR_INIT);
    }
}
