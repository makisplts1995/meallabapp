package gr.unipi.meallabapp;

import gr.unipi.meallab.model.Ingredient;
import gr.unipi.meallab.model.Recipe;
import javafx.print.PrinterJob;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.print.PageLayout;

/*
 * Controller για την οθόνη λεπτομερειών συνταγής (Recipe Details).
 * Αυτή η οθόνη ανοίγει σε νέο παράθυρο και δείχνει:
 * - Φωτογραφία της συνταγής
 * - Πλήρη λίστα υλικών
 * - Αναλυτικές οδηγίες μαγειρέματος
 * 
 * Περιλαμβάνει επίσης λειτουργία εκτύπωσης (Print).
 */
public class RecipeDetailsController {

    @FXML
    private ImageView mealImage;
    @FXML
    private Label mealName;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label areaLabel;
    @FXML
    private TextArea ingredientsArea;
    @FXML
    private TextArea instructionsArea;

    // Το όνομα του χρήστη - χρησιμοποιείται στην εκτύπωση ("Printed by...")
    private String username;

    /*
     * Setter για το username.
     * Καλείται από τον MainController πριν ανοίξει το παράθυρο.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /*
     * Κύρια μέθοδος που γεμίζει την οθόνη με τα στοιχεία της συνταγής.
     * Καλείται από τον MainController αφού περάσει το Recipe object.
     */
    public void setRecipe(Recipe recipe) {
        mealName.setText(recipe.getName());
        categoryLabel.setText(recipe.getCategory());
        areaLabel.setText(recipe.getArea());
        instructionsArea.setText(recipe.getDescription());

        // Φτιάχνουμε το κείμενο για τα υλικά
        StringBuilder ingredientsText = new StringBuilder();
        if (recipe.getIngredients() != null) {
            for (Ingredient ing : recipe.getIngredients()) {
                ingredientsText.append("• ").append(ing.getName()).append(": ").append(ing.getMeasure()).append("\n");
            }
        }
        ingredientsArea.setText(ingredientsText.toString());

        // Αν υπάρχει εικόνα, τη φορτώνουμε
        if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
            try {
                // Φόρτωση της εικόνας στο background (boolean true)
                mealImage.setImage(new Image(recipe.getImageUrl(), true));
            } catch (Exception e) {
                mealImage.setImage(null);
            }
        }
    }

    /*
     * Κλείνει το παράθυρο όταν πατηθεί το κουμπί "Close".
     * Παίρνει το Stage από το scene του τρέχοντος node και το κλείνει.
     */
    @FXML
    private void handleClose() {
        // Κλείνουμε το παράθυρο
        Stage stage = (Stage) mealName.getScene().getWindow();
        stage.close();
    }

    /*
     * Χειρίζεται την εκτύπωση της συνταγής. με σελιδοποίηση (pagination).
     * Αν το περιεχόμενο (κυρίως οι οδηγίες) είναι μεγάλο, το σπάμε σε πολλές
     * σελίδες.
     * Δημιουργεί ένα custom printable layout με:
     * 1. Εικόνα της συνταγής (150x150)
     * 2. Τίτλος με bold font
     * 3. Υλικά (Ingredients) με header
     * 4. Οδηγίες (Instructions) με header
     * 5. "Printed by [username]" στο τέλος
     * 
     * Χρησιμοποιεί το JavaFX PrinterJob API για το printing dialog.
     */
    @FXML
    private void handlePrint() {
        // Δημιουργία του PrinterJob
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(mealName.getScene().getWindow())) {

            // Παίρνουμε τις διαστάσεις της σελίδας
            PageLayout pageLayout = job.getJobSettings().getPageLayout();
            double printableWidth = pageLayout.getPrintableWidth();
            double printableHeight = pageLayout.getPrintableHeight();

            // Λίστα για να αποθηκεύσουμε όλα τα αντικείμενα (Nodes) που θέλουμε να
            // τυπώσουμε
            List<Node> nodesToPrint = new ArrayList<>();

            // 1. Εικόνα (Αν υπάρχει)
            if (mealImage.getImage() != null) {
                ImageView printImage = new ImageView(mealImage.getImage());
                printImage.setFitHeight(150);
                printImage.setFitWidth(150);
                printImage.setPreserveRatio(true);
                nodesToPrint.add(printImage);
            }

            // 2. Τίτλος Συνταγής
            Text title = new Text(mealName.getText());
            title.setFont(new Font("Calibri", 18));
            title.setStyle("-fx-font-weight: bold;");
            nodesToPrint.add(title);

            // 3. Επικεφαλίδα Υλικών
            Text ingHeader = new Text("INGREDIENTS");
            ingHeader.setFont(new Font("Calibri", 12));
            ingHeader.setStyle("-fx-font-weight: bold;");
            nodesToPrint.add(ingHeader);

            // 4. Υλικά (Ένα-ένα για να σπάνε σωστά στις σελίδες)
            // Σπάμε το κείμενο των υλικών ανά γραμμή
            String[] separateIngredients = ingredientsArea.getText().split("\n");
            for (String ing : separateIngredients) {
                Text ingItem = new Text(ing);
                ingItem.setFont(new Font("Calibri", 11));
                ingItem.setWrappingWidth(printableWidth - 40); // Αφήνουμε περιθώριο δεξιά
                nodesToPrint.add(ingItem);
            }

            // 5. Επικεφαλίδα Οδηγιών
            Text insHeader = new Text("INSTRUCTIONS");
            insHeader.setFont(new Font("Calibri", 12));
            insHeader.setStyle("-fx-font-weight: bold;");
            nodesToPrint.add(insHeader);

            // 6. Οδηγίες (Σπασμένες ανά παράγραφο/γραμμή για να αλλάζουν σελίδα)
            String[] separateInstructions = instructionsArea.getText().split("\n");
            for (String instr : separateInstructions) {
                if (!instr.trim().isEmpty()) {
                    Text instrItem = new Text(instr + "\n"); // Προσθέτουμε αλλαγή γραμμής για κενό
                    instrItem.setFont(new Font("Calibri", 11));
                    instrItem.setWrappingWidth(printableWidth - 40);
                    nodesToPrint.add(instrItem);
                }
            }

            // 7. Πληροφορίες Χρήστη (Printed by...)
            if (username != null && !username.isEmpty()) {
                Text userText = new Text("Printed by " + username);
                userText.setFont(new Font("Calibri", 10));
                userText.setStyle("-fx-font-style: italic;");
                nodesToPrint.add(userText);
            }

            // Σελιδοποίηση

            // Το τρέχον VBox που αντιπροσωπεύει τη σελίδα που γεμίζουμε
            VBox currentPage = new VBox(10); // 10px απόσταση μεταξύ των στοιχείων
            currentPage.setStyle("-fx-padding: 20; -fx-background-color: white;");

            double currentHeight = 0; // Το ύψος που έχουμε γεμίσει μέχρι στιγμής στη σελίδα

            for (Node node : nodesToPrint) {
                // Υπολογίζουμε το ύψος του αντικειμένου
                double nodeHeight = 0;
                if (node instanceof Text) {
                    // Το Text με wrapping έχει συγκεκριμένο ύψος LayoutBounds
                    nodeHeight = ((Text) node).getLayoutBounds().getHeight();
                } else if (node instanceof ImageView) {
                    nodeHeight = ((ImageView) node).getFitHeight();
                }

                // Ελέγχουμε αν χωράει στη σελίδα (+10 για το spacing του VBox)
                if (currentHeight + nodeHeight + 10 > printableHeight) {
                    // Αν δεν χωράει, τυπώνουμε την τρέχουσα σελίδα
                    job.printPage(pageLayout, currentPage);

                    // Και φτιάχνουμε καινούργια σελίδα
                    currentPage = new VBox(10);
                    currentPage.setStyle("-fx-padding: 20; -fx-background-color: white;");
                    currentHeight = 0;
                }

                // Προσθέτουμε το αντικείμενο στη σελίδα
                currentPage.getChildren().add(node);
                currentHeight += nodeHeight + 10;
            }

            // Τυπώνουμε και την τελευταία σελίδα (αν έχει κάτι μέσα)
            if (!currentPage.getChildren().isEmpty()) {
                job.printPage(pageLayout, currentPage);
            }

            // Τελειώνουμε το Job
            job.endJob();
        }
    }
}
