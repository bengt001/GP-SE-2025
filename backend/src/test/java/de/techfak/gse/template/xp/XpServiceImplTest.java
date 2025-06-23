package de.techfak.gse.template.xp;

import de.techfak.gse.template.domain.UserService;
import de.techfak.gse.template.domain.Usr;
import de.techfak.gse.template.domain.XpService;
import de.techfak.gse.template.domain.XpServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class XpServiceImplTest {

    private XpService xpService;


    @BeforeEach
    void setUp() {
        // Dummy-UserService, da für calculateXp nicht benötigt
        UserService dummyUserService = new UserService() {
            @Override public Usr loadUserByUsername(String email) { return null; }
            @Override public Usr createUser(String username, String email, String password, String displayName, String... roles) { return null; }
            @Override public boolean existsEmail(String email) { return false; }
            @Override public String getFreeID() { return ""; }
            @Override public void saveUser(Usr user) {}
            @Override public Usr loadUserByID(String id) { return null; }
        };

        xpService = new XpServiceImpl(dummyUserService);
    }

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
