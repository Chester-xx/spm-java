package com.example.spm_java.ui.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm_java.R;
import com.example.spm_java.model.Recipe;

import java.util.ArrayList;
import java.util.List;

// Binds recipes to the rows of the recipe list
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    // Listener for screen actions
    public interface Listener {
        void onRecipeClick(Recipe recipe);
    }

    // --- Attributes ---
    private final List<Recipe> recipes = new ArrayList<>();
    private final Listener listener;

    // --- Constructor ---
    public RecipeAdapter(Listener listener) { this.listener = listener; }

    // --- Methods ---

    // Replaces the shown recipes
    public void setRecipes(List<Recipe> newRecipes) {

        // Clear, add and notify change
        recipes.clear();
        recipes.addAll(newRecipes);
        notifyDataSetChanged();

    }

    // Set layout inflation and return the view obj
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);

    }

    // View holder binding
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Get recipe, and its ingredient count
        Recipe recipe = recipes.get(position);
        int count = recipe.getIngredients().size();

        // Set the name, quantity
        holder.name.setText(recipe.getName());
        holder.summary.setText(holder.itemView.getResources().getQuantityString(R.plurals.recipe_ingredient_count, count, count));

        // Bind view to event listener
        holder.itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));

    }

    // Returns number of recipes
    @Override
    public int getItemCount() { return recipes.size(); }

    // --- View holder ---
    static class ViewHolder extends RecyclerView.ViewHolder {

        // --- Attributes ---
        final TextView name;
        final TextView summary;

        // --- Constructor ---
        ViewHolder(@NonNull View itemView) {

            // Set name and quantity
            super(itemView);
            name = itemView.findViewById(R.id.text_recipe_name);
            summary = itemView.findViewById(R.id.text_recipe_summary);

        }

    }

}