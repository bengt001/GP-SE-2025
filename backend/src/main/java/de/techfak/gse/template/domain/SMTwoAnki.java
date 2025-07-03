package de.techfak.gse.template.domain;

/**
 * SuperMemory2 Anki algorithm.
 */
public class SMTwoAnki implements de.techfak.gse.template.domain.SpacedRepetitionAlgorithm {
    public static final int DIVIDER = 100;
    public static final int INITIAL_INTERVAL = 0;
    public static final int AGAIN_EF_PENALTY = 20;
    public static final float HARD_EF_PENALTY = 15;
    public static final int HARD_INTERVAL_INCREASE = 120;
    public static final int EASY_EF_REWARD = 15;
    public static final int EASY_INTERVAL_BONUS = 2;


    /**
     * Implementation of the SM2 Anki style algorithm.
     * This implementation uses a rating of 0 to 3 (four cases) named AGAIN, HARD, GOOD, EASY.
     * The number of repetitions will always be increased by one.
     * AGAIN case:
     * The EF (easinessFactor) decreases by 20 points (20%).
     * The interval resets to the initial interval 0, so the next interval is 0 days aka immediately.
     * HARD case:
     * The EF decreases by 15 points (15%).
     * The interval is multiplied by 1.2.
     * GOOD case:
     * The EF stays the same.
     * The interval is multiplied by the EF.
     * EASY case:
     * The EF increases by 15 points (15%).
     * The Interval is multiplied by teh EF and an bonus of 2 (two days) is added.
     * @param sraValues current sraValues
     * @param rating Rating given
     * @return SraValues calculated with the new rating.
     */
    @Override
    public SraValues updateValues(SraValues sraValues, Rating rating) {

        int newInterval;
        float newEasinessFactor;
        switch (rating) {
            case AGAIN:
                if (sraValues.easinessFactor() > AGAIN_EF_PENALTY) {
                    newEasinessFactor = sraValues.easinessFactor() - AGAIN_EF_PENALTY;
                } else {
                    newEasinessFactor = 0;
                }
                newInterval = INITIAL_INTERVAL;
                break;
            case HARD:
                if (sraValues.easinessFactor() > HARD_EF_PENALTY) {
                    newEasinessFactor = sraValues.easinessFactor() - HARD_EF_PENALTY;
                } else {
                    newEasinessFactor = 0;
                }
                newInterval = sraValues.interval() * HARD_INTERVAL_INCREASE / DIVIDER;
                break;
            case GOOD:
                newEasinessFactor = sraValues.easinessFactor();
                newInterval = (int) (sraValues.interval() * sraValues.easinessFactor() / DIVIDER);
                if (newInterval < 1) {
                    newInterval = 1;
                }
                break;
            case EASY:
                newEasinessFactor = sraValues.easinessFactor() + EASY_EF_REWARD;
                newInterval = (int) (sraValues.interval() * sraValues.easinessFactor() / DIVIDER)
                        + EASY_INTERVAL_BONUS;
                break;
            case NOT_LEARNED:
            default:
                newInterval = sraValues.interval();
                newEasinessFactor = sraValues.easinessFactor();
        }
        return new SraValues(sraValues.repetitions() + 1, newInterval, newEasinessFactor);
    }
}
