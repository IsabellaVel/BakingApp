package com.example.isabe.bakingapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by isabe on 5/5/2018.
 */

public class Ingredient implements Parcelable{
    public float quantityIngredient;
    public String unit;
    public String ingredientName;

    public Ingredient(float qty, String uoM, String text){
        this.quantityIngredient = qty;
        this.unit = uoM;
        this.ingredientName = text;
    }

    protected Ingredient(Parcel in) {
        quantityIngredient = in.readFloat();
        unit = in.readString();
        ingredientName = in.readString();
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getUnit() {
        return unit;
    }

    public float getQuantityIngredient() {
        return quantityIngredient;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(quantityIngredient);
        dest.writeString(unit);
        dest.writeString(ingredientName);
    }
}
