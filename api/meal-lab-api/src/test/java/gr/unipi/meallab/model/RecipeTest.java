package gr.unipi.meallab.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    @Test
    void testIngredientDeserialization() throws IOException {
        String json = "{"
                + "\"idMeal\": \"12345\","
                + "\"strMeal\": \"Test Meal\","
                + "\"strIngredient1\": \"Chicken\","
                + "\"strMeasure1\": \"1kg\","
                + "\"strIngredient2\": \"Salt\","
                + "\"strMeasure2\": \"1 tsp\","
                + "\"strIngredient3\": \"\","
                + "\"strMeasure3\": \"\","
                + "\"strIngredient21\": \"ShouldIgnore\""
                + "}";

        ObjectMapper mapper = new ObjectMapper();
        Recipe recipe = mapper.readValue(json, Recipe.class);

        assertEquals("12345", recipe.getId());
        assertEquals("Test Meal", recipe.getName());

        List<Ingredient> ingredients = recipe.getIngredients();
        assertNotNull(ingredients);
        assertEquals(2, ingredients.size());

        assertEquals("Chicken", ingredients.get(0).getName());
        assertEquals("1kg", ingredients.get(0).getMeasure());

        assertEquals("Salt", ingredients.get(1).getName());
        assertEquals("1 tsp", ingredients.get(1).getMeasure());
    }

    @Test
    void testEmptyIngredients() {
        Recipe recipe = new Recipe();
        List<Ingredient> ingredients = recipe.getIngredients();
        assertNotNull(ingredients);
        assertTrue(ingredients.isEmpty());
    }
}
