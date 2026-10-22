package com.example.spm_java.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.spm_java.model.Recipe;
import com.example.spm_java.model.RecipeIngredient;
import com.example.spm_java.model.Unit;

import java.util.ArrayList;
import java.util.Arrays;
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

    // Returns the recipe, or null if it does not exist
    public Recipe getById(long id) {

        // Declare db context, arguments and recipe ingredients container
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] args = {String.valueOf(id)};
        List<RecipeIngredient> ingredients = new ArrayList<>();

        // get all recipe ingredients and add to container
        try (Cursor cursor = db.query("recipe_ingredient", null, "recipe_id = ?", args, null, null, "id Asc")) {
            while (cursor.moveToNext()) ingredients.add(ingredientFromCursor(cursor));
        }

        // get recipe first occur
        try (Cursor cursor = db.query("recipe", null, "id = ?", args, null, null, null)) {
            return cursor.moveToFirst() ? recipeFromCursor(cursor, ingredients) : null;
        }

    }

    // Insert new recipe into db, transaction to maintain dual insertion
    public static long insert(SQLiteDatabase db, Recipe recipe) {

        // Init transaction
        db.beginTransaction();

        try {

            // Declare container, add name and steps to container
            ContentValues recipeValues = new ContentValues();
            recipeValues.put("name", recipe.getName());
            recipeValues.put("steps", String.join(LINE_SEPARATOR, recipe.getSteps()));

            // Insert into db returning succession id
            long recipeId = db.insert("recipe", null, recipeValues);

            //
            for (RecipeIngredient ingredient : recipe.getIngredients()) {

                // Declare and add fields to container for ingredients
                ContentValues values = new ContentValues();
                values.put("recipe_id", recipeId);
                values.put("name", ingredient.getName());
                values.put("quantity", ingredient.getQuantity());
                values.put("unit", ingredient.getUnit().getSymbol());

                // Insert ingredients into db
                db.insert("recipe_ingredient", null, values);

            }

            // Finalize success of insertion and return recipe id
            db.setTransactionSuccessful();
            return recipeId;

        // finally end transaction
        } finally { db.endTransaction(); }

    }

    // --- Helpers ---

    // Returns recipe obj at the cursor position
    private Recipe recipeFromCursor(Cursor cursor, List<RecipeIngredient> ingredients) {

        // Get steps from cursor position
        String steps = cursor.getString(cursor.getColumnIndexOrThrow("steps"));

        // Return new recipe obj with its id, name, ingredients and steps
        return new Recipe(cursor.getLong(cursor.getColumnIndexOrThrow("id")), cursor.getString(cursor.getColumnIndexOrThrow("name")), ingredients, steps.isEmpty() ? new ArrayList<>() : Arrays.asList(steps.split(LINE_SEPARATOR)));

    }

    // Returns ingredient obj at the cursor position
    private RecipeIngredient ingredientFromCursor(Cursor cursor) {

        // Returns new ingredients obj with its name, quantity and unit
        return new RecipeIngredient(cursor.getString(cursor.getColumnIndexOrThrow("name")), cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")), Unit.getUnitFromSymbol(cursor.getString(cursor.getColumnIndexOrThrow("unit"))));

    }

}
