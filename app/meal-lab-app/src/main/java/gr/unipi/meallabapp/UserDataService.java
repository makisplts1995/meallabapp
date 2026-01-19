package gr.unipi.meallabapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.unipi.meallab.model.Recipe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*Υπηρεσία διαχείρισης δεδομένων χρήστη (UserDataService). 
Αποθηκεύει και διαβάζει τα δεδομένα (Αγαπημένα, Ιστορικό) σε αρχείο JSON.
Χρησιμοποιεί το μοτίβο Singleton ώστε να έχουμε μόνο ένα αντικείμενο
διαχείρισης σε όλη την εφαρμογή.
 */
public class UserDataService {

    // Βασικό όνομα αρχείου
    private static final String DEFAULT_FILE_NAME = "user_data.json";
    private String currentFileName = DEFAULT_FILE_NAME;
    private String currentUsername = "Guest"; // Default username

    // Η μοναδική instance (Singleton)
    private static UserDataService instance;
    // Jackson Mapper για μετατροπή Java αντικειμένων σε JSON και αντίστροφα
    private final ObjectMapper mapper = new ObjectMapper();
    // Τα δεδομένα που διαχειριζόμαστε (Favorites & Cooked History)
    private UserData data;

    // Private constructor για να μην μπορεί να δημιουργηθεί νέο αντικείμενο απ' έξω
    private UserDataService() {
        loadData();
    }

    // Μέθοδος για να πάρουμε το μοναδικό instance της υπηρεσίας
    public static synchronized UserDataService getInstance() {
        if (instance == null) {
            instance = new UserDataService();
        }
        return instance;
    }

    // Setter για να αλλάζουμε το αρχείο στα JUnit tests
    public void setFileNameForTesting(String fileName) {
        this.currentFileName = fileName;
        loadData(); // Ξαναφορτώνουμε τα δεδομένα από το νέο αρχείο
    }

    /*
     * Ορισμός χρήστη και αλλαγή αρχείου αποθήκευσης,
     * Αυτή η μέθοδος δέχεται το όνομα του χρήστη (username)
     * και ενημερώνει το όνομα του αρχείου (currentFileName).
     * Έτσι, κάθε χρήστης έχει τη δική του ανεξάρτητη λίστα αγαπημένων.
     */
    public void setUser(String username) {
        if (username != null && !username.trim().isEmpty()) {
            this.currentUsername = username.trim();
            /*
             * Καθαρισμός ονόματος και μετατροπή σε πεζά (case insensitive)
             * για να μην έχουμε προβλήματα με την αποθήκευση και το handle των users.
             * Π.χ. το "Makis" ή το "MAKIS" γίνεται "makis" και το αρχείο
             * "user_data_makis.json"
             */
            String safeName = username.trim().toLowerCase().replaceAll("[^a-z0-9]", "_");
            this.currentFileName = "user_data_" + safeName + ".json";
        } else {
            this.currentUsername = "Guest";
            this.currentFileName = DEFAULT_FILE_NAME;
        }
        loadData(); // Φόρτωση δεδομένων του συγκεκριμένου χρήστη
    }

    public String getCurrentUserFile() {
        return currentFileName;
    }

    public String getUsername() {
        return currentUsername;
    }

    // Φόρτωση δεδομένων από το αρχείο JSON κατά την εκκίνηση
    private void loadData() {
        File file = new File(currentFileName);
        if (file.exists()) {
            try {
                data = mapper.readValue(file, UserData.class);
            } catch (IOException e) {
                e.printStackTrace();
                // Αν αποτύχει η ανάγνωση, ξεκινάμε με κενά δεδομένα
                data = new UserData();
            }
        } else {
            // Αν δεν υπάρχει το αρχείο, δημιουργούμε νέο αντικείμενο
            data = new UserData();
        }
    }

    // Αποθήκευση των τρεχόντων δεδομένων στο αρχείο
    private void saveData() {
        try {
            mapper.writeValue(new File(currentFileName), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= Μέθοδοι Getter / Setter / Add / Remove ================= //

    public List<Recipe> getFavorites() {
        return data.getFavorites();
    }

    public void addFavorite(Recipe recipe) {
        // Ελέγχουμε αν υπάρχει ήδη για να μην έχουμε διπλές εγγραφές
        if (data.getFavorites().stream().noneMatch(r -> r.getId().equals(recipe.getId()))) {
            data.getFavorites().add(recipe);
            saveData(); // Αποθήκευση αμέσως μετά την αλλαγή
        }
    }

    public void removeFavorite(Recipe recipe) {
        data.getFavorites().removeIf(r -> r.getId().equals(recipe.getId()));
        saveData();
    }

    public List<Recipe> getCooked() {
        return data.getCooked();
    }

    public void addCooked(Recipe recipe) {
        if (data.getCooked().stream().noneMatch(r -> r.getId().equals(recipe.getId()))) {
            data.getCooked().add(recipe);
            saveData();
        }
    }

    public void removeCooked(Recipe recipe) {
        data.getCooked().removeIf(r -> r.getId().equals(recipe.getId()));
        saveData();
    }

    /*
     * Εσωτερική κλάση (Inner Class) που αναπαριστά τη δομή του JSON αρχείου.
     * Περιέχει λίστες για τα favorites και τα cooked .
     */

    public static class UserData {
        private List<Recipe> favorites = new ArrayList<>();
        private List<Recipe> cooked = new ArrayList<>();

        public List<Recipe> getFavorites() {
            return favorites;
        }

        public void setFavorites(List<Recipe> favorites) {
            this.favorites = favorites;
        }

        public List<Recipe> getCooked() {
            return cooked;
        }

        public void setCooked(List<Recipe> cooked) {
            this.cooked = cooked;
        }
    }
}
