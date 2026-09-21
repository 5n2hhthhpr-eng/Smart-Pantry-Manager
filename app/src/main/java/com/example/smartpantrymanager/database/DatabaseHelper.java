package com.example.smartpantrymanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.smartpantrymanager.model.PantryItem;

import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2;

    // Pantry table
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String COLUMN_PANTRY_ID = "id";
    public static final String COLUMN_PANTRY_NAME = "name";
    public static final String COLUMN_PANTRY_QUANTITY = "quantity";
    public static final String COLUMN_PANTRY_UNIT = "unit";
    public static final String COLUMN_PANTRY_EXPIRY = "expiry_date";

    // Recipe table
    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_RECIPE_INSTRUCTIONS = "instructions";

    // Recipe ingredients table
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COLUMN_RECIPE_INGREDIENT_ID = "id";
    public static final String COLUMN_RECIPE_INGREDIENT_RECIPE_ID = "recipe_id";
    public static final String COLUMN_RECIPE_INGREDIENT_NAME = "ingredient_name";
    public static final String COLUMN_RECIPE_INGREDIENT_QUANTITY = "quantity";
    public static final String COLUMN_RECIPE_INGREDIENT_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createPantryTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COLUMN_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PANTRY_NAME + " TEXT NOT NULL, " +
                COLUMN_PANTRY_QUANTITY + " REAL NOT NULL, " +
                COLUMN_PANTRY_UNIT + " TEXT NOT NULL, " +
                COLUMN_PANTRY_EXPIRY + " TEXT)";

        String createRecipesTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                COLUMN_RECIPE_INSTRUCTIONS + " TEXT NOT NULL)";

        String createRecipeIngredientsTable =
                "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                        COLUMN_RECIPE_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RECIPE_INGREDIENT_RECIPE_ID + " INTEGER NOT NULL, " +
                        COLUMN_RECIPE_INGREDIENT_NAME + " TEXT NOT NULL, " +
                        COLUMN_RECIPE_INGREDIENT_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_RECIPE_INGREDIENT_UNIT + " TEXT NOT NULL, " +
                        "FOREIGN KEY (" + COLUMN_RECIPE_INGREDIENT_RECIPE_ID +
                        ") REFERENCES " + TABLE_RECIPES + "(" + COLUMN_RECIPE_ID + "))";

        db.execSQL(createPantryTable);
        db.execSQL(createRecipesTable);
        db.execSQL(createRecipeIngredientsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);

        onCreate(db);
    }

    public long addPantryItem(PantryItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_PANTRY_NAME, item.getName());
        values.put(COLUMN_PANTRY_QUANTITY, item.getQuantity());
        values.put(COLUMN_PANTRY_UNIT, item.getUnit());
        values.put(COLUMN_PANTRY_EXPIRY, item.getExpiryDate());

        return db.insert(TABLE_PANTRY, null, values);
    }

    public ArrayList<PantryItem> getAllPantryItems() {

        ArrayList<PantryItem> pantryItems = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_PANTRY_ID + " DESC"
        );

        if (cursor.moveToFirst()) {

            do {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_PANTRY_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_PANTRY_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_PANTRY_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_PANTRY_UNIT)
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_PANTRY_EXPIRY)
                );

                PantryItem item = new PantryItem(
                        id,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

                pantryItems.add(item);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return pantryItems;
    }

    public int updatePantryItem(PantryItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_PANTRY_NAME, item.getName());
        values.put(COLUMN_PANTRY_QUANTITY, item.getQuantity());
        values.put(COLUMN_PANTRY_UNIT, item.getUnit());
        values.put(COLUMN_PANTRY_EXPIRY, item.getExpiryDate());

        return db.update(
                TABLE_PANTRY,
                values,
                COLUMN_PANTRY_ID + " = ?",
                new String[]{String.valueOf(item.getId())}
        );
    }

    public int deletePantryItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(
                TABLE_PANTRY,
                COLUMN_PANTRY_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // =========================
    // RECIPE METHODS
    // =========================

    public long addRecipe(Recipe recipe) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(
                COLUMN_RECIPE_NAME,
                recipe.getName()
        );

        values.put(
                COLUMN_RECIPE_INSTRUCTIONS,
                recipe.getInstructions()
        );

        return db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }

    public long addRecipeIngredient(
            RecipeIngredient ingredient) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(
                COLUMN_RECIPE_INGREDIENT_RECIPE_ID,
                ingredient.getRecipeId()
        );

        values.put(
                COLUMN_RECIPE_INGREDIENT_NAME,
                ingredient.getIngredientName()
        );

        values.put(
                COLUMN_RECIPE_INGREDIENT_QUANTITY,
                ingredient.getQuantity()
        );

        values.put(
                COLUMN_RECIPE_INGREDIENT_UNIT,
                ingredient.getUnit()
        );

        return db.insert(
                TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );
    }

    public ArrayList<Recipe> getAllRecipes() {

        ArrayList<Recipe> recipes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                COLUMN_RECIPE_ID + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_ID
                        )
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_NAME
                        )
                );

                String instructions = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_INSTRUCTIONS
                        )
                );

                Recipe recipe = new Recipe(
                        id,
                        name,
                        instructions
                );

                recipes.add(recipe);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return recipes;
    }

    public ArrayList<RecipeIngredient> getIngredientsForRecipe(
            int recipeId) {

        ArrayList<RecipeIngredient> ingredients =
                new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                COLUMN_RECIPE_INGREDIENT_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                COLUMN_RECIPE_INGREDIENT_ID + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_INGREDIENT_ID
                        )
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_INGREDIENT_NAME
                        )
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_INGREDIENT_QUANTITY
                        )
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_INGREDIENT_UNIT
                        )
                );

                RecipeIngredient ingredient =
                        new RecipeIngredient(
                                id,
                                recipeId,
                                name,
                                quantity,
                                unit
                        );

                ingredients.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return ingredients;
    }

    public int getRecipeCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_RECIPES,
                null
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();

        return count;
    }

    public void seedRecipes() {

        if (getRecipeCount() > 0) {
            return;
        }

        // 1. Creamy Chicken Alfredo
        Recipe recipe1 = new Recipe(
                "Creamy Chicken Alfredo",
                "Cook the fettuccine according to the package instructions."
                        + "Season and cook the chicken in a pan until golden and fully cooked. "
                        + "Add the cooked pasta and chicken and toss until the sauce coats everything."
        );

        long recipe1Id = addRecipe(recipe1);

        addRecipeIngredient(new RecipeIngredient((int) recipe1Id, "Chicken Breast", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe1Id, "Fettuccine", 250, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe1Id, "Heavy Cream", 1, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe1Id, "Parmesan Cheese", 0.5, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe1Id, "Butter", 2, "tbsp"));
        addRecipeIngredient(new RecipeIngredient((int) recipe1Id, "Garlic", 2, "cloves"));

        // 2. Spaghetti Bolognese
        Recipe recipe2 = new Recipe(
                "Spaghetti Bolognese",
                "Cook diced onion in a large pan with 2 tbsp of oil and garlic until softened. Add the beef and brown. "
                        + "Add the tomatoes and tomato paste and simmer the sauce until thickened. "
                        + "Cook the spaghetti separately, then serve it with the Bolognese sauce and Parmesan."
        );

        long recipe2Id = addRecipe(recipe2);

        addRecipeIngredient(new RecipeIngredient((int) recipe2Id, "Ground beef", 500, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe2Id, "Spaghetti", 250, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe2Id, "Tomato", 400, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe2Id, "Tomato Paste", 2, "tbsp"));
        addRecipeIngredient(new RecipeIngredient((int) recipe2Id, "Onion", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe2Id, "Garlic", 2, "cloves"));
        addRecipeIngredient(new RecipeIngredient((int) recipe2Id, "Parmesan Cheese", 0.25, "cup"));

        // 3. Chicken fried rice
        Recipe recipe3 = new Recipe(
                "Chicken Fried Rice",
                "Cook the chicken until golden brown and fully cooked. Add the onion, garlic and vegetables. "
                        + "Push everything to one side of the pan and scramble the eggs. "
                        + "Add the cooked rice and soy sauce and stir-fry everything together."
        );
        long recipe3Id = addRecipe(recipe3);

        addRecipeIngredient(new RecipeIngredient((int) recipe3Id, "Chicken Breast", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe3Id, "Rice", 2, "cups"));
        addRecipeIngredient(new RecipeIngredient((int) recipe3Id, "Egg", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe3Id, "Carrot", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe3Id, "Peas", 0.5, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe3Id, "Onion", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe3Id, "Soy Sauce", 3, "tbsp"));
        addRecipeIngredient(new RecipeIngredient((int) recipe3Id, "Garlic", 2, "cloves"));

        // 4. Butter Chicken
        Recipe recipe4 = new Recipe(
                "Butter Chicken",
                "Cook the chicken with garlic, onion and spices until browned. "
                        + "Add tomato paste and tomatoes and simmer until the chicken is cooked. "
                        + "Stir in the cream and butter and simmer gently until the sauce becomes rich and creamy. "
                        + "Serve with cooked rice."
        );

        long recipe4Id = addRecipe(recipe4);

        addRecipeIngredient(new RecipeIngredient((int) recipe4Id, "Chicken Breast", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe4Id, "Tomato", 400, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe4Id, "Tomato Paste", 2, "tbsp"));
        addRecipeIngredient(new RecipeIngredient((int) recipe4Id, "Heavy Cream", 1, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe4Id, "Butter", 3, "tbsp"));
        addRecipeIngredient(new RecipeIngredient((int) recipe4Id, "Onion", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe4Id, "Garlic", 3, "cloves"));
        addRecipeIngredient(new RecipeIngredient((int) recipe4Id, "Rice", 2, "cups"));

        // 5. Lamb Curry
        Recipe recipe5 = new Recipe(
                "Lamb Curry",
                "In oil, brown onions and ginger and garlic with whole spice. Add in spices "
                        + "and cook until fragrant. Add in lamb pieces and salt and add water and let simmer. "
                        + "Add tomato and potatoes and simmer and cook until lamb is tender and gravy is thick. Serve with rice."
        );

        long recipe5Id = addRecipe(recipe5);

        addRecipeIngredient(new RecipeIngredient((int) recipe5Id, "Lamb Pieces", 1, "kg"));
        addRecipeIngredient(new RecipeIngredient((int) recipe5Id, "Onion", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe5Id, "Ginger and Garlic", 2, "tsp"));
        addRecipeIngredient(new RecipeIngredient((int) recipe5Id, "Water", 1, "cups"));
        addRecipeIngredient(new RecipeIngredient((int) recipe5Id, "Tomato", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe5Id, "Potatoes", 4, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe5Id, "Whole spice", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe5Id, "Rice", 2, "cups"));

        // 6. Beef Lasagna
        Recipe recipe6 = new Recipe(
                "Beef Lasagna",
                "Brown the beef with onion and garlic. Add tomatoes and tomato paste "
                        + "and simmer to create the meat sauce. In separate pan, melt butter and add flour and garlic and turn into paste "
                        + "and add milk and cheese and stir until thick. Layer meat sauce in dish, lasagna sheets "
                        + " and cheese sauce and repeat one more time. Top with mozzarella and bake until golden."
        );

        long recipe6Id = addRecipe(recipe6);

        addRecipeIngredient(new RecipeIngredient((int) recipe6Id, "Ground beef", 500, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe6Id, "Lasagna sheets", 250, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe6Id, "Onion", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe6Id, "Garlic", 4, "cloves"));
        addRecipeIngredient(new RecipeIngredient((int) recipe6Id, "Tomato", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe6Id, "Tomato paste", 2, "tbsp"));
        addRecipeIngredient(new RecipeIngredient((int) recipe6Id, "Butter", 100, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe6Id, "Flour", 3, "tbsp"));
        addRecipeIngredient(new RecipeIngredient((int) recipe6Id, "Cheese", 100, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe6Id, "Mozzarella Cheese", 200, "g"));

        // 7. Garlic butter shrimp pasta

        Recipe recipe7 = new Recipe(
                "Garlic Butter Shrimp Pasta",
                "Cook the pasta until al dente. Melt the butter in a pan and cook the garlic "
                        + "until fragrant. Add the shrimp and cook until pink. Stir in cream and Parmesan, "
                        + "then add the cooked pasta and toss until coated."
        );

        long recipe7Id = addRecipe(recipe7);

        addRecipeIngredient(new RecipeIngredient((int) recipe7Id, "Shrimp", 300, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe7Id, "Pasta", 250, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe7Id, "Butter", 3, "tbsp"));
        addRecipeIngredient(new RecipeIngredient((int) recipe7Id, "Garlic", 3, "cloves"));
        addRecipeIngredient(new RecipeIngredient((int) recipe7Id, "Heavy Cream", 1, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe7Id, "Parmesan Cheese", 0.5, "cup"));

        // 8. Chicken Stir-Fry Noodles

        Recipe recipe8 = new Recipe(
                "Beef Stir-Fry Noodles",
                "Cook the noodles according to the package instructions. Stir-fry the beef "
                        + "until cooked, then add the vegetables and garlic. Add the noodles and soy sauce "
                        + "and toss everything together over high heat."
        );

        long recipe8Id = addRecipe(recipe8);

        addRecipeIngredient(new RecipeIngredient((int) recipe8Id, "Beef", 500, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe8Id, "Noodles", 250, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe8Id, "Carrot", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe8Id, "Broccoli", 250, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe8Id, "Onion", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe8Id, "Soy Sauce", 3, "tbsp"));
        addRecipeIngredient(new RecipeIngredient((int) recipe8Id, "Garlic", 2, "cloves"));

        // 9. Homemade beef burgers

        Recipe recipe9 = new Recipe(
                "Homemade Beef Burgers",
                "Season the ground beef and shape it into burger patties. Cook the patties "
                        + "in a hot pan until browned and cooked to your preferred level. "
                        + "Toast the burger buns and assemble with lettuce, tomato, onion and cheese."
        );

        long recipe9Id = addRecipe(recipe9);

        addRecipeIngredient(new RecipeIngredient((int) recipe9Id, "Ground beef", 500, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe9Id, "Burger buns", 4, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe9Id, "Cheddar Cheese", 4, "slices"));
        addRecipeIngredient(new RecipeIngredient((int) recipe9Id, "Tomato", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe9Id, "lettuce", 4, "leaves"));
        addRecipeIngredient(new RecipeIngredient((int) recipe9Id, "Onion", 1, "item"));

        // 10. Chicken tacos
        Recipe recipe10 = new Recipe(
                "Chicken tacos",
                "Season the chicken with taco seasoning and cook until browned and fully cooked. "
                        + "Warm the tortillas and fill them with chicken, lettuce, tomato, cheese and onion. "
                        + "Serve immediately with your preferred taco toppings."
        );

        long recipe10Id = addRecipe(recipe10);

        addRecipeIngredient(new RecipeIngredient((int) recipe10Id, "Chicken Breast", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe10Id, "Tortillas", 6, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe10Id, "Taco Seasoning", 2, "tbsp"));
        addRecipeIngredient(new RecipeIngredient((int) recipe10Id, "Lettuce", 4, "leaves"));
        addRecipeIngredient(new RecipeIngredient((int) recipe10Id, "Tomato", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe10Id, "Cheddar Cheese", 100, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe10Id, "Onion", 1, "item"));

        // 11. Beef Burrito Bowl
        Recipe recipe11 = new Recipe(
                "Beef Burrito Bowl",
                "Cook the rice and set aside. Brown the ground beef with onion and taco seasoning. "
                        + "Build the bowl with rice, beef, beans, corn, tomato, lettuce and cheese."
        );

        long recipe11Id = addRecipe(recipe11);

        addRecipeIngredient(new RecipeIngredient((int) recipe11Id, "Ground Beef", 400, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe11Id, "Rice", 2, "cups"));
        addRecipeIngredient(new RecipeIngredient((int) recipe11Id, "Black Beans", 1, "can"));
        addRecipeIngredient(new RecipeIngredient((int) recipe11Id, "Corn", 1, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe11Id, "Tomato", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe11Id, "Lettuce", 4, "leaves"));
        addRecipeIngredient(new RecipeIngredient((int) recipe11Id, "Cheddar Cheese", 100, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe11Id, "Taco Seasoning", 2, "tbsp"));

        // 12. Vegetable Stir-fry

        Recipe recipe12 = new Recipe(
                "Vegetable Stir-Fry",
                "Heat oil in a large pan or wok. Add the vegetables and garlic and stir-fry "
                        + "over high heat until tender but still crisp. Add soy sauce and toss well. "
                        + "Serve with cooked rice."
        );

        long recipe12Id = addRecipe(recipe12);

        addRecipeIngredient(new RecipeIngredient((int) recipe12Id, "Carrot", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe12Id, "Bell Pepper", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe12Id, "Broccoli", 200, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe12Id, "Onion", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe12Id, "Garlic", 2, "cloves"));
        addRecipeIngredient(new RecipeIngredient((int) recipe12Id, "Soy Sauce", 3, "tbsp"));
        addRecipeIngredient(new RecipeIngredient((int) recipe12Id, "Rice", 2, "cups"));

        // 13. Creamy chicken pasta

        Recipe recipe13 = new Recipe(
                "Creamy Chicken Pasta",
                "Cook the pasta until al dente. Cook the chicken with garlic until golden. "
                        + "Add cream and Parmesan and simmer until the sauce thickens. "
                        + "Toss the cooked pasta through the sauce and serve hot."
        );

        long recipe13Id = addRecipe(recipe13);

        addRecipeIngredient(new RecipeIngredient((int) recipe13Id, "Chicken Breast", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe13Id, "Pasta", 250, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe13Id, "Heavy Cream", 1, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe13Id, "Parmesan Cheese", 0.5, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe13Id, "Garlic", 2, "cloves"));
        addRecipeIngredient(new RecipeIngredient((int) recipe13Id, "Butter", 2, "tbsp"));

        // 14. Chicken Caesar Salad
        Recipe recipe14 = new Recipe(
                "Chicken Caesar Salad",
                "Season and grill the chicken until cooked through. "
                        + "Toss lettuce with Caesar dressing and Parmesan cheese. "
                        + "Top with sliced chicken and croutons before serving."
        );

        long recipe14Id = addRecipe(recipe14);

        addRecipeIngredient(new RecipeIngredient((int) recipe14Id, "Chicken Breast", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe14Id, "Lettuce", 1, "head"));
        addRecipeIngredient(new RecipeIngredient((int) recipe14Id, "Parmesan Cheese", 0.25, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe14Id, "Caesar Dressing", 0.5, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe14Id, "Croutons", 1, "cup"));

        // 15. Macaroni and Cheese

        Recipe recipe15 = new Recipe(
                "Macaroni and Cheese",
                "Cook the macaroni until al dente. Melt butter in a saucepan and whisk in flour. "
                        + "Gradually add milk while whisking until smooth. Stir in cheddar and Parmesan "
                        + "until melted, then combine with the macaroni."
        );

        long recipe15Id = addRecipe(recipe15);

        addRecipeIngredient(new RecipeIngredient((int) recipe15Id, "Macaroni", 250, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe15Id, "Cheddar Cheese", 200, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe15Id, "Parmesan Cheese", 0.5, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe15Id, "Milk", 2, "cups"));
        addRecipeIngredient(new RecipeIngredient((int) recipe15Id, "Butter", 3, "tbsp"));
        addRecipeIngredient(new RecipeIngredient((int) recipe15Id, "Flour", 2, "tbsp"));

        // 16. Beef Ramen
        Recipe recipe16 = new Recipe(
                "Beef Ramen",
                "Cook the beef strips in a hot pan until browned. Prepare the ramen noodles "
                        + "and broth according to the package instructions. Add vegetables and beef "
                        + "to the broth and simmer briefly before serving with the noodles."
        );

        long recipe16Id = addRecipe(recipe16);

        addRecipeIngredient(new RecipeIngredient((int) recipe16Id, "Beef Strips", 300, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe16Id, "Ramen Noodles", 2, "packs"));
        addRecipeIngredient(new RecipeIngredient((int) recipe16Id, "Carrot", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe16Id, "Spring onion", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe16Id, "Soy Sauce", 2, "tbsp"));
        addRecipeIngredient(new RecipeIngredient((int) recipe16Id, "Garlic", 2, "cloves"));

        // 17. Cottage Pie
        Recipe recipe17 = new Recipe(
                "Cottage Pie",
                "Brown the ground beef with onion and carrots. Add peas and gravy "
                        + "and simmer until thick. Transfer to a baking dish and cover with mashed potato. "
                        + "Bake until the top is golden."
        );

        long recipe17Id = addRecipe(recipe17);

        addRecipeIngredient(new RecipeIngredient((int) recipe17Id, "Ground Beef", 500, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe17Id, "Potato", 1, "kg"));
        addRecipeIngredient(new RecipeIngredient((int) recipe17Id, "Carrot", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe17Id, "Peas", 1, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe17Id, "Onion", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe17Id, "Garlic", 2, "cloves"));
        addRecipeIngredient(new RecipeIngredient((int) recipe17Id, "Butter", 2, "tbsp"));

        // 18. Chicken and Vegetable Soup
        Recipe recipe18 = new Recipe(
                "Chicken and Vegetable Soup",
                "Cook the chicken in a large pot with onion and garlic. Add carrots, potato "
                        + "and stock and simmer until the vegetables are tender and the chicken is cooked. "
                        + "Shred the chicken and return it to the soup before serving."
        );

        long recipe18Id = addRecipe(recipe18);

        addRecipeIngredient(new RecipeIngredient((int) recipe18Id, "Chicken Breast", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe18Id, "Carrot", 2, "items"));
        addRecipeIngredient(new RecipeIngredient((int) recipe18Id, "Potato", 500, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe18Id, "Onion", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe18Id, "Garlic", 2, "cloves"));
        addRecipeIngredient(new RecipeIngredient((int) recipe18Id, "Chicken Stock", 1, "L"));

        // 19. Homemade Pizza
        Recipe recipe19 = new Recipe(
                "Homemade Pizza",
                "Prepare or roll out the pizza dough. Spread tomato sauce over the base. "
                        + "Add mozzarella, ham, peppers and onion. Bake in a hot oven "
                        + "until the crust is golden and the cheese is melted."
        );

        long recipe19Id = addRecipe(recipe19);

        addRecipeIngredient(new RecipeIngredient((int) recipe19Id, "Pizza dough", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe19Id, "Tomato sauce", 0.5, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe19Id, "Mozzarella cheese", 200, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe19Id, "Ham", 3, "slices"));
        addRecipeIngredient(new RecipeIngredient((int) recipe19Id, "Pepper", 1, "item"));
        addRecipeIngredient(new RecipeIngredient((int) recipe19Id, "Onion", 1, "item"));

        // 20. Creamy Garlic Prawns
        Recipe recipe20 = new Recipe(
                "Creamy Garlic Prawns",
                "Season and cook the prawns until pink. Remove it from the pan and cook "
                        + "the garlic in butter. Add cream and Parmesan and simmer until the sauce thickens. "
                        + "Return the prawns to the pan and serve hot."
        );

        long recipe20Id = addRecipe(recipe20);

        addRecipeIngredient(new RecipeIngredient((int) recipe20Id, "Prawns", 500, "g"));
        addRecipeIngredient(new RecipeIngredient((int) recipe20Id, "Garlic", 4, "cloves"));
        addRecipeIngredient(new RecipeIngredient((int) recipe20Id, "Heavy cream", 1, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe20Id, "Parmesan cheese", 0.5, "cup"));
        addRecipeIngredient(new RecipeIngredient((int) recipe20Id, "Butter", 2, "tbsp"));
    }

    public void clearRecipes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPE_INGREDIENTS, null, null);
        db.delete(TABLE_RECIPES, null, null);
    }

    public ArrayList<Recipe> getMatchingRecipes() {
        ArrayList<Recipe> matchingRecipes = new ArrayList<>();

        ArrayList<PantryItem> pantryItems = getAllPantryItems();
        ArrayList<Recipe> allRecipes = getAllRecipes();

        for (Recipe recipe: allRecipes) {
            ArrayList<RecipeIngredient> recipeIngredients =
                    getIngredientsForRecipe(recipe.getId());

            boolean recipeMatches = true;

            for (RecipeIngredient requiredIngredient : recipeIngredients) {

                boolean ingredientAvailable = false;

                for (PantryItem pantryItem : pantryItems) {

                    if (ingredientsMatch(
                            pantryItem.getName(),
                            requiredIngredient.getIngredientName()
                    )) {
                        if (quantityIsEnough(
                                pantryItem,
                                requiredIngredient
                        )) {
                            ingredientAvailable = true;
                            break;
                        }
                    }
                }
                if (!ingredientAvailable) {
                    recipeMatches = false;
                    break;
                }
            }

            if (recipeMatches) {
                matchingRecipes.add(recipe);
            }
        }
        return matchingRecipes;
    }

    private boolean ingredientsMatch(String pantryName, String recipeName) {

        String pantryIngredient = normalizeIngredientName(pantryName);
        String recipeIngredient = normalizeIngredientName(recipeName);

        return pantryIngredient.equals(recipeIngredient);
    }

    private String normalizeIngredientName(String ingredientName) {

        String ingredient = ingredientName
                .trim()
                .toLowerCase();

                if (ingredient.endsWith("ies")) {
                    ingredient = ingredient.substring(
                            0,
                            ingredient.length() - 3
                    ) + "y";
                } else if (ingredient.endsWith("oes")) {
                    ingredient = ingredient.substring(
                            0,
                            ingredient.length() - 2
                    );
                } else if (ingredient.endsWith("s")
                        && !ingredient.endsWith("ss")) {
                    ingredient = ingredient.substring(
                            0,
                            ingredient.length() - 1
                    );
                }
                return ingredient;
    }

    private boolean quantityIsEnough(
            PantryItem pantryItem,
            RecipeIngredient requiredIngredient) {

        double pantryQuantity = pantryItem.getQuantity();
        double requiredQuantity = requiredIngredient.getQuantity();

        String pantryUnit = pantryItem.getUnit()
                .trim()
                .toLowerCase();

        String requiredUnit = requiredIngredient.getUnit()
                .trim()
                .toLowerCase();

        // Make singular and plural units the same
        if (pantryUnit.equals("items")) {
            pantryUnit = "item";
        }

        if (requiredUnit.equals("items")) {
            requiredUnit = "item";
        }

        if (pantryUnit.equals("cups")) {
            pantryUnit = "cup";
        }

        if (requiredUnit.equals("cups")) {
            requiredUnit = "cup";
        }

        if (pantryUnit.equals("cloves")) {
            pantryUnit = "clove";
        }

        if (requiredUnit.equals("cloves")) {
            requiredUnit = "clove";
        }

        if (pantryUnit.equals("leaves")) {
            pantryUnit = "leaf";
        }

        if (requiredUnit.equals("leaves")) {
            requiredUnit = "leaf";
        }

        if (pantryUnit.equals("slices")) {
            pantryUnit = "slice";
        }

        if (requiredUnit.equals("slices")) {
            requiredUnit = "slice";
        }

        if (pantryUnit.equals(requiredUnit)) {
            return pantryQuantity >= requiredQuantity;
        }

        // Same unit
        if (pantryUnit.equals("kg")
                && requiredUnit.equals("g")) {

            pantryQuantity = pantryQuantity * 1000;

            return pantryQuantity >= requiredQuantity;
        }

        // Convert grams to kilograms
        if (pantryUnit.equals("g")
                && requiredUnit.equals("kg")) {

            pantryQuantity = pantryQuantity / 1000;

            return pantryQuantity >= requiredQuantity;
        }

        // Convert liters to millilitres
        if (pantryUnit.equals("l")
                && requiredUnit.equals("ml")) {

            pantryQuantity = pantryQuantity * 1000;

            return pantryQuantity >= requiredQuantity;
        }

        // Convert millilitres to liters
        if (pantryUnit.equals("ml")
                && requiredUnit.equals("l")) {

            pantryQuantity = pantryQuantity / 1000;

            return pantryQuantity >= requiredQuantity;
        }
        // if units are different and cannot be converted,
        // they are treated as incompatible
        return false;
    }

}

