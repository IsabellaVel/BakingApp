package com.example.isabe.bakingapp.objects;

/**
 * Created by isabe on 5/5/2018.
 */

public class Ingredient {
    public float quantityIngredient;
    public String unit;
    public String ingredientName;

    public Ingredient(float qty, String uoM, String text){
        this.quantityIngredient = qty;
        this.unit = uoM;
        this.ingredientName = text;
    }

    public String getUnit() {
        return unit;
    }

    public float getQuantityIngredient() {
        return quantityIngredient;
    }

    public String getIngredientName() {
        return ingredientName;
    }
}
