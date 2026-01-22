package gr.unipi.meallabapp;

import gr.unipi.meallab.model.Ingredient;
import gr.unipi.meallab.model.Recipe;
import gr.unipi.meallab.network.MealService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import java.util.Optional;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.util.List;

/* Ο Controller της εφαρμογής.
Εδώ γίνεται η διαχείριση όλων των ενεργειών του χρήστη, όπως αναζήτηση,
προσθήκη στα αγαπημένα και εμφάνιση λεπτομερειών συνταγής.*/
public class MainController {

    // Σταθερές για τους τίτλους των Tabs και τις επιλογές αναζήτησης
    private static final String TITLE_FAVORITES = "My Favorites";
    private static final String TITLE_HISTORY = "Cooked History";
    private static final String SEARCH_BY_NAME = "By Name";
    private static final String SEARCH_BY_INGREDIENT = "By Ingredient";
    private static final String SEARCH_BY_CATEGORY = "By Category";
    private static final String SEARCH_BY_AREA = "By Area"; // Νέα επιλογή

    // Λίστα με τις κατηγορίες φαγητών
    private static final String[] CATEGORIES = {
            "Beef", "Chicken", "Dessert", "Lamb", "Miscellaneous", "Pasta",
            "Pork", "Seafood", "Side", "Starter", "Vegan", "Vegetarian", "Breakfast", "Goat"
    };

    // Λίστα με τις διαθέσ ιμες περιοχές/κουζίνες
    private static final String[] AREAS = {
            "American", "British", "Canadian", "Chinese", "Croatian", "Dutch",
            "Egyptian", "Filipino", "French", "Greek", "Indian", "Irish",
            "Italian", "Jamaican", "Japanese", "Kenyan", "Malaysian", "Mexican",
            "Moroccan", "Polish", "Portuguese", "Russian", "Spanish", "Thai",
            "Tunisian", "Turkish", "Ukrainian", "Vietnamese"
    };

    // Στοιχεία του FXML (GUI)
    @FXML
    private TextField searchField; // Πεδίο κειμένου για αναζήτηση
    @FXML
    private ComboBox<String> categoryCombo; // Λίστα επιλογής κατηγορίας
    @FXML
    private ComboBox<String> areaCombo; // Λίστα επιλογής περιοχής
    @FXML
    private Accordion mainAccordion; // Το μενού ακορντεόν
    @FXML
    private TableView<Recipe> searchTable; // Πίνακας αποτελεσμάτων αναζήτησης
    @FXML
    private TableColumn<Recipe, String> nameCol;
    @FXML
    private TableColumn<Recipe, String> categoryCol;
    @FXML
    private TableColumn<Recipe, String> areaCol;
    @FXML
    private TableView<Recipe> favTable; // Πίνακας αγαπημένων
    @FXML
    private TableView<Recipe> historyTable; // Πίνακας ιστορικού μαγειρέματος
    @FXML
    private Label statusLabel; // Ετικέτα για εμφάνιση μηνυμάτων κατάστασης
    @FXML
    private VBox detailsBox; // Το δεξί πάνελ με τις λεπτομέρειες της συνταγής
    @FXML
    private ImageView mealImage; // Εικόνα της συνταγής
    @FXML
    private Label mealName; // Όνομα συνταγής
    @FXML
    private Label tagsLabel; // Combined tags (Category | Area)
    @FXML
    private TextArea ingredientsArea; // Περιοχή κειμένου για τα υλικά
    @FXML
    private Button viewInstructionsBtn; // Κουμπί για εμφάνιση οδηγιών συνταγής σε νέο παράθυρο
    @FXML
    private Button addToFavoritesBtn; // Κουμπί προσθήκης στα αγαπημένα
    @FXML
    private Button removeFavoriteBtn; // Κουμπί αφαίρεσης από τα αγαπημένα
    @FXML
    private ComboBox<String> searchTypeCombo; // Επιλογή τρόπου αναζήτησης
    @FXML
    private Button searchBtn; // Κουμπί εκτέλεσης αναζήτησης
    @FXML
    private Button ingredientBtn; // Κουμπί αναζήτησης με βάση υλικό

