package gr.unipi.meallabapp;

import gr.unipi.meallab.model.Recipe;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDataServiceTest {

    private final UserDataService service = UserDataService.getInstance();
    private static final String TEST_FILE = "test_user_data.json";

    @BeforeAll
    static void setup() {
        /*
         * Διαγραφή του αρχείου πριν ξεκινήσουν τα tests,
         * γιατί υπήρχε False Positive στα αποτελέσματα των tests
         */
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @AfterAll
    static void cleanup() {
        // Διαγραφή του αρχείου μετά το τέλος των test για επανάληψη test την επόμενη
        // φορά
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    public UserDataServiceTest() {
        // Χρήση ξεχωριστού αρχείου για τα tests
        service.setFileNameForTesting(TEST_FILE);
    }

    @Test
    @Order(1)
    void testAddFavorite() {
        Recipe recipe = new Recipe();
        recipe.setId("99999");
        recipe.setName("Test Recipe");

        service.addFavorite(recipe);

        assertTrue(service.getFavorites().stream().anyMatch(r -> r.getId().equals("99999")));
    }

    @Test
    @Order(2)
    void testRemoveFavorite() {
        Recipe recipe = new Recipe();
        recipe.setId("99999");

        service.removeFavorite(recipe);

        assertFalse(service.getFavorites().stream().anyMatch(r -> r.getId().equals("99999")));
    }

    @Test
    @Order(3)
    void testAddCooked() {
        Recipe recipe = new Recipe();
        recipe.setId("88888");
        recipe.setName("Cooked Recipe");

        service.addCooked(recipe);

        assertTrue(service.getCooked().stream().anyMatch(r -> r.getId().equals("88888")));
    }
}
