package com.example.spm_java.ui.recipes;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.spm_java.R;
import com.example.spm_java.data.RecipeRepository;
import com.example.spm_java.model.Recipe;
import com.example.spm_java.model.RecipeIngredient;
import com.example.spm_java.ui.base.BaseActivity;
import com.example.spm_java.util.IntentKeys;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

// Shows the full ingredient list and method of the recipe whose id was passed in the intent
public class RecipeDetailActivity extends BaseActivity {

    // --- Base screen ---
    @Override
    protected int getContentLayoutId() { return R.layout.activity_recipe_detail; }

    @Override
    protected int getSelectedNavItemId() { return R.id.nav_recipes; }

    // --- Lifecycle ---
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Get the recipe id sent by the suggested recipes screen
        long recipeId = getIntent().getLongExtra(IntentKeys.EXTRA_RECIPE_ID, IntentKeys.NO_ID);
        Recipe recipe = recipeId == IntentKeys.NO_ID ? null : new RecipeRepository(this).getById(recipeId);

        // Nothing to show when the id is missing or the recipe no longer exists
        if (recipe == null) {

            Toast.makeText(this, R.string.recipe_detail_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;

        }

        // Fill the three text areas
        ((TextView) findViewById(R.id.text_recipe_title)).setText(recipe.getName());
        ((TextView) findViewById(R.id.text_ingredients)).setText(buildIngredients(recipe.getIngredients()));
        ((TextView) findViewById(R.id.text_steps)).setText(buildSteps(recipe.getSteps()));

    }

    // --- Methods ---

    // One ingredient per line, e.g. "• 500 g chicken"
    private String buildIngredients(List<RecipeIngredient> ingredients) {

        // Whole numbers show without decimals: 500 not 500.0
        DecimalFormat format = new DecimalFormat("0.##", DecimalFormatSymbols.getInstance(Locale.ROOT));
        StringBuilder text = new StringBuilder();

        // Add each ingredient as its own line
        for (RecipeIngredient ingredient : ingredients) {

            if (text.length() > 0) text.append("\n");
            text.append("• ").append(format.format(ingredient.getQuantity())).append(" ").append(ingredient.getUnit().getSymbol()).append(" ").append(ingredient.getName());

        }

        // Return ingredients
        return text.toString();

    }

    // Numbered steps separated by a blank line, e.g. "1. Beat the eggs."
    private String buildSteps(List<String> steps) {

        StringBuilder text = new StringBuilder();

        // Number each step from 1
        for (int i = 0; i < steps.size(); i++) {

            if (i > 0) text.append("\n\n");
            text.append(i + 1).append(". ").append(steps.get(i));

        }

        // Return steps
        return text.toString();

    }

}