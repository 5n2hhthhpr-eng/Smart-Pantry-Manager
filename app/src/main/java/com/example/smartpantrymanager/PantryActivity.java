package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.adapter.PantryAdapter;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.model.PantryItem;

import java.util.ArrayList;

public class PantryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantry;
    private PantryAdapter pantryAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<PantryItem> pantryItems;

    private Button btnAddIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        recyclerViewPantry = findViewById(R.id.recyclerViewPantry);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);

        databaseHelper = new DatabaseHelper(this);

        pantryItems = databaseHelper.getAllPantryItems();

        pantryAdapter = new PantryAdapter(
                this,
                pantryItems,
                new PantryAdapter.OnPantryItemActionListener() {

                    @Override
                    public void onEdit(PantryItem item) {
                        editPantryItem(item);
                    }

                    @Override
                    public void onDelete(PantryItem item) {
                        deletePantryItem(item);
                    }
                }
        );

        recyclerViewPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerViewPantry.setAdapter(pantryAdapter);

        btnAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    PantryActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });
    }

    private void editPantryItem(PantryItem item) {
        Intent intent = new Intent(
                PantryActivity.this,
                AddEditIngredientActivity.class
        );

        intent.putExtra("ingredient_id", item.getId());
        intent.putExtra("ingredient_name", item.getName());
        intent.putExtra("ingredient_quantity", item.getQuantity());
        intent.putExtra("ingredient_unit", item.getUnit());
        intent.putExtra("ingredient_expiry", item.getExpiryDate());

        startActivity(intent);
    }

    private void deletePantryItem(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage(
                        "Are you sure you want to delete "
                        + item.getName() + "?"
                )
                .setPositiveButton("Delete", (dialog, which) -> {
                    databaseHelper.deletePantryItem(item.getId());

                    pantryItems.clear();
                    pantryItems.addAll(
                            databaseHelper.getAllPantryItems()
                    );

                    pantryAdapter.notifyDataSetChanged();
                })
                        .setNegativeButton("Cancel", null)
                        .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {

            pantryItems.clear();
            pantryItems.addAll(databaseHelper.getAllPantryItems());

            if (pantryAdapter != null) {
                pantryAdapter.notifyDataSetChanged();
            }
        }
    }
}