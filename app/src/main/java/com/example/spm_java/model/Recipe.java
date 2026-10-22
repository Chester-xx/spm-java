package com.example.spm_java.model;

import com.example.spm_java.util.IntentKeys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Recipe class that holds ingredients and cooking steps / instructions
public class Recipe {

    // --- Attributes ---
    private final long id;
    private final String name;
    private final List<RecipeIngredient> ingredients;
    private final List<String> steps;

    // --- Constructors ---

    // Saved recipe
    public Recipe(long id, String name, List<RecipeIngredient> ingredients, List<String> steps) {

        this.id = id;
        this.name = name;
        this.ingredients = Collections.unmodifiableList(new ArrayList<>(ingredients));
        this.steps = Collections.unmodifiableList(new ArrayList<>(steps));

    }

    // New recipe
    public Recipe(String name, List<RecipeIngredient> ingredients, List<String> steps) { this(IntentKeys.NO_ID, name, ingredients, steps); }

    // --- Getters ---
    public long getId() { return id; }
    public String getName() { return name; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public List<String> getSteps() { return steps; }

}
