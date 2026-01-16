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
    private void handleStart() {
        try {
            // Χρησιμοποιούμε τη μέθοδο setRoot της κλάσης App για να αλλάξουμε το FXML
            App.setRoot("main_view");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
