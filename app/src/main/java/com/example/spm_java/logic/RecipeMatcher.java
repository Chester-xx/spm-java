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

    // A recipe that is short of exactly one ingredient, kept apart from the strict matches
    public static class AlmostThere {

        // --- Attributes ---
        public final Recipe recipe;
        public final RecipeIngredient missing; // the one ingredient the pantry cannot cover
        public final boolean notEnough; // true when some is in the pantry but too little, false when none is

        // --- Constructor ---
        AlmostThere(Recipe recipe, RecipeIngredient missing, boolean notEnough) {

            this.recipe = recipe;
            this.missing = missing;
            this.notEnough = notEnough;

        }

    }

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

    // Returns the recipes that are short of exactly one ingredient, never any recipe from findMakeable
    public static List<AlmostThere> findAlmostThere(List<Recipe> recipes, List<PantryItem> pantry) {

        // Build the same available map the strict match uses
        Map<String, Map<Unit.Family, Double>> available = buildAvailable(pantry);
        List<AlmostThere> almost = new ArrayList<>();

        for (Recipe recipe : recipes) {

            // A recipe without ingredients is never suggested
            if (recipe.getIngredients().isEmpty()) continue;

            // Only recipes with exactly one uncovered ingredient are almost there
            List<RecipeIngredient> uncovered = findUncovered(recipe, available);
            if (uncovered.size() != 1) continue;

            // Some of the ingredient in the pantry means there is not enough, none means it is missing
            RecipeIngredient missing = uncovered.get(0);
            almost.add(new AlmostThere(recipe, missing, getHave(missing, available) != null));

        }

        return almost;

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

        // Cannot make recipe if it has no ingredients, otherwise every ingredient must be covered
        return !recipe.getIngredients().isEmpty() && findUncovered(recipe, available).isEmpty();

    }

    // Returns every ingredient of the recipe that the pantry cannot cover, this is the one strict rule both lists share
    private static List<RecipeIngredient> findUncovered(Recipe recipe, Map<String, Map<Unit.Family, Double>> available) {

        List<RecipeIngredient> uncovered = new ArrayList<>();

        for (RecipeIngredient ingredient : recipe.getIngredients()) {

            // What we have of this ingredient in its unit family, null if there is none
            Double have = getHave(ingredient, available);

            // Get what quantity is needed of the ingredient
            double need = ingredient.getUnit().getBaseValue(ingredient.getQuantity());

            // Not covered when there is none, or less than what is needed
            if (have == null || have < need) uncovered.add(ingredient);

        }

        return uncovered;

    }

    // Returns the pantry total for an ingredient in its unit family, different families never match
    private static Double getHave(RecipeIngredient ingredient, Map<String, Map<Unit.Family, Double>> available) {

        Map<Unit.Family, Double> byFamily = available.get(NameNormalizer.normalize(ingredient.getName()));
        return byFamily == null ? null : byFamily.get(ingredient.getUnit().getFamily());

    }

}
