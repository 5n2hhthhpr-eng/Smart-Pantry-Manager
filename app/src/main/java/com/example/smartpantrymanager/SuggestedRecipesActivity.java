package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.adapter.RecipeAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecipes;
    private DatabaseHelper databaseHelper;

    private ArrayList<Recipe> recipes;
    private RecipeAdapter recipeAdapter;
    private Button btnNavPantry;
    private Button btnNavRecipes;
    private Button btnNavSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes);
        TextView txtNoRecipes = findViewById(R.id.txtNoRecipes);
        btnNavPantry = findViewById(R.id.btnNavPantry);
        btnNavRecipes = findViewById(R.id.btnNavRecipes);
        btnNavSettings = findViewById(R.id.btnNavSettings);

        databaseHelper = new DatabaseHelper(this);

        // Make sure the recipes exist in the database
        databaseHelper.seedRecipes();

        // Get all recipes from the database
        recipes = databaseHelper.getMatchingRecipes();
        if (recipes.isEmpty()) {
            txtNoRecipes.setVisibility(TextView.VISIBLE);
            recyclerViewRecipes.setVisibility(RecyclerView.GONE);
        } else {
            txtNoRecipes.setVisibility(TextView.GONE);
            recyclerViewRecipes.setVisibility(RecyclerView.VISIBLE);
        }

        // Set up the RecyclerView
        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recipeAdapter = new RecipeAdapter(
                this,
                recipes
        );

        recyclerViewRecipes.setAdapter(recipeAdapter);

        btnNavPantry.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    PantryActivity.class
            );
            startActivity(intent);
        });
        btnNavRecipes.setOnClickListener(v -> {
        });
        btnNavSettings.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    SettingsActivity.class
            );
            startActivity(intent);
        });

    }
}