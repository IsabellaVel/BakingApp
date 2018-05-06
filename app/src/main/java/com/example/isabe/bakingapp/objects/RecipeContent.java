package com.example.isabe.bakingapp.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample recipeName for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class RecipeContent {
    public final String id;
    public final String recipeName;
    public final List<Ingredient> ingredients;
    public final List<BakingStep> bakingSteps;
    public final int mServings = 0;
    public final String recipeImage;
    /**
     * An array of sample (dummy) items.
     */
    public static final List<RecipeItem> ITEMS = new ArrayList<RecipeItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, RecipeItem> RECIPE_MAP = new HashMap<String, RecipeItem>();

    public RecipeContent(String id, String recipeName, List<Ingredient> ingredients, List<BakingStep> bakingSteps, String recipeImage) {
        this.id = id;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.bakingSteps = bakingSteps;
        this.recipeImage = recipeImage;
    }

    private static void addRecipe(RecipeItem item) {
        ITEMS.add(item);
        RECIPE_MAP.put(item.id, item);
    }

    /**
     * private static RecipeItem createRecipeItem(int position) {
     * return new RecipeItem(String.valueOf(position), "Item " + position, ingredients, bakingSteps, makeDetails(position));
     * }
     **/
    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of recipeName.
     */
    public static class RecipeItem {
        public final String id;
        public final String recipeName;
        public final List<Ingredient> ingredients;
        public final List<BakingStep> bakingSteps;
        public final int mServings = 0;
        public final String recipeImage;

        public RecipeItem(String id, String recipeName, List<Ingredient> ingredients, List<BakingStep> bakingSteps, String image) {
            this.id = id;
            this.recipeName = recipeName;
            this.ingredients = ingredients;
            this.bakingSteps = bakingSteps;
            this.recipeImage = image;
        }

        @Override
        public String toString() {
            return recipeName;
        }
    }

    public String getId() {
        return id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<BakingStep> getBakingSteps() {
        return bakingSteps;
    }

    public int getmServings() {
        return mServings;
    }

    public String getRecipeImage() {
        return recipeImage;
    }
}
