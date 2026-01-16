package gr.unipi.meallabapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/*Η κλάση App είναι το σημείο εκκίνησης της εφαρμογής μας (JavaFX Application).
Εδώ φορτώνουμε το αρχικό FXML και ρυθμίζουμε το βασικό Stage (παράθυρο).
 */
public class App extends Application {

    /*
     * Κρατάμε το primaryStage ως static,
     * για να είναι προσβάσιμο και από άλλες κλάσεις π.χ. WelcomeController)
     */
    public static Stage primaryStage;
    private static Scene scene;

    /*
     * Η μέθοδος start καλείται αυτόματα όταν ξεκινάει η εφαρμογή.
     * Εδώ ρυθμίζουμε την αρχική εικόνα.
     */
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        // Φορτώνουμε αρχικά το welcome_view.fxml που είναι η οθόνη καλωσορίσματος
        scene = new Scene(loadFXML("welcome_view"), 1200, 700);

        // Προσθήκη του CSS αρχείου για το στυλ της εφαρμογής
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // Ρυθμίσεις του παραθύρου (τίτλος και εμφάνιση)
        stage.setTitle("Meal Lab App");
        stage.setScene(scene);
        stage.show();
    }

    /*
     * Βοηθητική μέθοδος για την αλλαγή του FXML αρχείου (Scene Root),
     * χωρίς να κλείσουμε το παράθυρο.
     * Χρησιμοποιείται για τη μετάβαση από το Welcome Screen στο Main Screen.
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /* Η main μέθοδος είναι το entry point για την Java εφαρμογή */
    public static void main(String[] args) {
        launch();
    }
}
