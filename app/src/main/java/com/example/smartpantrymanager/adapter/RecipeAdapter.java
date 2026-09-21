package com.example.smartpantrymanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.RecipeDetailActivity;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private ArrayList<Recipe> recipes;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position) {

        Recipe recipe = recipes.get(position);
        holder.txtRecipeName.setText(recipe.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(
                    context,
                    RecipeDetailActivity.class
            );

            intent.putExtra("recipe_id", recipe.getId());
            intent.putExtra("recipe_name", recipe.getName());
            intent.putExtra(
                    "recipe_instructions",
                    recipe.getInstructions()
            );
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder
    extends RecyclerView.ViewHolder {
        TextView txtRecipeName;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            txtRecipeName = itemView.findViewById(
                    R.id.txtRecipeName
            );
        }
    }
}
