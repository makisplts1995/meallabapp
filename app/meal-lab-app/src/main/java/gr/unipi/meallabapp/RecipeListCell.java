package gr.unipi.meallabapp;

import gr.unipi.meallab.model.Recipe;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/*Προσαρμοσμένο κελί λίστας (Custom ListCell) για την εμφάνιση συνταγών. 
Επιτρέπει να εμφανίζουμε εικόνα και όνομα δίπλα-δίπλα αντί για απλό κείμενο.*/
public class RecipeListCell extends ListCell<Recipe> {
    private final ImageView imageView = new ImageView();
    private final Label nameLabel = new Label();
    private final Label categoryLabel = new Label();
    private final Label areaLabel = new Label();

    // Containers
    private final HBox chipsBox = new HBox(5, categoryLabel, areaLabel); // Chips δίπλα-δίπλα
    private final VBox textContainer = new VBox(5, nameLabel, chipsBox); // Όνομα πάνω, chips κάτω
    private final HBox root = new HBox(10, imageView, textContainer); // Εικόνα αριστερά, κείμενα δεξιά

    public RecipeListCell() {
        // Ρυθμίσεις εμφάνισης για την εικόνα
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);

        // Ρυθμίσεις στυλ
        root.setStyle("-fx-padding: 5; -fx-alignment: center-left;");
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Styling για τα chips (χρήση κλάσεων από style.css)
        categoryLabel.getStyleClass().addAll("chip", "chip-category");
        areaLabel.getStyleClass().addAll("chip", "chip-area");

        // Μικρότερη γραμματοσειρά για τα chips στο list cell αν χρειάζεται (ή το
        // παίρνει από το css .chip)
        // Τα chips στο css έχουν οριστεί με font-size: 12px
    }

    /*
     * Η μέθοδος updateItem καλείται αυτόματα από το JavaFX για να ενημερώσει το
     * περιεχόμενο του κελιού.
     * 
     * @param item Η συνταγή που πρέπει να εμφανιστεί.
     * 
     * @param empty Αν το κελί είναι άδειο.
     */
    @Override
    protected void updateItem(Recipe item, boolean empty) {
        super.updateItem(item, empty);

        // Αν είναι άδειο, καθαρίζουμε τα περιεχόμενα
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Αν έχουμε συνταγή, βάζουμε το όνομα και την εικόνα
            nameLabel.setText(item.getName());
            categoryLabel.setText(item.getCategory());
            areaLabel.setText(item.getArea());

            if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                try {
                    // Φόρτωση εικόνας στο background για να μην κολλάει το UI
                    imageView.setImage(new Image(item.getImageUrl(), 50, 50, true, true, true));
                } catch (Exception e) {
                    imageView.setImage(null);
                }
            } else {
                imageView.setImage(null);
            }

            setText(null);
            setGraphic(root); // Ορίζουμε το HBox ως γραφικό του κελιού
        }
    }
}
