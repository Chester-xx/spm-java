package com.example.spm_java.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm_java.R;
import com.example.spm_java.data.PantryRepository;
import com.example.spm_java.data.RecipeRepository;
import com.example.spm_java.logic.RecipeMatcher;
import com.example.spm_java.model.PantryItem;
import com.example.spm_java.model.Recipe;
import com.example.spm_java.ui.base.BaseActivity;
import com.example.spm_java.util.IntentKeys;

import java.util.ArrayList;
import java.util.List;

// Lists the recipes the current pantry can make using the strict matching rule, and below them the ones one ingredient short
public class SuggestedRecipesActivity extends BaseActivity implements RecipeAdapter.Listener {

    // --- Attributes ---
    private PantryRepository pantryRepository;
    private RecipeRepository recipeRepository;
    private RecipeAdapter adapter;
    private RecipeAdapter almostAdapter;
    private RecyclerView recycler;
    private RecyclerView recyclerAlmost;
    private TextView textEmpty;
    private TextView textAlmostHeader;

    // --- Base screen ---
    @Override
    protected int getContentLayoutId() { return R.layout.activity_suggested_recipes; }

    @Override
    protected int getSelectedNavItemId() { return R.id.nav_recipes; }

    // --- Lifecycle ---
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Set up data access and both lists
        pantryRepository = new PantryRepository(this);
        recipeRepository = new RecipeRepository(this);
        adapter = new RecipeAdapter(this);
        almostAdapter = new RecipeAdapter(this);

        recycler = findViewById(R.id.recycler_recipes);
        recyclerAlmost = findViewById(R.id.recycler_almost);
        textEmpty = findViewById(R.id.text_empty);
        textAlmostHeader = findViewById(R.id.text_almost_header);

        recycler.setAdapter(adapter);
        recyclerAlmost.setAdapter(almostAdapter);

    }

    // Matches again every time the screen is shown, as the pantry may have changed
    @Override
    protected void onResume() {

        super.onResume();
        loadSuggestions();

    }

    // --- Adapter events ---

    // Tapping a recipe in either list opens its detail screen
    @Override
    public void onRecipeClick(Recipe recipe) {

        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(IntentKeys.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);

    }

    // --- Methods ---

    // Runs the strict match and the almost there match on the same data, then shows both lists
    private void loadSuggestions() {

        List<Recipe> recipes = recipeRepository.getAll();
        List<PantryItem> pantry = pantryRepository.getAll();

        // Strict list, or the empty message when nothing can be made
        List<Recipe> makeable = RecipeMatcher.findMakeable(recipes, pantry);
        adapter.setRecipes(makeable);

        textEmpty.setVisibility(makeable.isEmpty() ? View.VISIBLE : View.GONE);
        recycler.setVisibility(makeable.isEmpty() ? View.GONE : View.VISIBLE);

        // Almost there list, each recipe with a note on what is short
        List<Recipe> almostRecipes = new ArrayList<>();
        List<String> notes = new ArrayList<>();

        for (RecipeMatcher.AlmostThere almost : RecipeMatcher.findAlmostThere(recipes, pantry)) {

            almostRecipes.add(almost.recipe);

            // Not enough when some is in the pantry, missing when there is none
            int label = almost.notEnough ? R.string.almost_not_enough : R.string.almost_missing;
            notes.add(getString(label, almost.missing.getName()));

        }

        almostAdapter.setRecipes(almostRecipes, notes);

        // The header and list only show when there is something almost there
        int visibility = almostRecipes.isEmpty() ? View.GONE : View.VISIBLE;
        textAlmostHeader.setVisibility(visibility);
        recyclerAlmost.setVisibility(visibility);

    }

}
