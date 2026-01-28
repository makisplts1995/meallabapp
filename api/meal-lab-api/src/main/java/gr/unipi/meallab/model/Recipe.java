package gr.unipi.meallab.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/*Κλάση που αναπαριστά μια Συνταγή (Recipe).
Περιέχει όλες τις πληροφορίες που χρειαζόμαστε από το API.
το ignoreproperties μπαίνει για να αγνοήσει τα πεδία που δεν υπάρχουν στην κλάση*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipe {

    @JsonProperty("idMeal")
    private String id;

    @JsonProperty("strMeal")
    private String name;

    @JsonProperty("strArea")
    private String area;

    @JsonProperty("strCategory")
    private String category;

    @JsonProperty("strInstructions")
    private String description;

    @JsonProperty("strMealThumb") // URL για την εικόνα της συνταγής
    private String imageUrl;

    // Map για να αποθηκεύσουμε τα δυναμικά πεδία
    private Map<String, String> dynamicFields = new HashMap<>();

    // Λίστα για να αποθηκεύσουμε τα υλικά σε πιο εύχρηστη μορφή
    private List<Ingredient> ingredients;

    public Recipe() {
    }

    /*
     * Χρησιμοποιούμε το @JsonAnySetter για να πιάσουμε όλα τα πεδία,
     * που δεν έχουν δικό τους πεδίο στην κλάση.
     * Αυτό χρειάζεται επειδή το API επιστρέφει υλικά ως strIngredient1,
     * strIngredient2
     */
    @JsonAnySetter
    public void setDynamicField(String name, String value) {
        dynamicFields.put(name, value);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Επεξεργασία των δυναμικών πεδίων για τη δημιουργία της λίστας ingredients
    public List<Ingredient> getIngredients() {
        if (ingredients == null) {
            ingredients = new ArrayList<>();
            for (int i = 1; i <= 20; i++) {
                String name = dynamicFields.get("strIngredient" + i);
                String measure = dynamicFields.get("strMeasure" + i);

                if (name != null && !name.trim().isEmpty()) {
                    /* Αν το measure είναι null, βάζουμε κενό string για αποφυγή σφαλμάτων */
                    if (measure == null) {
                        measure = "";
                    }
                    ingredients.add(new Ingredient(name.trim(), measure.trim()));
                }
            }
        }
        return ingredients;
    }

    // Helper method to get ingredients formatted as a bulleted list string
    public String getFormattedIngredients() {
        StringBuilder sb = new StringBuilder();
        if (getIngredients() != null) {
            for (Ingredient ing : getIngredients()) {
                sb.append("• ").append(ing.getName()).append(": ").append(ing.getMeasure()).append("\n");
            }
        }
        return sb.toString();
    }
}