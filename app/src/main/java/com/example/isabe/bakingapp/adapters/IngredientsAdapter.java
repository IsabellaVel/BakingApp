package com.example.isabe.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isabe.bakingapp.R;
import com.example.isabe.bakingapp.objects.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isabe on 5/25/2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private List<Ingredient> ingredientList = new ArrayList();
    @BindView(R.id.ingreds_details)
    TextView recipeIngreds;

    private final Context mContext;
    @BindString(R.string.recipe_ingredients)
    String stIngredsHeader;


    public IngredientsAdapter(Context context, List<Ingredient> ingredients) {
        mContext = context;
        ingredientList = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipe_details, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient mIngredItem = ingredientList.get(position);
        final String ingredName = mIngredItem.getIngredientName();
        final float ingredQty = mIngredItem.getQuantityIngredient();
        final String ingredUnit = mIngredItem.getUnit();

        StringBuilder stringBuilder = new StringBuilder();
        stIngredsHeader = "<b>" + mContext.getString(R.string.ingredients) + "<b> ";
        // stringBuilder.append(Html.fromHtml(stIngredsHeader));

        for (int iIngred = 0; iIngred < ingredientList.size(); iIngred++) {
            mIngredItem = ingredientList.get(position);
            String nameIngredient = mIngredItem.getIngredientName();
            float qty = mIngredItem.getQuantityIngredient();
            String uMeasure = mIngredItem.getUnit();
            stringBuilder.append("\n");
            stringBuilder.append(formatIngredient(mContext, nameIngredient, qty, uMeasure));
        }
        holder.tvIngreds.setText(stringBuilder.toString());
    }

    public static String formatIngredient(Context context, String name, float qty, String unit) {
        String itemIngred = context.getResources().getString(R.string.ingredients_list);

        String qtyString = String.format(Locale.US, "%s", qty);
        if (qty == (long) qty) {
            qtyString = String.format(Locale.US, "%d", (long) qty);
        }
        return String.format(Locale.US, itemIngred, name, qtyString, unit);
    }


    @Override
    public int getItemCount() {
        return ingredientList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @BindView(R.id.ingreds_details)
        TextView tvIngreds;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public Ingredient getItem(int position) {
        return ingredientList.get(position);

    }
}
