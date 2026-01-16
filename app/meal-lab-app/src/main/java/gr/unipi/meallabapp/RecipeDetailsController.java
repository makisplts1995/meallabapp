package gr.unipi.meallabapp;

import gr.unipi.meallab.model.Ingredient;
import gr.unipi.meallab.model.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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

    public void setRecipe(Recipe recipe) {
        mealName.setText(recipe.getName());
        categoryLabel.setText(recipe.getCategory());
        areaLabel.setText(recipe.getArea());
        instructionsArea.setText(recipe.getDescription());

        // Δημιουργία του κειμένου των υλικών
        StringBuilder ingredientsText = new StringBuilder();
        if (recipe.getIngredients() != null) {
            for (Ingredient ing : recipe.getIngredients()) {
                ingredientsText.append("• ").append(ing.getName()).append(": ").append(ing.getMeasure()).append("\n");
            }
        }
        ingredientsArea.setText(ingredientsText.toString());

        // Φόρτωση της εικόνας αν υπάρχει URL
        if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
            try {
                // Φόρτωση της εικόνας στο background (boolean true)
                mealImage.setImage(new Image(recipe.getImageUrl(), true));
            } catch (Exception e) {
                mealImage.setImage(null);
            }
        }
    }

    // Κλείσιμο του παραθύρου όταν πατηθεί το κουμπί
    @FXML
    private void handleClose() {
        // Παίρνουμε το Stage (παράθυρο) από οποιοδήποτε στοιχείο της σκηνής και το
        // κλείνουμε
        Stage stage = (Stage) mealName.getScene().getWindow();
        stage.close();
    }
}
