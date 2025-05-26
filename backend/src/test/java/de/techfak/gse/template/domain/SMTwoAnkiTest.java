package de.techfak.gse.template.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SMTwoAnkiTest {

    SMTwoAnki smTwo = new SMTwoAnki();
    SraValues initValues = new SraValues(0, 0, 250);
    SraValues someValues = new SraValues(5, 6, 230);
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Test case for the Again rating with the init values.
     */
    @Test
    void updateValuesAgainInitValues() {
        SraValues updatedValues = smTwo.updateValues(initValues, 0);
        assertThat(updatedValues).isEqualTo(new SraValues(1, 0, 230));
    }

    /**
     * Test case for the Again rating with random values.
     */
    @Test
    void updateValuesAgainSomeValues() {
        SraValues updatedValues = smTwo.updateValues(someValues, 0);
        assertThat(updatedValues).isEqualTo(new SraValues(6, 0, 210));
    }

    /**
     * Test case for the Hard rating with the init values.
     */
    @Test
    void updateValuesHardInitValues() {
        SraValues updatedValues = smTwo.updateValues(initValues, 1);
        assertThat(updatedValues).isEqualTo(new SraValues(1, 0, 235));
    }

    /**
     * Test case for the Hard rating with random values.
     */
    @Test
    void updateValuesHardSomeValues() {
        SraValues updatedValues = smTwo.updateValues(someValues, 1);
        assertThat(updatedValues).isEqualTo(new SraValues(6, 7, 215));
    }

    /**
     * Test case for the Good rating with init values.
     */
    @Test
    void updateValuesGoodInitValues() {
        SraValues updatedValues = smTwo.updateValues(initValues, 2);
        assertThat(updatedValues).isEqualTo(new SraValues(1, 1, 250));
    }

    /**
     * Test case for the Good rating with random values.
     */
    @Test
    void updateValuesGoodSomeValues() {
        SraValues updatedValues = smTwo.updateValues(someValues, 2);
        assertThat(updatedValues).isEqualTo(new SraValues(6, 13, 230));
    }

    /**
     * Test case for the Easy rating with init values.
     */
    @Test
    void updateValuesEasyInitValues() {
        SraValues updatedValues = smTwo.updateValues(initValues, 3);
        assertThat(updatedValues).isEqualTo(new SraValues(1, 2, 265));
    }

    /**
     * Test case for the Easy rating with random values.
     */
    @Test
    void updateValuesEasySomeValues() {
        SraValues updatedValues = smTwo.updateValues(someValues, 3);
        assertThat(updatedValues).isEqualTo(new SraValues(6, 15, 245));
    }

    @Test
    void updateValuesRangeIllegalArgument() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> smTwo.updateValues(initValues, -1));
        assertThat(exception).hasMessageContaining("Rating must be between");
    }
}