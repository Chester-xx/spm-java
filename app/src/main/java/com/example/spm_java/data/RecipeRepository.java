package com.example.spm_java.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.spm_java.model.Recipe;
import com.example.spm_java.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeRepository {

    // --- Attributes ---
    private static final String LINE_SEPARATOR = "\n";
    private final PantryDbHelper helper;

    // --- Constructor ---
    public RecipeRepository(Context context) { helper = new PantryDbHelper(context.getApplicationContext()); }

    // --- Methods ---

    // Returns All recipes and their ingredients, ordered by name
    public List<Recipe> getAll() {

        // Declare db context, ingredients specific and recipes
        SQLiteDatabase db = helper.getReadableDatabase();
        Map<Long, List<RecipeIngredient>> ingredientsByRecipe = new HashMap<>();
        List<Recipe> recipes = new ArrayList<>();

        // Get all ingredients by their recipe id
        try (Cursor cursor = db.query("recipe_ingredient", null, null, null, null, null, "id Asc")) {

            // loop through all
            while (cursor.moveToNext()) {

                // get recipe id & then get its corresponding ingredients
                long recipeId = cursor.getLong(cursor.getColumnIndexOrThrow("recipe_id"));
                ingredientsByRecipe.computeIfAbsent(recipeId, key -> new ArrayList<>()).add(ingredientFromCursor(cursor));

            }

        }

        // Get recipe and map its ingredients
        try (Cursor cursor = db.query("recipe", null, null, null, null, null, "name Collate NoCase Asc")) {

            while (cursor.moveToNext()) {

                long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                List<RecipeIngredient> ingredients = ingredientsByRecipe.getOrDefault(id, new ArrayList<>());
                recipes.add(recipeFromCursor(cursor, ingredients));

            }

        }

        // Return list of recipes and their ingredients
        return recipes;

    }

}