    @FXML
    private Label userLabel; // εμφάνιση ονόματος χρήστη

    // Υπηρεσίες για ανάκτηση δεδομένων από το API και αποθήκευση τοπικά
    private final MealService mealService = new MealService();
    private final UserDataService userDataService = UserDataService.getInstance();

    // Η συνταγή που επιλέγουμε
    private Recipe currentRecipe;

    /*
     * Η initialize τρέχει αυτόματα όταν ανοίγει το παράθυρο.
     * Εδώ κάνουμε τις αρχικές ρυθμίσεις.
     */
    @FXML
    public void initialize() {
        /*
         * Εμφάνιση ονόματος χρήστη στην πάνω δεξιά γωνία.
         * Παίρνουμε το όνομα από το UserDataService και το μορφοποιούμε
         * ώστε να ξεκινάει με κεφαλαίο γράμμα.
         */
        if (userLabel != null) {
            String currentUser = userDataService.getUsername();
            // Κεφαλαίο το πρώτο γράμμα για ωραία εμφάνιση
            if (currentUser != null && !currentUser.isEmpty()) {
                String displayUser = currentUser.substring(0, 1).toUpperCase() + currentUser.substring(1);
                userLabel.setText("User: " + displayUser);
            }
            // Override του inline style για να φαίνεται με άσπρα γράμματα
            userLabel.setStyle("-fx-text-fill: white; -fx-font-weight: 700; -fx-font-size: 14px;");
        }

        // Initialize search controls
        setupSearchControls(); // Ρύθμιση φίλτρων αναζήτησης

        setupTableSelections(); // Ρύθμιση λιστών (tables)
        setupAccordion(); // Ρύθμιση του μενού πλοήγησης

        // Αρχική κατάσταση: Δεν βλέπουμε τα αγαπημένα ούτε το ιστορικό
        updateActionButtonsVisibility(true, false, false);
        resetAccordionState(); // Ανοίγουμε την πρώτη καρτέλα

        // Placeholders for clean UI state
        searchTable.setPlaceholder(new Label("No search results yet. Search to get recipes!"));
        favTable.setPlaceholder(new Label("No favourites yet"));
        historyTable.setPlaceholder(new Label("No cooking history! Start cooking to add recipes here!"));

        // handle για ESC για logout
        mainAccordion.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                        handleLogout();
                    }
                });
            }
        });
    }

    /*
     * Κουμπί Logout.
     * Μας γυρνάει στην αρχική οθόνη.
     */
    @FXML
    private void handleLogout() {
        try {
            App.setRoot("welcome_view");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    // Ρυθμίζει τα ComboBox και τα listeners για την αναζήτηση
    // Ρυθμίζει τα ComboBox και τα listeners για την αναζήτηση
    private void setupSearchControls() {
        categoryCombo.setItems(FXCollections.observableArrayList(CATEGORIES));
        areaCombo.setItems(FXCollections.observableArrayList(AREAS));
        searchTypeCombo
                .setItems(FXCollections.observableArrayList(SEARCH_BY_NAME, SEARCH_BY_INGREDIENT, SEARCH_BY_CATEGORY,
                        SEARCH_BY_AREA));

        // Όταν αλλάζει ο τύπος αναζήτησης, αλλάζουμε τα ορατά πεδία
        searchTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> updateSearchMode(newVal));

        // Όταν επιλέγεται κατηγορία, κάνουμε αυτόματη αναζήτηση
        categoryCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null)
                handleSearchByCategory(newVal);
        });

        // Όταν επιλέγεται περιοχή, κάνουμε αυτόματη αναζήτηση
        areaCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null)
                handleSearchByArea(newVal);
        });
    }

    // Ρυθμίζει τι συμβαίνει όταν επιλέγουμε μια γραμμή στους πίνακες
    private void setupTableSelections() {
        setupTableSelection(searchTable);
        setupTableSelection(favTable);
        setupTableSelection(historyTable);
    }

    private void setupTableSelection(TableView<Recipe> table) {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null)
                showRecipeDetails(newVal); // Εμφάνιση λεπτομερειών
        });
    }

    // Διαχειρίζεται το άνοιγμα/κλείσιμο των καρτελών του Accordion
    private void setupAccordion() {
        mainAccordion.expandedPaneProperty().addListener((obs, oldPane, newPane) -> {
            if (newPane != null) {
                String title = newPane.getText();
                if (TITLE_FAVORITES.equals(title)) {
                    handleShowFavorites(); // Αν ανοίξουν τα αγαπημένα, φόρτωσέ τα
                } else if (TITLE_HISTORY.equals(title)) {
                    handleShowCooked(); // Αν ανοίξει το ιστορικό, φόρτωσέ το
                } else {
                    // Search Results
                    // Αποτελέσματα αναζήτησης
                    updateActionButtonsVisibility(true, false, false);
                }
            }
        });
    }

    // Επαναφέρει το Accordion στην πρώτη καρτέλα (Search Results)
    private void resetAccordionState() {
        if (!mainAccordion.getPanes().isEmpty()) {
            mainAccordion.setExpandedPane(mainAccordion.getPanes().get(0));
        }
    }

    // Αναζήτηση με βάση την κατηγορία
    private void handleSearchByCategory(String category) {
        statusLabel.setText("Searching category: " + category);
        searchField.clear();
        updateActionButtonsVisibility(true, false, false);
        updateColumnVisibility(true, false); // Δείχνουμε Category, Κρύβουμε Area
        resetAccordionState();

        // Εκτέλεση σε ξεχωριστό thread για να μην κολλήσει το UI
        startTask(() -> {
            List<Recipe> results = mealService.filterRecipes("c", category);
            // API does not return category in filter response, so we set it manually
            for (Recipe r : results) {
                r.setCategory(category);
                r.setArea("-"); // Area is unknown in this mode
            }
            updateList(searchTable, results, "Found " + results.size() + " recipes in " + category + ".");
        });
    }

    // Αναζήτηση με βάση το όνομα (χειριστής κουμπιού "Search")
    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query.isBlank())
            return;

        statusLabel.setText("Searching for: " + query);
        updateActionButtonsVisibility(true, false, false);
        updateColumnVisibility(true, true); // Εμφάνιση όλων
        resetAccordionState();

        startTask(() -> {
            List<Recipe> results = mealService.searchRecipesByName(query);
            updateList(searchTable, results, "Search completed. Found " + results.size() + " recipes.");
        });
    }

    // Αναζήτηση με βάση συστατικό
    @FXML
    private void handleSearchIngredient() {
        String query = searchField.getText();
        if (query.isBlank())
            return;

        statusLabel.setText("Searching by ingredient: " + query);
        updateActionButtonsVisibility(true, false, false);
        updateColumnVisibility(true, true); // Εμφάνιση όλων
        resetAccordionState();

        startTask(() -> {
            List<Recipe> results = mealService.getRecipesByIngredient(query);
            updateList(searchTable, results, "Ingredient search completed. Found " + results.size() + " recipes.");
        });
    }

    // "I'm Feeling Lucky" - Τυχαία συνταγή
    @FXML
    private void handleRandom() {
        statusLabel.setText("Fetching random recipe...");
        updateActionButtonsVisibility(true, false, false);
        resetAccordionState();

        startTask(() -> {
            Recipe random = mealService.getRandomRecipe();
            Platform.runLater(() -> {
                if (random != null) {
                    searchTable.setItems(FXCollections.observableArrayList(random));
                    searchTable.getSelectionModel().select(0); // Αυτόματη επιλογή
                    statusLabel.setText("Random recipe found!");
                } else {
                    statusLabel.setText("No random recipe returned.");
                }
            });
        });
    }

    // Εμφάνιση λίστας αγαπημένων από το αρχείο
    private void handleShowFavorites() {
        updateActionButtonsVisibility(false, true, false);
        List<Recipe> favs = userDataService.getFavorites();
        favTable.setItems(FXCollections.observableArrayList(favs));
        statusLabel.setText("Showing Favorites (" + favs.size() + ")");
    }

    // Εμφάνιση ιστορικού μαγειρεμένων συνταγών
    private void handleShowCooked() {
        updateActionButtonsVisibility(false, false, true); // Mode Ιστορικού: Κρύψε Mark as Cooked, Δείξε Mark as
                                                           // Uncooked
        List<Recipe> cooked = userDataService.getCooked();
        historyTable.setItems(FXCollections.observableArrayList(cooked));
        statusLabel.setText("Showing Cooked History (" + cooked.size() + ")");
    }

    // Κουμπί προσθήκης στα αγαπημένα
    @FXML
    private void handleAddToFavorites() {
        if (currentRecipe == null)
            return;
        userDataService.addFavorite(currentRecipe);
        statusLabel.setText("Added to Favorites: " + currentRecipe.getName());
        // Εμφάνιση ενημερωτικού μηνύματος (Information Alert) επιτυχίας
        showAlert("Success", "Recipe added to favorites successfully.", AlertType.INFORMATION);
    }

    // Κουμπί σήμανσης ως μαγειρεμένο
    @FXML
    private void handleMarkAsCooked() {
        if (currentRecipe == null)
            return;
        userDataService.addCooked(currentRecipe);
        statusLabel.setText("Marked as Cooked: " + currentRecipe.getName());
        // Εμφάνιση ενημερωτικού μηνύματος (Information Alert) ότι η ενέργεια
        // ολοκληρώθηκε
        showAlert("Success", "Recipe marked as cooked.", AlertType.INFORMATION);
    }

    // Κουμπί αφαίρεσης από τα αγαπημένα
    @FXML
    private void handleRemoveFromFavorites() {
        if (currentRecipe == null)
            return;

        /*
         * Ζητάμε επιβεβαίωση από τον χρήστη πριν τη διαγραφή.
         * Χρησιμοποιούμε AlertType.CONFIRMATION που δίνει επιλογές OK/Cancel.
         */
        Optional<ButtonType> result = showAlert("Confirm Removal",
                "Are you sure you want to remove this recipe from favorites?", AlertType.CONFIRMATION);

        // Αν ο χρήστης πάτησε OK, προχωράμε στη διαγραφή
        if (result.isPresent() && result.get() == ButtonType.OK) {
            userDataService.removeFavorite(currentRecipe);
            statusLabel.setText("Removed from Favorites: " + currentRecipe.getName());

            // Ανανέωση της λίστας αν είμαστε ήδη στην καρτέλα των αγαπημένων
            TitledPane expanded = mainAccordion.getExpandedPane();
            if (expanded != null && TITLE_FAVORITES.equals(expanded.getText())) {
                handleShowFavorites();
                if (favTable.getItems().isEmpty()) {
                    detailsBox.setVisible(false); // Κρύψε τις λεπτομέρειες αν άδειασε η λίστα
                }
            }
        }
    }

    // Ελέγχει την ορατότητα των κουμπιών "Add" και "Remove" favorite
    @FXML
    private Button markAsCookedBtn; // Κουμπί σήμανσης ως μαγειρεμένο
    @FXML
    private Button removeCookedBtn; // Κουμπί αναίρεσης μαγειρέματος (Mark as Uncooked)

    @FXML
    private void handleRemoveFromCooked() {
        if (currentRecipe == null)
            return;

        // Παρόμοια επιβεβαίωση για την αφαίρεση από το ιστορικό
        Optional<ButtonType> result = showAlert("Confirm Removal",
                "Mark recipe as uncooked? This will remove it from history.", AlertType.CONFIRMATION);

        if (result.isPresent() && result.get() == ButtonType.OK) {
            userDataService.removeCooked(currentRecipe);
            statusLabel.setText("Removed from Cooked History: " + currentRecipe.getName());

            // Ανανέωση της λίστας αν είμαστε στο ιστορικό
            TitledPane expanded = mainAccordion.getExpandedPane();
            if (expanded != null && TITLE_HISTORY.equals(expanded.getText())) {
                handleShowCooked();
                if (historyTable.getItems().isEmpty()) {
                    detailsBox.setVisible(false);
                }
            }
        }
    }

    // Ελέγχει την ορατότητα των κουμπιών ενεργειών ανάλογα με το Tab που
    // βρισκόμαστε
    private void updateActionButtonsVisibility(boolean isSearch, boolean isFavorites, boolean isHistory) {
        // Add to Favorites: Ορατό σε Search και History (αν θέλουμε), κρυφό στα
        // Favorites
        addToFavoritesBtn.setVisible(!isFavorites);
        addToFavoritesBtn.setManaged(!isFavorites);

        // Remove Favorite: Ορατό μόνο στα Favorites
        removeFavoriteBtn.setVisible(isFavorites);
        removeFavoriteBtn.setManaged(isFavorites);

        // Mark as Cooked: Ορατό σε Search και Favorites, ΚΡΥΦΟ στο History
        markAsCookedBtn.setVisible(!isHistory);
        markAsCookedBtn.setManaged(!isHistory);

        // Mark as Uncooked: Ορατό ΜΟΝΟ στο History
        removeCookedBtn.setVisible(isHistory);
        removeCookedBtn.setManaged(isHistory);
    }

    // Ενημέρωση του πίνακα αποτελεσμάτων (πρέπει να γίνει στο JavaFX Thread)
    private void updateList(TableView<Recipe> table, List<Recipe> results, String status) {
        Platform.runLater(() -> {
            table.setItems(FXCollections.observableArrayList(results));
            statusLabel.setText(status);
        });
    }

    // Φόρτωση και εμφάνιση λεπτομερειών συνταγής
    private void showRecipeDetails(Recipe briefRecipe) {
        // Αν λείπουν οι οδηγίες, κάνε νέο αίτημα στο API για πλήρη στοιχεία
        if (briefRecipe.getDescription() == null || briefRecipe.getDescription().isEmpty()) {
            statusLabel.setText("Fetching details for " + briefRecipe.getName() + "...");
            startTask(() -> {
                Recipe fullDetails = mealService.getRecipeById(briefRecipe.getId());
                if (fullDetails != null) {
                    Platform.runLater(() -> displayDetails(fullDetails));
                }
            });
        } else {
            displayDetails(briefRecipe);
        }
    }

    // Γεμίζει τα πεδία της δεξιάς στήλης με τα στοιχεία της συνταγής
    private void displayDetails(Recipe recipe) {
        this.currentRecipe = recipe;
        detailsBox.setVisible(true);

        mealName.setText(recipe.getName());
        tagsLabel.setText(recipe.getCategory() + " | " + recipe.getArea());

        // Μορφοποίηση λίστας υλικών με bullets
        StringBuilder ingredientsText = new StringBuilder();
        if (recipe.getIngredients() != null) {
            for (Ingredient ing : recipe.getIngredients()) {
                ingredientsText.append("• ").append(ing.getName()).append(": ").append(ing.getMeasure()).append("\n");
            }
        }
        ingredientsArea.setText(ingredientsText.toString());

        // Φόρτωση εικόνας σε background thread
        if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
            startTask(() -> {
                Image image = new Image(recipe.getImageUrl(), true);
                Platform.runLater(() -> mealImage.setImage(image));
            });
        } else {
            mealImage.setImage(null);
        }
    }

    // Αλλάζει την ορατότητα των πεδίων αναζήτησης ανάλογα με την επιλογή
    // στοComboBox
    private void updateSearchMode(String mode) {
        if (mode == null)
            return;

        // Καθαρισμός του προηγούμενου state
        // για να αποφύγουμε μπέρδεμα του χρήστη στις αλλαγές του search mode
        searchTable.getItems().clear();
        statusLabel.setText("");
        detailsBox.setVisible(false);

        // Reset τις επιλογές για να μην μένουν όταν επιστρέφουμε στο mode
        // Χρησιμοποιούμε setValue(null) για να εμφανιστεί ξανά το placeholder text
        categoryCombo.setValue(null);
        areaCombo.setValue(null);
        // Καθαρισμός και του text field
        searchField.clear();

        // Κρύβουμε τα πάντα αρχικά
        searchField.setVisible(false);
        searchField.setManaged(false);
        categoryCombo.setVisible(false);
        categoryCombo.setManaged(false);
        areaCombo.setVisible(false);
        areaCombo.setManaged(false);
        searchBtn.setVisible(false);
        searchBtn.setManaged(false);
        ingredientBtn.setVisible(false);
        ingredientBtn.setManaged(false);

        // Εμφανίζουμε μόνο ότι χρειάζεται
        switch (mode) {
            case SEARCH_BY_NAME -> {
                searchField.setVisible(true);
                searchField.setManaged(true);
                searchBtn.setVisible(true);
                searchBtn.setManaged(true);
                searchField.setPromptText("Enter recipe name...");
            }
            case SEARCH_BY_INGREDIENT -> {
                searchField.setVisible(true);
                searchField.setManaged(true);
                ingredientBtn.setVisible(true);
                ingredientBtn.setManaged(true);
                searchField.setPromptText("Enter ingredient...");
            }
            case SEARCH_BY_CATEGORY -> {
                categoryCombo.setVisible(true);
                categoryCombo.setManaged(true);
            }
            case SEARCH_BY_AREA -> {
                areaCombo.setVisible(true);
                areaCombo.setManaged(true);
            }
        }
    }

    // Αναζήτηση με βάση την περιοχή
    private void handleSearchByArea(String area) {
        statusLabel.setText("Searching area: " + area);
        updateActionButtonsVisibility(true, false, false);
        updateColumnVisibility(false, true); // Κρύβουμε Category, Δείχνουμε Area
        resetAccordionState();

        startTask(() -> {
            // Χρησιμοποιούμε το MealService για να πάρουμε τις συνταγές ανά περιοχή
            List<Recipe> results = mealService.filterMealsByArea(area);

            // Περιορισμός του API: to filter response επιστρέφει μόνο name, thumb, και id.
            // Ορίζουμε το Area χειροκίνητα επειδή το γνωρίζουμε από το search query.
            for (Recipe r : results) {
                r.setArea(area);
                r.setCategory("-"); // Το Category είναι άγνωστο σε αυτό το mode
            }
            updateList(searchTable, results, "Found " + results.size() + " recipes from " + area);
        });
    }

    // Βοηθητική μέθοδος για εμφάνιση/απόκρυψη στηλών
    private void updateColumnVisibility(boolean showCategory, boolean showArea) {
        if (categoryCol != null)
            categoryCol.setVisible(showCategory);
        if (areaCol != null)
            areaCol.setVisible(showArea);
    }

    // Βοηθητική μέθοδος για εκτέλεση εργασιών σε νέο Thread (για να μην παγώνει το
    // UI)
    private void startTask(ThrowingRunnable task) {
        new Thread(() -> {
            try {
                task.run();
            } catch (Exception e) {
                Platform.runLater(() -> statusLabel.setText("Error occurred: " + e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }

    // Event handler για το άνοιγμα των οδηγιών σε νέο παράθυρο
    @FXML
    private void handleViewInstructions() {
        if (currentRecipe == null)
            return;
        try {
            // Φόρτωση του FXML αρχείου για το παράθυρο λεπτομερειών
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("recipe_details.fxml"));
            javafx.scene.Parent root = loader.load();

            // controller του νέου FXML για να περάσουμε τη συνταγή
            RecipeDetailsController controller = loader.getController();
            controller.setRecipe(currentRecipe);
            controller.setUsername(userDataService.getUsername()); // Pass username for printing

            // Δημιουργία νέου Stage (παραθύρου)
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Recipe Details - " + currentRecipe.getName());

            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            // Add ESC key handler to close the window
            scene.setOnKeyPressed(event -> {
                if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                    stage.close();
                }
            });

            stage.setScene(scene);
            stage.show(); // Εμφάνιση παραθύρου
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error opening details: " + e.getMessage());
        }
    }

    /*
     * Βοηθητική μέθοδος για την εμφάνιση παραθύρων διαλόγου (Alerts).
     * Δέχεται τον τίτλο, το μήνυμα και τον τύπο του Alert (Info, Warning,
     * Confirmation κλπ).
     * Επιστρέφει το αποτέλεσμα (κουμπί που πατήθηκε) τυλιγμένο σε Optional.
     */
    private Optional<ButtonType> showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Αυτόματη προσαρμογή μεγέθους για να φαίνεται όλο το κείμενο
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setMinHeight(Region.USE_PREF_SIZE);

        return alert.showAndWait(); // Περιμένουμε την απάντηση του χρήστη
    }

    // Functional Interface που επιτρέπει εξαιρέσεις (Exceptions)
    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
