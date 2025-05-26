package de.techfak.gse.template.domain;

import lombok.Getter;

import java.util.Objects;

/**
 * Stores the values needed for an sra.
 */
@Getter
public class SraValues {
    private final int repetitions;
    private final int interval;
    private final float easinessFactor;

    /**
     * Creating a new SraValues object.
     * @param repetitions repetitions done.
     * @param interval size of the interval.
     * @param easinessFactor EasinessFactor.
     */
    public SraValues(int repetitions, int interval, float easinessFactor) {
        this.repetitions = repetitions;
        this.interval = interval;
        this.easinessFactor = easinessFactor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SraValues sraValues = (SraValues) obj;
        return repetitions == sraValues.repetitions
                && interval == sraValues.interval
                && Float.compare(easinessFactor, sraValues.easinessFactor) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(repetitions, interval, easinessFactor);
    }
}
