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
        try {
            service.setFileNameForTesting(TEST_FILE);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    void testAddFavorite() throws java.io.IOException {
        Recipe recipe = new Recipe();
        recipe.setId("99999");
        recipe.setName("Test Recipe");

        service.addFavorite(recipe);

        assertTrue(service.getFavorites().stream().anyMatch(r -> r.getId().equals("99999")));
    }

    @Test
    @Order(2)
    void testRemoveFavorite() throws java.io.IOException {
        Recipe recipe = new Recipe();
        recipe.setId("99999");

        service.removeFavorite(recipe);

        assertFalse(service.getFavorites().stream().anyMatch(r -> r.getId().equals("99999")));
    }

    @Test
    @Order(3)
    void testAddCooked() throws java.io.IOException {
        Recipe recipe = new Recipe();
        recipe.setId("88888");
        recipe.setName("Cooked Recipe");

        service.addCooked(recipe);

        assertTrue(service.getCooked().stream().anyMatch(r -> r.getId().equals("88888")));
    }

    @Test
    @Order(4)
    void testMultiUserIsolation() throws java.io.IOException {
        // Ensure clean state from previous runs
        new File("user_data_usera.json").delete();
        new File("user_data_userb.json").delete();

        // Scenario: User A adds a favorite
        service.setUser("UserA");
        Recipe r1 = new Recipe();
        r1.setId("1");
        r1.setName("UserA Recipe");
        service.addFavorite(r1);
        assertTrue(service.getFavorites().stream().anyMatch(r -> r.getId().equals("1")));

        // Switch to User B
        service.setUser("UserB");
        // Should NOT have UserA's favorite
        assertFalse(service.getFavorites().stream().anyMatch(r -> r.getId().equals("1")),
                "UserB should not see UserA's favorites");

        // Add something for User B
        Recipe r2 = new Recipe();
        r2.setId("2");
        r2.setName("UserB Recipe");
        service.addFavorite(r2);

        // Switch back to User A
        service.setUser("UserA");
        // Should have UA1 but not UB1 (which is now ID 2)
        assertTrue(service.getFavorites().stream().anyMatch(r -> r.getId().equals("1")));
        assertFalse(service.getFavorites().stream().anyMatch(r -> r.getId().equals("2")));

        // Cleanup extra files created by this test
        new File("user_data_usera.json").delete();
        new File("user_data_userb.json").delete();
    }

    @Test
    @Order(6)
    void testInvalidCharacters() throws java.io.IOException {
        // Δοκιμή με απαγορευμένους χαρακτήρες (Windows reserved chars)
        // π.χ. < > : " / \ | ? *
        String trickyName = "My<User/Name:is*Makis?";
        service.setUser(trickyName);

        String fileName = service.getCurrentUserFile();
        // Το expected είναι: my<user/name:is*makis? -> my_user_name_is_makis_
        // Όλα τα σύμβολα γίνονται "_"
        assertFalse(fileName.contains("<"), "Should not contain less than");
        assertFalse(fileName.contains(":"), "Should not contain colon");
        assertFalse(fileName.contains("*"), "Should not contain star");
        assertFalse(fileName.contains("?"), "Should not contain question mark");
        assertTrue(fileName.startsWith("user_data_my_user_name_is_makis_"), "Should be sanitized");

        new File(fileName).delete();
    }

    @Test
    @Order(5)
    void testGreekUser() throws java.io.IOException {
        // Δοκιμή με Ελληνικό όνομα
        service.setUser("Μάκης");

        // Επιβεβαίωση ότι το αρχείο έχει σωστό όνομα (μάκης - lowercase)
        // ΣΗΜΕΙΩΣΗ: Η υλοποίηση αντικαθιστά όλα τα μη-λατινικά με "_"
        String fileName = service.getCurrentUserFile();
        // Μάκης -> _____
        assertTrue(fileName.contains("_____"), "Filename should contain underscores for non-latin characters");

        Recipe r = new Recipe();
        r.setId("1");
        r.setName("Recipe");
        service.addFavorite(r);

        assertTrue(service.getFavorites().stream().anyMatch(f -> f.getId().equals("1")));

        // Cleanup
        new File(fileName).delete();
    }
}
