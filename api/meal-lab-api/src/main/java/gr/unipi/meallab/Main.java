package gr.unipi.meallab;

import java.io.IOException;
import java.util.List;

import gr.unipi.meallab.model.Recipe;
import gr.unipi.meallab.network.MealService;

/*Κύρια κλάση για τον έλεγχο της λειτουργικότητας του API (API functionality testing).
Εκτελεί διάφορα σενάρια χρήσης (use cases) για να επιβεβαιώσει ότι η επικοινωνία
με το TheMealDB λειτουργεί σωστά.   */
public class Main {
    /*
     * Η μέθοδος entry point της εφαρμογής. Εδώ γίνονται οι κλήσεις στις μεθόδους
     * του MealService.
     */
    public static void main(String[] args) {
        MealService service = new MealService();
        // Δοκιμή 1: Λήψη μιας τυχαίας συνταγής (Random Recipe)
        try {
            System.out.println("=== TEST 1: Random Recipe ===");
            Recipe random = service.getRandomRecipe();
            printRecipe(random);
            // Δοκιμή 2: Αναζήτηση συνταγής με βάση το όνομα (Search by Name)
            System.out.println("\n=== TEST 2: Search by Name (Arrabiata) ===");
            List<Recipe> searchResults = service.searchRecipesByName("Arrabiata");
            for (Recipe r : searchResults) {
                printRecipe(r);
            }
            // Δοκιμή 3: Φιλτράρισμα συνταγών με βάση το υλικό (Filter by Ingredient)
            System.out.println("\n=== TEST 3: Filter by Ingredient (Chicken_Breast) ===");
            List<Recipe> ingredientResults = service.getRecipesByIngredient("Chicken_Breast");
            // Φίλτρο για τον υλικό, το API επιστρέφει μόνο τον όνομα του υλικού και το
            // measure
            if (ingredientResults.size() > 3) {
                System.out.println("Found " + ingredientResults.size() + " recipes. Showing first 3:");
                for (int i = 0; i < 3; i++) {
                    printRecipe(ingredientResults.get(i));
                }
            } else {
                for (Recipe r : ingredientResults) {
                    printRecipe(r);
                }
            }
            // Δοκιμή 4: Λήψη αναλυτικών πληροφοριών συνταγής με βάση το ID (Get Details by
            // ID)
            if (!ingredientResults.isEmpty()) {
                System.out.println("\n=== TEST 4: Get Details by ID (" + ingredientResults.get(0).getId() + ") ===");
                Recipe details = service.getRecipeById(ingredientResults.get(0).getId());
                printRecipe(details);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Βοηθητική μέθοδος για την εκτύπωση των στοιχείων μιας συνταγής στην κονσόλα.
     * 
     * @param recipe Η συνταγή προς εκτύπωση
     */
    private static void printRecipe(Recipe recipe) {
        if (recipe == null) {
            System.out.println("Recipe is null");
            return;
        }
        System.out.println("ID: " + recipe.getId());
        System.out.println("Name: " + recipe.getName());
        System.out.println("Category: " + recipe.getCategory());
        System.out.println("Area: " + recipe.getArea());
        if (recipe.getIngredients().isEmpty()) {
            System.out.println("Ingredients: (Not available in brief view)");
        } else {
            System.out.println("Ingredients:");
            for (gr.unipi.meallab.model.Ingredient ing : recipe.getIngredients()) {
                System.out.println(" - " + ing.toString());
            }
        }
        System.out.println("Instructions: " + (recipe.getDescription() != null
                ? recipe.getDescription().substring(0, Math.min(recipe.getDescription().length(), 50)) + "..."
                : "N/A"));
        System.out.println("--------------------------------------------------");
    }
}