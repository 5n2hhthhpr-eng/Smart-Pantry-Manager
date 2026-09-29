package com.example.smartpantrymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiryReminders;
    private Spinner spinnerUnits;
    private Button btnNavPantry;
    private Button btnNavRecipes;
    private Button btnNavSettings;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchExpiryReminders = findViewById(R.id.switchExpiryReminders);
        spinnerUnits = findViewById(R.id.spinnerUnits);
        btnNavPantry = findViewById(R.id.btnNavPantry);
        btnNavRecipes = findViewById(R.id.btnNavRecipes);
        btnNavSettings = findViewById(R.id.btnNavSettings);

        preferences = getSharedPreferences(
                "SmartPantrySettings",
                MODE_PRIVATE
        );

        // Preferred unit options
        String[] units = {
                "Items",
                "Grams (g)",
                "Kilograms (kg)",
                "Millilitres (ml)",
                "Litres (L)",
                "Cups"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerUnits.setAdapter(adapter);

        //Load saved settings
        boolean remindersEnabled = preferences.getBoolean(
                "expiry_reminders",
                false
        );

        int savedUnit = preferences.getInt(
                "preferred_unit",
                0
        );

        switchExpiryReminders.setChecked(remindersEnabled);
        spinnerUnits.setSelection(savedUnit);

        // Save expiry reminder setting
        switchExpiryReminders.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    preferences.edit()
                            .putBoolean("expiry_reminders", isChecked)
                            .apply();
                }
        );

        // Save preferred unit
        spinnerUnits.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            android.view.View view,
                            int position,
                            long id) {

                        preferences.edit()
                                .putInt("preferred_unit", position)
                                .apply();
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {
                    }
                }
        );

        btnNavPantry.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SettingsActivity.this,
                    PantryActivity.class
            );
            startActivity(intent);
        });
        btnNavRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SettingsActivity.this,
                    SuggestedRecipesActivity.class
            );
            startActivity(intent);
        });
        btnNavSettings.setOnClickListener(v -> {
        });
    }
}