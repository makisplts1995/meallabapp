package gr.unipi.meallab.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    @Test
    void testConstructorAndGetters() {
        Ingredient ingredient = new Ingredient("Sugar", "1 tsp");

        assertEquals("Sugar", ingredient.getName());
        assertEquals("1 tsp", ingredient.getMeasure());
    }

    @Test
    void testSetters() {
        Ingredient ingredient = new Ingredient("Salt", "Pinch");

        ingredient.setName("Pepper");
        ingredient.setMeasure("Dash");

        assertEquals("Pepper", ingredient.getName());
        assertEquals("Dash", ingredient.getMeasure());
    }

    @Test
    void testToString() {
        Ingredient ingredient = new Ingredient("Flour", "500g");
        assertEquals("Flour (500g)", ingredient.toString());
    }
}
