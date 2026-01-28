package gr.unipi.meallab.network;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MealApiConnectorTest {

    @Test
    void testConstructor() {
        MealApiConnector connector = new MealApiConnector();
        assertNotNull(connector);
    }

    @Test
    void testGetMealApiInterface() {
        MealApiInterface api = MealApiConnector.getMealApiInterface();
        assertNotNull(api, "API Interface should not be null");
    }


}
