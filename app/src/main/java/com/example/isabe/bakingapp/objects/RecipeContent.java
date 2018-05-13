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
    public String id;
    public String recipeName;
    public List<Ingredient> ingredients;
    public List<BakingStep> bakingSteps;
    public int mServings = 0;
    public String recipeImage;
    private Parcelable ingredsParcelable;
    private Parcelable stepsParcelable;
    /**
     * An array of sample (dummy) items.
     */
    public static final List<RecipeItem> ITEMS = new ArrayList<RecipeItem>();
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, RecipeItem> RECIPE_MAP = new HashMap<String, RecipeItem>();

    public RecipeContent(String id, String recipeName, List<Ingredient> ingredients,
                         List<BakingStep> bakingSteps, String recipeImage) {
        this.id = id;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.bakingSteps = bakingSteps;
        this.recipeImage = recipeImage;
    }

    public RecipeContent(Parcel in) {
        ingredsParcelable = in.readParcelable(Ingredient.class.getClassLoader());
        stepsParcelable = in.readParcelable(BakingStep.class.getClassLoader());

        id = in.readString();
        recipeName = in.readString();
        ingredients = in.readParcelable(Ingredient.class.getClassLoader());
        bakingSteps = in.readParcelable(BakingStep.class.getClassLoader());
        mServings = in.readInt();
        recipeImage = in.readString();
    }

    public static final Creator<RecipeContent> CREATOR = new Parcelable.Creator<RecipeContent>() {
        @Override
        public RecipeContent createFromParcel(Parcel in) {
            return new RecipeContent(in);
        }

        @Override
        public RecipeContent[] newArray(int size) {
            return new RecipeContent[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(recipeName);
        dest.writeParcelable(ingredsParcelable, flags);
        dest.writeParcelable(stepsParcelable, flags);
        dest.writeInt(mServings);
        dest.writeString(recipeImage);
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
