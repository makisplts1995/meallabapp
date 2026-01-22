package gr.unipi.meallabapp;

import gr.unipi.meallab.model.Recipe;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/*
 * Προσαρμοσμένο κελί λίστας (Custom ListCell) για την εμφάνιση συνταγών.
 * Αντί για απλό κείμενο, δείχνουμε:
 * - Thumbnail εικόνα (50x50)
 * - Όνομα συνταγής (bold)
 * - Category και Area chips (colored badges)
 * 
 * Το custom rendering βελτιώνει την UX και κάνει τη λίστα πιο αναγνωρίσιμη.
 */
public class RecipeListCell extends ListCell<Recipe> {
    private final ImageView imageView = new ImageView();
    private final Label nameLabel = new Label();
    private final Label categoryLabel = new Label();
    private final Label areaLabel = new Label();

    // Containers για διάταξη
    private final HBox chipsBox = new HBox(5, categoryLabel, areaLabel);
    private final VBox textContainer = new VBox(5, nameLabel, chipsBox);
    private final HBox root = new HBox(10, imageView, textContainer);

    public RecipeListCell() {
        // Ρυθμίσεις εικόνας
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
     * Η updateItem τρέχει αυτόματα και ενημερώνει το κελί.
     */
    @Override
    protected void updateItem(Recipe item, boolean empty) {
        super.updateItem(item, empty);

        // Αν είναι άδειο, δεν δείχνουμε τίποτα
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Βάζουμε όνομα και εικόνα
            nameLabel.setText(item.getName());
            categoryLabel.setText(item.getCategory());
            areaLabel.setText(item.getArea());

            if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                try {
                    // Φόρτωση εικόνας (background)
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
