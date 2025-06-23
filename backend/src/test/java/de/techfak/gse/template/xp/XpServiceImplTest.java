package de.techfak.gse.template.xp;

import de.techfak.gse.template.domain.XpService;
import de.techfak.gse.template.domain.XpServiceImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class XpServiceImplTest {
    private final XpService xpService = new XpServiceImpl();

    @Test
    void testDefinitionWithSuperRating() {
        int xp = xpService.calculateXp("definition", 0, 4);
        assertEquals(10, xp);
    }

    @Test
    void testProblemWithBadRating() {
        int xp = xpService.calculateXp("problem", 0, 2);
        assertEquals(5, xp); // 10 * 0.5
    }

    @Test
    void testSchemaWithThreeItemsAndGoodRating() {
        int xp = xpService.calculateXp("schema", 3, 3);
        assertEquals(11, xp); // 15 * 0.75
    }

    @Test
    void testUnknownCardType() {
        int xp = xpService.calculateXp("unknown", 5, 4);
        assertEquals(0, xp);
    }

    @Test
    void testSchemaWithZeroRating() {
        int xp = xpService.calculateXp("schema", 4, 0);
        assertEquals(0, xp); // multiplier 0.0
    }
}
