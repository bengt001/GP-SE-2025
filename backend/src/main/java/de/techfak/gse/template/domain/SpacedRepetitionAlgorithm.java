package de.techfak.gse.template.domain;

/**
 * Interface for spaced repetitions algorithms.
 */
public interface SpacedRepetitionAlgorithm {

    SraValues updateValues(SraValues sraValues, int rating);
}
