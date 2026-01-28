package gr.unipi.meallabapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDataVersioningTest {

    private final UserDataService service = UserDataService.getInstance();
    private static final String TEST_FILE = "test_version_migration.json";
    private final ObjectMapper mapper = new ObjectMapper();

    @AfterEach
    void cleanup() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testMigrationFromNoVersion() throws IOException {
        // Δημιουργία αρχείου JSON χωρίς το πεδίο version (παλιά δομή)
        // Αυτό προσομοιώνει δεδομένα πριν την εισαγωγή του versioning
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("{\"favorites\":[],\"cooked\":[]}");
        }

        service.setFileNameForTesting(TEST_FILE);

        /*
         * Αφού φορτώσουμε τα παλιά δεδομένα, η UserDataService θα πρέπει να
         * καταλάβει ότι λείπει το version και να τρέξει το migration,
         * φέρνοντας τα δεδομένα στην τρέχουσα έκδοση (1).
         */

        JsonNode root = mapper.readTree(new File(TEST_FILE));
        assertTrue(root.has("version"), "Το αρχείο πρέπει να έχει πλέον πεδίο 'version'");
        assertEquals(1, root.get("version").asInt(), "Η έκδοση (version) πρέπει να είναι 1");
    }

    @Test
    void testMigrationFromOldVersion() throws IOException {
        // Δημιουργία αρχείου JSON με version 0 (μια παλιά έκδοση απλά για δοκιμή)
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("{\"version\":0,\"favorites\":[],\"cooked\":[]}");
        }

        service.setFileNameForTesting(TEST_FILE);

        JsonNode root = mapper.readTree(new File(TEST_FILE));
        assertEquals(1, root.get("version").asInt(), "Η έκδοση (version) πρέπει να αναβαθμιστεί σε 1");
    }

}
