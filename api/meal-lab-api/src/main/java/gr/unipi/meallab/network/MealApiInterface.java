package gr.unipi.meallab.network;

import gr.unipi.meallab.model.MealResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*Interface που ορίζει τα endpoints του API (TheMealDB).
Το Retrofit χρησιμοποιεί αυτό το interface για να κάνει τα HTTP requests.*/
public interface MealApiInterface {

    // Αναζήτηση συνταγής με βάση το όνομα
    @GET("search.php")
    Call<MealResponse> searchMeals(@Query("s") String mealName);

    // Φιλτράρισμα με βάση το κύριο υλικό
    @GET("filter.php")
    Call<MealResponse> filterMealsByMainIngredient(@Query("i") String ingrediant);

    // Λήψη συνταγής με βάση το ID της
    @GET("lookup.php")
    Call<MealResponse> getMealById(@Query("i") String mealId);

    // Φιλτράρισμα με βάση την κατηγορία
    @GET("filter.php")
    Call<MealResponse> filterMealsByCategory(@Query("c") String category);

    // Φιλτράρισμα με βάση την περιοχή/κουζίνα
    @GET("filter.php")
    Call<MealResponse> filterMealsByArea(@Query("a") String area);

    // Λήψη μιας τυχαίας συνταγής
    @GET("random.php")
    Call<MealResponse> getRandomMeal();
}