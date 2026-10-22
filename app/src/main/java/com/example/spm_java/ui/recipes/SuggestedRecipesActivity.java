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
import com.example.spm_java.model.Recipe;
import com.example.spm_java.ui.base.BaseActivity;
import com.example.spm_java.util.IntentKeys;

import java.util.List;

// Lists only the recipes the current pantry can make, using the strict matching rule
public class SuggestedRecipesActivity extends BaseActivity implements RecipeAdapter.Listener {

    // --- Attributes ---
    private PantryRepository pantryRepository;
    private RecipeRepository recipeRepository;
    private RecipeAdapter adapter;
    private RecyclerView recycler;
    private TextView textEmpty;

    // --- Base screen ---
    @Override
    protected int getContentLayoutId() { return R.layout.activity_suggested_recipes; }

    @Override
    protected int getSelectedNavItemId() { return R.id.nav_recipes; }

    // --- Lifecycle ---
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Set up data access and the list
        pantryRepository = new PantryRepository(this);
        recipeRepository = new RecipeRepository(this);
        adapter = new RecipeAdapter(this);

        recycler = findViewById(R.id.recycler_recipes);
        textEmpty = findViewById(R.id.text_empty);
        recycler.setAdapter(adapter);

    }

    // Matches again every time the screen is shown, as the pantry may have changed
    @Override
    protected void onResume() {

        super.onResume();
        loadSuggestions();

    }

    // --- Adapter events ---

    // Tapping a recipe opens its detail screen
    @Override
    public void onRecipeClick(Recipe recipe) {

        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(IntentKeys.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);

    }

    // --- Methods ---

    // Runs the strict match, then shows the list or the empty message
    private void loadSuggestions() {

        List<Recipe> makeable = RecipeMatcher.findMakeable(recipeRepository.getAll(), pantryRepository.getAll());
        adapter.setRecipes(makeable);

        textEmpty.setVisibility(makeable.isEmpty() ? View.VISIBLE : View.GONE);
        recycler.setVisibility(makeable.isEmpty() ? View.GONE : View.VISIBLE);

    }

}