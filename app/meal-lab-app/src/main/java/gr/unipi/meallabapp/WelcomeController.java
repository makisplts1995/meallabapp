package gr.unipi.meallabapp;

import javafx.fxml.FXML;

import java.io.IOException;

/*Controller για την οθόνη καλωσορίσματος (Welcome Screen).
Χειρίζεται τις ενέργειες χρήστη πριν μπούμε στην κυρίως εφαρμογή.*/
public class WelcomeController {

    // Πεδίο για το όνομα χρήστη
    @FXML
    private javafx.scene.control.TextField usernameField;

    @FXML
    public void initialize() {
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                handleStart();
            }
        });
    }

    /*
     * Όταν πατηθεί το κουμπί "Start Cooking".
     * Ελέγχει το όνομα και μας βάζει στην εφαρμογή.
     */
    @FXML
    private void handleStart() {
        String username = usernameField.getText();

        if (username == null || username.isBlank()) {

            // Μήνυμα αν το όνομα είναι κενό
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Login Required");
            alert.setHeaderText(null);
            alert.setContentText("Please enter your name to continue!");
            alert.showAndWait();
            return;
        }

        /*
         * Ελέγχουμε αν έχει μόνο γράμματα και αριθμούς.
         * Έτσι αποφεύγουμε προβλήματα με τα αρχεία (π.χ. ειδικούς χαρακτήρες).
         */
        if (!username.matches("^[a-zA-Z0-9 ]+$")) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Invalid Username");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Username can only contain Latin letters, numbers, and spaces.\nSpecial characters are not allowed.");
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
