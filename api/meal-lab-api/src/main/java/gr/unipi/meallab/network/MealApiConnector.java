package gr.unipi.meallab.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/*Κλάση υπεύθυνη για τη σύνδεση με το API.
Χρησιμοποιεί το Retrofit για να δημιουργήσει την υλοποίηση του interface.
 */
public class MealApiConnector {
    // δήλωση της βάσης URL του API
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    // δήλωση του interface του API
    private static MealApiInterface mealApiInterface;

    /*
     * Singleton Pattern:Αυτό σημαίνει ότι δημιουργούμε μόνο ΜΙΑ φορά τη σύνδεση
     * και τη χρησιμοποιούμε παντού.
     */
    public static MealApiInterface getMealApiInterface() {
        if (mealApiInterface == null) {

            /*
             * Debugging: Εμφανίζει τα requests/responses στην κονσόλα,
             * για να βλέπουμε τι στέλνουμε/λαμβάνουμε
             */
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            /*
             * Setup Retrofit: Σύνδεση Base URL, Jackson Converter (για JSON) και OkHttp
             * Client
             */
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(client)
                    .build();

            // Δημιουργία της υλοποίησης του interface από το Retrofit
            mealApiInterface = retrofit.create(MealApiInterface.class);
        }
        return mealApiInterface;
    }
}