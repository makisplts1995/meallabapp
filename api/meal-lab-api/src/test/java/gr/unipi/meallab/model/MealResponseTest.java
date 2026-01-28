package gr.unipi.meallab.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MealResponseTest {

    @Test
    void testGettersAndSetters() {
        Recipe recipe1 = new Recipe();
        recipe1.setName("Pasta");

        Recipe recipe2 = new Recipe();
        recipe2.setName("Pizza");

        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);

        MealResponse response = new MealResponse();
        response.setRecipes(recipes);

        assertNotNull(response.getRecipes());
        assertEquals(2, response.getRecipes().size());
        assertEquals("Pasta", response.getRecipes().get(0).getName());
        assertEquals("Pizza", response.getRecipes().get(1).getName());
    }
}
