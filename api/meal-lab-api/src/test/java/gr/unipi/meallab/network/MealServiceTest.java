package gr.unipi.meallab.network;

import gr.unipi.meallab.model.MealResponse;
import gr.unipi.meallab.model.Recipe;
import okhttp3.Request;
import okio.Timeout;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MealServiceTest {

    // Helper method to create a Stub Call that returns specific data

    // Stub implementation of Retrofit Call
    static class StubCall<T> implements Call<T> {
        private final Response<T> response;

        StubCall(Response<T> response) {
            this.response = response;
        }

        @Override
        public Response<T> execute() throws IOException {
            return response;
        }

        @Override
        public void enqueue(Callback<T> callback) {
        }

        @Override
        public boolean isExecuted() {
            return true;
        }

        @Override
        public void cancel() {
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public Call<T> clone() {
            return this;
        }

        @Override
        public Request request() {
            return new Request.Builder().url("http://localhost").build();
        }

        @Override
        public Timeout timeout() {
            return Timeout.NONE;
        }
    }

    // Manual Mock of MealApiInterface
    static class MockApi implements MealApiInterface {
        private final MealResponse successResponse;

        public MockApi(List<Recipe> recipes) {
            this.successResponse = new MealResponse();
            this.successResponse.setRecipes(recipes);
        }

        // Constructor for returning null/empty
        public MockApi() {
            this.successResponse = new MealResponse(); // logic can vary
        }

        @Override
        public Call<MealResponse> searchMeals(String name) {
            if ("fail".equals(name)) {
                // Return error response
                return new StubCall<>(Response.error(404,
                        okhttp3.ResponseBody.create("", okhttp3.MediaType.parse("application/json"))));
            }
            return new StubCall<>(Response.success(successResponse));
        }

        @Override
        public Call<MealResponse> getMealById(String id) {
            if ("999".equals(id)) {
                MealResponse empty = new MealResponse();
                return new StubCall<>(Response.success(empty));
            }
            return new StubCall<>(Response.success(successResponse));
        }

        @Override
        public Call<MealResponse> getRandomMeal() {
            return new StubCall<>(Response.success(successResponse));
        }

        @Override
        public Call<MealResponse> filterMealsByMainIngredient(String ingredient) {
            return new StubCall<>(Response.success(successResponse));
        }

        @Override
        public Call<MealResponse> filterMealsByCategory(String category) {
            return new StubCall<>(Response.success(successResponse));
        }

        @Override
        public Call<MealResponse> filterMealsByArea(String area) {
            return new StubCall<>(Response.success(successResponse));
        }
    }

    @Test
    void testDefaultConstructor() {
        MealService service = new MealService();
        assertNotNull(service);
    }

    @Test
    void testSearchRecipesByName() throws IOException {
        Recipe r = new Recipe();
        r.setId("1");
        MealApiInterface mockApi = new MockApi(Collections.singletonList(r));
        MealService service = new MealService(mockApi);

        List<Recipe> result = service.searchRecipesByName("test");
        assertFalse(result.isEmpty());
        assertEquals("1", result.get(0).getId());
    }

    @Test
    void testGetRecipesByIngredient() throws IOException {
        Recipe r = new Recipe();
        MealApiInterface mockApi = new MockApi(Collections.singletonList(r));
        MealService service = new MealService(mockApi);

        List<Recipe> result = service.getRecipesByIngredient("Chicken");
        assertFalse(result.isEmpty());
    }

    @Test
    void testFilterRecipes() throws IOException {
        Recipe r = new Recipe();
        MealApiInterface mockApi = new MockApi(Collections.singletonList(r));
        MealService service = new MealService(mockApi);

        assertFalse(service.filterRecipes("c", "Beef").isEmpty());
        assertFalse(service.filterRecipes("a", "Greek").isEmpty());
        assertFalse(service.filterRecipes("i", "Salt").isEmpty());

        // Test invalid
        assertTrue(service.filterRecipes("invalid", "x").isEmpty());
    }

    @Test
    void testGetRecipeById() throws IOException {
        Recipe r = new Recipe();
        r.setId("123");
        MealApiInterface mockApi = new MockApi(Collections.singletonList(r));
        MealService service = new MealService(mockApi);

        Recipe result = service.getRecipeById("123");
        assertNotNull(result);
        assertEquals("123", result.getId());
    }

    @Test
    void testGetRecipeByIdNotFound() throws IOException {
        MealApiInterface mockApi = new MockApi(Collections.emptyList()); // or handle inside mock
        MealService service = new MealService(mockApi);

        Recipe result = service.getRecipeById("999");
        assertNull(result);
    }

    @Test
    void testGetRandomRecipe() throws IOException {
        Recipe r = new Recipe();
        MealApiInterface mockApi = new MockApi(Collections.singletonList(r));
        MealService service = new MealService(mockApi);

        assertNotNull(service.getRandomRecipe());
    }

    @Test
    void testNetworkFailure() throws IOException {
        MealApiInterface mockApi = new MockApi(Collections.emptyList());
        MealService service = new MealService(mockApi);

        List<Recipe> result = service.searchRecipesByName("fail");
        assertTrue(result.isEmpty());
    }
}
