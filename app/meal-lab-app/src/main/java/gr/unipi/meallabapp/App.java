package gr.unipi.meallabapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/*Η κλάση App είναι το σημείο εκκίνησης της εφαρμογής.
Εδώ φορτώνουμε το αρχικό FXML και ρυθμίζουμε το βασικό Stage (παράθυρο).
 */
public class App extends Application {

    // Κρατάμε το Stage static για να μπορούμε να το αλλάζουμε από παντού
    public static Stage primaryStage;
    private static Scene scene;

    // Η μέθοδος start τρέχει μόλις ανοίξει η εφαρμογή.
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        // Φορτώνουμε την οθόνη welcome_view
        scene = new Scene(loadFXML("welcome_view"), 1200, 700);

        // Συνδέουμε το CSS για την εμφάνιση
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // Ρυθμίσεις του παραθύρου (τίτλος και εμφάνιση)
        stage.setTitle("Meal Lab App");
        stage.setScene(scene);
        stage.show();

    }

    // Αλλάζει την οθόνη (Scene) χωρίς να κλείσει το παράθυρο.
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /*
     * Βοηθητική μέθοδος που φορτώνει ένα FXML αρχείο και το μετατρέπει σε JavaFX
     * nodes.
     * Το FXMLLoader διαβάζει το XML και δημιουργεί αυτόματα το UI hierarchy.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // Η main
    public static void main(String[] args) {
        launch();
    }
}
