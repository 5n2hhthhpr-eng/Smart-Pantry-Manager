package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.model.RecipeIngredient;
import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView txtRecipeDetailName;
    private TextView txtRecipeIngredients;
    private TextView txtRecipeInstructions;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        txtRecipeDetailName = findViewById(R.id.txtRecipeDetailName);
        txtRecipeIngredients = findViewById(R.id.txtRecipeIngredients);
        txtRecipeInstructions = findViewById(R.id.txtRecipeInstructions);

        databaseHelper = new DatabaseHelper(this);

        // Get recipe information from the Intent
        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        String recipeName = getIntent().getStringExtra("recipe_name");
        String recipeInstructions =
                getIntent().getStringExtra("recipe_instructions");

        //Display recipe name
        txtRecipeDetailName.setText(recipeName);

        // Display instructions
        txtRecipeInstructions.setText(recipeInstructions);

        // Get the recipe ingredients from SQLite
        ArrayList<RecipeIngredient> ingredients =
                databaseHelper.getIngredientsForRecipe(recipeId);

        StringBuilder ingredientText = new StringBuilder();

        for (RecipeIngredient ingredient : ingredients) {
            ingredientText
                    .append(". ")
                    .append(ingredient.getIngredientName())
                    .append(" - ")
                    .append(formatQuantity(ingredient.getQuantity()))
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append("\n");
        }

        txtRecipeIngredients.setText(
                ingredientText.toString()
        );
    }

    private String formatQuantity(double quantity) {
        if (quantity == (long) quantity) {
            return String.valueOf((long) quantity);
        }

        return String.valueOf(quantity);
    }
}