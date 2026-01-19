package gr.unipi.meallabapp;

import javafx.fxml.FXML;

import java.io.IOException;

/*Controller για την οθόνη καλωσορίσματος (Welcome Screen).
Χειρίζεται τις ενέργειες χρήστη πριν μπούμε στην κυρίως εφαρμογή.*/
public class WelcomeController {

    /*
     * Καλείται όταν ο χρήστης πατήσει το κουμπί "Start Cooking".
     * Αλλάζει στην κυρίως οθόνη της εφαρμογής.
     */
    @FXML
    private javafx.scene.control.TextField usernameField;

    /*
     * Καλείται όταν ο χρήστης πατήσει το κουμπί "Start Cooking".
     * Ελέγχει το όνομα και αλλάζει στην κυρίως οθόνη.
     */
    @FXML
    private void handleStart() {
        String username = usernameField.getText();

        if (username == null || username.isBlank()) {

            /* Alert Παράθυρο για υποχρεώτικότητα ονόματος. */
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Login Required");
            alert.setHeaderText(null);
            alert.setContentText("Please enter your name to continue!");
            alert.showAndWait();
            return;
        }

        /*
         * Έλεγχος για απαγορευμένους χαρακτήρες.
         * Επιτρέπουμε μόνο γράμματα (Ελληνικά/Αγγλικά), αριθμούς και κενά.
         * Έτσι αποφεύγουμε προβλήματα με το σύστημα αρχείων και διενέξεις σε αυτά (π.χ.
         * *makis
         * vs ?makis), θα είχαμε πρόσβαση απο 2 users στο ίδιο αρχείο.
         */
        if (!username.matches("^[\\p{L}0-9 ]+$")) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Invalid Username");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Username can only contain letters, numbers, and spaces.\nSpecial characters are not allowed.");
            alert.showAndWait();
            return;
        }

        // Ορισμός του χρήστη στο Service
        UserDataService.getInstance().setUser(username);

        try {
            // Χρησιμοποιούμε τη μέθοδο setRoot της κλάσης App για να αλλάξουμε το FXML
            App.setRoot("main_view");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
