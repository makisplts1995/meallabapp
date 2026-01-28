package gr.unipi.meallab.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/*Κλάση POJO (Plain Old Java Object) για την αντιστοίχιση της απάντησης JSON
από το API. Το API επιστρέφει πάντα ένα αντικείμενο που περιέχει μια λίστα "meals:".*/
@JsonIgnoreProperties(ignoreUnknown = true) // Αγνοεί πεδία στο JSON που δεν υπάρχουν στην κλάση
public class MealResponse {

    @JsonProperty("meals") // Αντιστοιχίζει το πεδίο "meals" του JSON στη λίστα με τις συνταγές
    private List<Recipe> recipes;

    public List<Recipe> getRecipes() {
        return recipes; // Επιστρέφει τη λίστα με τις συνταγές
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes; // Ορίζει τη λίστα με τις συνταγές
    }
}