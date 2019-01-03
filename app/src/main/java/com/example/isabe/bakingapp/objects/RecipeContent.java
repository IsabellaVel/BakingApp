package com.example.isabe.bakingapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

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
public class RecipeContent implements Parcelable {
    public int id;
    public String recipeName;
    public List<Ingredient> ingredients = new ArrayList<>();
    public List<BakingStep> bakingSteps = new ArrayList<>();
    public int mServings = 0;
    public String recipeImage;
    public Ingredient ingredientItem;
    public BakingStep bakingStep;
    /**
     * An array of sample (dummy) items.
     */
    public static final List<RecipeItem> ITEMS = new ArrayList<RecipeItem>();
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, RecipeItem> RECIPE_MAP = new HashMap<String, RecipeItem>();

    public RecipeContent(int id, String recipeName, List<Ingredient> ingredients,
                         List<BakingStep> bakingSteps, String recipeImage) {
        this.id = id;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.bakingSteps = bakingSteps;
        this.recipeImage = recipeImage;
    }

    public RecipeContent(Parcel in) {
        id = in.readInt();
        recipeName = in.readString();
        in.readList(ingredients, getClass().getClassLoader());
        in.readList(bakingSteps, BakingStep.class.getClassLoader());
        recipeImage = in.readString();
    }

    public static final Parcelable.Creator<RecipeContent> CREATOR = new Parcelable.Creator<RecipeContent>() {
        @Override
        public RecipeContent createFromParcel(Parcel in) {
            return new RecipeContent(in);
        }

        @Override
        public RecipeContent[] newArray(int size) {
            return new RecipeContent[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(recipeName);
        dest.writeList(ingredients);
        dest.writeList(bakingSteps);
        dest.writeString(recipeImage);
    }

    /**
     * A dummy item representing a piece of recipeName.
     */
    public static class RecipeItem {
        public final int id;
        public final String recipeName;
        public final List<Ingredient> ingredients;
        public final List<BakingStep> bakingSteps;
        public final String recipeImage;

        public RecipeItem(int id, String recipeName, List<Ingredient> ingredients, List<BakingStep> bakingSteps, String image) {
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

    public int getId() {
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