package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.model.PantryItem;

import java.util.Calendar;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText edtIngredientName;
    private EditText edtIngredientQuantity;
    private EditText edtIngredientUnit;
    private EditText edtIngredientExpiry;

    private Button btnSaveIngredient;

    private DatabaseHelper databaseHelper;

    private int ingredientId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        edtIngredientName = findViewById(R.id.edtIngredientName);
        edtIngredientQuantity = findViewById(R.id.edtIngredientQuantity);
        edtIngredientUnit = findViewById(R.id.edtIngredientUnit);
        edtIngredientExpiry = findViewById(R.id.edtIngredientExpiry);

        btnSaveIngredient = findViewById(R.id.btnSaveIngredient);

        databaseHelper = new DatabaseHelper(this);

        // Check if this screen was opened for editing
        if (getIntent().hasExtra("ingredient_id")) {

            isEditMode = true;

            ingredientId = getIntent().getIntExtra(
                    "ingredient_id",
                    -1
            );

            loadIngredientData();
        }

        edtIngredientExpiry.setOnClickListener(v -> showDatePicker());

        btnSaveIngredient.setOnClickListener(v -> saveIngredient());
    }

    private void loadIngredientData() {

        String name = getIntent().getStringExtra("ingredient_name");

        double quantity = getIntent().getDoubleExtra(
                "ingredient_quantity",
                0
        );

        String unit = getIntent().getStringExtra("ingredient_unit");

        String expiryDate = getIntent().getStringExtra(
                "ingredient_expiry"
        );

        edtIngredientName.setText(name);
        edtIngredientQuantity.setText(formatQuantity(quantity));
        edtIngredientUnit.setText(unit);
        edtIngredientExpiry.setText(expiryDate);

        TextView title = findViewById(
                R.id.txtIngredientTitle
        );

        title.setText("Edit Ingredient");

        btnSaveIngredient.setText("Update Ingredient");
    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    String date = selectedYear + "-"
                            + String.format(
                            "%02d",
                            selectedMonth + 1
                    )
                            + "-"
                            + String.format(
                            "%02d",
                            selectedDay
                    );

                    edtIngredientExpiry.setText(date);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void saveIngredient() {

        String name = edtIngredientName
                .getText()
                .toString()
                .trim();

        String quantityText = edtIngredientQuantity
                .getText()
                .toString()
                .trim();

        String unit = edtIngredientUnit
                .getText()
                .toString()
                .trim();

        String expiryDate = edtIngredientExpiry
                .getText()
                .toString()
                .trim();

        // Validate ingredient name
        if (name.isEmpty()) {

            edtIngredientName.setError(
                    "Please enter an ingredient name"
            );

            edtIngredientName.requestFocus();
            return;
        }

        // Validate quantity
        if (quantityText.isEmpty()) {

            edtIngredientQuantity.setError(
                    "Please enter a quantity"
            );

            edtIngredientQuantity.requestFocus();
            return;
        }

        double quantity;

        try {

            quantity = Double.parseDouble(quantityText);

        } catch (NumberFormatException e) {

            edtIngredientQuantity.setError(
                    "Please enter a valid number"
            );

            edtIngredientQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {

            edtIngredientQuantity.setError(
                    "Quantity must be greater than 0"
            );

            edtIngredientQuantity.requestFocus();
            return;
        }

        // Validate unit
        if (unit.isEmpty()) {

            edtIngredientUnit.setError(
                    "Please enter a unit"
            );

            edtIngredientUnit.requestFocus();
            return;
        }

        PantryItem pantryItem = new PantryItem(
                name,
                quantity,
                unit,
                expiryDate
        );

        if (isEditMode) {

            pantryItem.setId(ingredientId);

            int result = databaseHelper.updatePantryItem(
                    pantryItem
            );

            if (result > 0) {

                Toast.makeText(
                        this,
                        "Ingredient updated successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to update ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } else {

            long result = databaseHelper.addPantryItem(
                    pantryItem
            );

            if (result != -1) {

                Toast.makeText(
                        this,
                        "Ingredient saved successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to save ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private String formatQuantity(double quantity) {

        if (quantity == (long) quantity) {
            return String.valueOf((long) quantity);
        }

        return String.valueOf(quantity);
    }
}