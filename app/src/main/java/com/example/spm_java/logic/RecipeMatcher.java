package com.example.spm_java.logic;

import com.example.spm_java.model.PantryItem;
import com.example.spm_java.model.Recipe;
import com.example.spm_java.model.RecipeIngredient;
import com.example.spm_java.model.Unit;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Matches a recipe to every ingredient needed that is available in the pantry and fills its quantity requirement
public class RecipeMatcher {

    // --- Constructor ---
    private RecipeMatcher() { }

    // --- Methods ---

    // Returns the recipes the pantry can make with the current pantry stock
    public static List<Recipe> findMakeable(List<Recipe> recipes, List<PantryItem> pantry) {

        // Build an available map of pantry items by family and quantity and init a list for what can be made
        Map<String, Map<Unit.Family, Double>> available = buildAvailable(pantry);
        List<Recipe> makeable = new ArrayList<>();

        // Check what recipes can be made from the list of available ingredients
        for (Recipe recipe : recipes) { if (canMake(recipe, available)) makeable.add(recipe); }

        // Return the makable list of recipes
        return makeable;

    }

    // Total quantity per ingredient name and unit family, in base units (g, ml, pcs). Expired items ignored NB!
    // Returns a mapped list of available recipes by available ingredients
    private static Map<String, Map<Unit.Family, Double>> buildAvailable(List<PantryItem> pantry) {

        // Declare
        Map<String, Map<Unit.Family, Double>> available = new HashMap<>();

        // For each pantry item
        for (PantryItem item : pantry) {

            // Dont add expired items
            if (item.isExpired()) continue;

            // Normalize pantry name, group the families, and merge the attributes with family and base value
            String name = NameNormalizer.normalize(item.getName());
            Map<Unit.Family, Double> byFamily = available.computeIfAbsent(name, key -> new EnumMap<>(Unit.Family.class));
            byFamily.merge(item.getUnit().getFamily(), item.getUnit().getBaseValue(item.getQuantity()), Double::sum);

        }

        // Return the available merged item attributes
        return available;

    }

    // Returns true or false on whether a recipe can be made with the specified available pantry ingredients
    private static boolean canMake(Recipe recipe, Map<String, Map<Unit.Family, Double>> available) {

        // Cannot make recipe if ingredients is false
        if (recipe.getIngredients().isEmpty()) return false;

        // For each ingredient
        for (RecipeIngredient ingredient : recipe.getIngredients()) {

            // Create a mapped family group and its quantity
            Map<Unit.Family, Double> byFamily = available.get(NameNormalizer.normalize(ingredient.getName()));

            // If the family map is empty, cannot be made
            if (byFamily == null) return false;

            // Ensure that the unit family is equal
            Double have = byFamily.get(ingredient.getUnit().getFamily());

            // Total quantity that we have stored is null, cannot be made
            if (have == null) return false;

            // Get what quantity is needed of the ingredient
            double need = ingredient.getUnit().getBaseValue(ingredient.getQuantity());

            // If the needed quantity is greater than what we have stored, cannot be made
            if (have < need) return false;

        }

        // Return true that the recipe can be made
        return true;

    }

}
