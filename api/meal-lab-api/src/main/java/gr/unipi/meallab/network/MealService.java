package gr.unipi.meallab.network;

import gr.unipi.meallab.model.MealResponse;
import gr.unipi.meallab.model.Recipe;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/*Κλάση Service που μεσολαβεί μεταξύ κώδικα και MealApiInterface.
Υλοποίηση της λογικής για κλήση σε API 
και επιστροφή αποτελεσμάτων.*/
public class MealService {

    private final MealApiInterface api;

    // Constructor: Αρχικοποιεί τη σύνδεση παίρνοντας το instance από τον Connector
    public MealService() {
        this(MealApiConnector.getMealApiInterface());
    }

    // Constructor για Dependency Injection (χρήσιμο για Tests)
    public MealService(MealApiInterface api) {
        this.api = api;
    }

    // 1. Αναζήτηση συνταγών με βάση το όνομα
    // Χρησιμοποιούμε το search.php endpoint
    public List<Recipe> searchRecipesByName(String name) throws IOException {
        Call<MealResponse> call = api.searchMeals(name);
        return executeCallList(call);
    }

    // 1b. Αναζήτηση συνταγών με βάση το υλικό (Φιλτράρισμα)
    public List<Recipe> getRecipesByIngredient(String ingredient) throws IOException {
        Call<MealResponse> call = api.filterMealsByMainIngredient(ingredient);
        return executeCallList(call);
    }

    // 1c. Αναζήτηση συνταγών με βάση την περιοχή (Φιλτράρισμα)
    public List<Recipe> filterMealsByArea(String area) throws IOException {
        Call<MealResponse> call = api.filterMealsByArea(area);
        return executeCallList(call);
    }

    // Γενική μέθοδος φιλτραρίσματος βάσει Κατηγορίας (c), Περιοχής (a) ή Υλικού (i)
    // Γενική μέθοδος φιλτραρίσματος βάσει Κατηγορίας (c), Περιοχής (a) ή Υλικού (i)
    public List<Recipe> filterRecipes(String type, String value) throws IOException {
        Call<MealResponse> call = switch (type) {
            case "c" -> api.filterMealsByCategory(value);
            case "a" -> api.filterMealsByArea(value);
            case "i" -> api.filterMealsByMainIngredient(value);
            default -> null;
        };

        if (call == null) {
            return Collections.emptyList();
        }
        return executeCallList(call);
    }

    // 2. Λήψη λεπτομερειών μιας συγκεκριμένης συνταγής με βάση το ID της
    public Recipe getRecipeById(String id) throws IOException {
        Call<MealResponse> call = api.getMealById(id);
        List<Recipe> recipes = executeCallList(call);
        if (!recipes.isEmpty()) {
            return recipes.get(0);
        }
        return null;
    }

    // 3. Λήψη μιας τυχαίας συνταγής (για την αρχική σελίδα ή πρόταση ημέρας)
    public Recipe getRandomRecipe() throws IOException {
        Call<MealResponse> call = api.getRandomMeal();
        List<Recipe> recipes = executeCallList(call);
        if (!recipes.isEmpty()) {
            return recipes.get(0);
        }
        return null;
    }

    /*
     * Βοηθητική μέθοδος για την εκτέλεση της κλήσης και την ασφαλή επιστροφή της
     * λίστας.
     * Ελέγχει αν η απάντηση είναι επιτυχής και αν υπάρχει response
     */
    private List<Recipe> executeCallList(Call<MealResponse> call) throws IOException {
        Response<MealResponse> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            List<Recipe> recipes = response.body().getRecipes();
            if (recipes != null) {
                return recipes;
            }
        }
        // Επιστροφή κενής λίστας αν δεν βρέθηκαν συνταγές ή υπήρξε πρόβλημα
        return Collections.emptyList();
    }
}
