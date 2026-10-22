package com.example.spm_java.data;

import static com.example.spm_java.model.Unit.CUP;
import static com.example.spm_java.model.Unit.GRAM;
import static com.example.spm_java.model.Unit.MILLILITRE;
import static com.example.spm_java.model.Unit.PIECE;
import static com.example.spm_java.model.Unit.TABLESPOON;
import static com.example.spm_java.model.Unit.TEASPOON;

import android.database.sqlite.SQLiteDatabase;

import com.example.spm_java.model.Recipe;
import com.example.spm_java.model.RecipeIngredient;
import com.example.spm_java.model.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Seeds the recipe collection the first time db is created
public final class DatabaseSeeder {

    // --- Constructor ---
    private DatabaseSeeder() { }

    // --- Methods ---

    // Adds all (22 recipes i think) into db
    public static void seed(SQLiteDatabase db) { for (Recipe recipe : buildRecipes()) RecipeRepository.insert(db, recipe); }

    // Returns a list of recipes, built into the declared list using constructor
    private static List<Recipe> buildRecipes() {

        List<Recipe> recipes = new ArrayList<>();

        // Create new recipe obj and add to the list
        recipes.add(recipe("Scrambled Eggs", ingredients(ing("egg", 3, PIECE), ing("butter", 15, GRAM), ing("milk", 50, MILLILITRE)), "Beat the eggs with the milk.", "Melt the butter in a pan.", "Add the eggs and stir on low heat until set."));
        recipes.add(recipe("Cheese Omelette", ingredients(ing("egg", 3, PIECE), ing("cheese", 50, GRAM), ing("butter", 15, GRAM)), "Beat the eggs.", "Melt the butter in a pan and add the eggs.", "When the base is set, add the cheese, fold over and cook for another minute."));
        recipes.add(recipe("French Toast", ingredients(ing("bread", 4, PIECE), ing("egg", 2, PIECE), ing("milk", 100, MILLILITRE), ing("butter", 15, GRAM)), "Beat the eggs and milk together in a shallow dish.", "Dip each slice of bread in the mixture.", "Fry in the butter until golden on both sides."));
        recipes.add(recipe("Pancakes", ingredients(ing("flour", 200, GRAM), ing("milk", 300, MILLILITRE), ing("egg", 2, PIECE), ing("butter", 15, GRAM), ing("sugar", 15, GRAM)), "Mix the flour, sugar, milk and eggs into a smooth batter.", "Melt some of the butter in a pan.", "Pour in a ladle of batter and cook until golden on both sides. Repeat with the rest."));
        recipes.add(recipe("Banana Pancakes", ingredients(ing("banana", 2, PIECE), ing("egg", 2, PIECE), ing("flour", 100, GRAM)), "Mash the bananas.", "Mix in the eggs and flour.", "Fry spoonfuls in a pan until golden on both sides."));
        recipes.add(recipe("Banana Oat Porridge", ingredients(ing("oats", 100, GRAM), ing("milk", 2, CUP), ing("banana", 1, PIECE), ing("honey", 1, TABLESPOON)), "Put the oats and milk in a pot.", "Heat gently, stirring, until thick.", "Top with sliced banana and honey."));
        recipes.add(recipe("Toasted Cheese", ingredients(ing("bread", 2, PIECE), ing("cheese", 60, GRAM), ing("butter", 15, GRAM)), "Butter one side of each slice of bread.", "Put the cheese between the slices, butter side out.", "Fry in a pan until golden on both sides and the cheese has melted."));
        recipes.add(recipe("Garlic Butter Pasta", ingredients(ing("pasta", 200, GRAM), ing("butter", 30, GRAM), ing("garlic", 3, PIECE), ing("cheese", 30, GRAM)), "Cook the pasta in boiling water, then drain.", "Melt the butter and fry the chopped garlic for one minute.", "Mix in the pasta and sprinkle with the cheese."));
        recipes.add(recipe("Tomato Pasta", ingredients(ing("pasta", 200, GRAM), ing("tomato", 4, PIECE), ing("onion", 1, PIECE), ing("garlic", 2, PIECE), ing("oil", 2, TABLESPOON)), "Cook the pasta in boiling water, then drain.", "Fry the chopped onion and garlic in the oil.", "Add the chopped tomatoes and cook for 15 minutes.", "Mix with the pasta."));
        recipes.add(recipe("Tuna Pasta", ingredients(ing("pasta", 200, GRAM), ing("tuna", 1, PIECE), ing("tomato", 2, PIECE), ing("onion", 1, PIECE)), "Cook the pasta in boiling water, then drain.", "Fry the chopped onion, add the chopped tomatoes and cook for 10 minutes.", "Stir in the tuna and mix with the pasta."));
        recipes.add(recipe("Mince Bolognese", ingredients(ing("mince", 500, GRAM), ing("pasta", 250, GRAM), ing("tomato", 4, PIECE), ing("onion", 1, PIECE), ing("garlic", 2, PIECE)), "Cook the pasta in boiling water, then drain.", "Fry the mince with the chopped onion and garlic until brown.", "Add the chopped tomatoes and cook for 20 minutes.", "Serve over the pasta."));
        recipes.add(recipe("Egg Fried Rice", ingredients(ing("rice", 400, GRAM), ing("egg", 2, PIECE), ing("carrot", 1, PIECE), ing("onion", 1, PIECE), ing("oil", 2, TABLESPOON)), "Cook the rice and let it cool.", "Fry the chopped onion and grated carrot in the oil.", "Push them to one side, scramble the eggs, then mix in the rice."));
        recipes.add(recipe("Chicken and Rice", ingredients(ing("chicken", 500, GRAM), ing("rice", 200, GRAM), ing("onion", 1, PIECE), ing("garlic", 2, PIECE), ing("oil", 1, TABLESPOON)), "Fry the chicken pieces in the oil until brown.", "Add the chopped onion and garlic and cook for a few minutes.", "Add the rice and twice as much water. Cover and cook until the rice is soft."));
        recipes.add(recipe("Lemon Honey Chicken", ingredients(ing("chicken", 500, GRAM), ing("lemon", 1, PIECE), ing("garlic", 2, PIECE), ing("oil", 1, TABLESPOON), ing("honey", 1, TEASPOON)), "Mix the lemon juice, honey, oil and crushed garlic.", "Coat the chicken in the mixture.", "Bake at 200 °C for 30 minutes, or until cooked through."));
        recipes.add(recipe("Vegetable Soup", ingredients(ing("carrot", 2, PIECE), ing("potato", 2, PIECE), ing("onion", 1, PIECE), ing("tomato", 2, PIECE), ing("oil", 1, TABLESPOON)), "Fry the chopped onion in the oil.", "Add the chopped carrots, potatoes and tomatoes.", "Cover with water and cook for 30 minutes until soft."));
        recipes.add(recipe("Mashed Potatoes", ingredients(ing("potato", 4, PIECE), ing("butter", 30, GRAM), ing("milk", 100, MILLILITRE)), "Peel and cut the potatoes, then boil until soft.", "Drain and mash.", "Mix in the butter and warm milk."));
        recipes.add(recipe("Baked Cheese Potatoes", ingredients(ing("potato", 4, PIECE), ing("cheese", 100, GRAM), ing("butter", 15, GRAM)), "Bake the potatoes at 200 °C for about an hour until soft.", "Cut them open and add the butter.", "Top with the cheese."));
        recipes.add(recipe("Tuna on Toast", ingredients(ing("bread", 2, PIECE), ing("tuna", 1, PIECE), ing("butter", 15, GRAM)), "Toast the bread.", "Spread with the butter.", "Top with the tuna."));
        recipes.add(recipe("Chicken Schnitzel", ingredients(ing("chicken", 500, GRAM), ing("flour", 30, GRAM), ing("egg", 2, PIECE), ing("breadcrumb", 100, GRAM), ing("oil", 4, TABLESPOON)), "Flatten the chicken pieces.", "Coat each piece in flour, then beaten egg, then breadcrumbs.", "Fry in the oil until golden and cooked through."));
        recipes.add(recipe("Rösti", ingredients(ing("potato", 4, PIECE), ing("butter", 30, GRAM)), "Grate the potatoes and squeeze out the water.", "Melt the butter in a pan.", "Press the potato into the pan and fry until crisp, then flip and fry the other side."));
        recipes.add(recipe("Sauerbraten", ingredients(ing("beef", 800, GRAM), ing("vinegar", 250, MILLILITRE), ing("onion", 2, PIECE), ing("carrot", 2, PIECE), ing("sugar", 15, GRAM), ing("flour", 20, GRAM), ing("oil", 2, TABLESPOON)), "Slice the onions and carrots. Put the beef, vegetables and vinegar in a bowl and leave overnight.", "Remove the beef and brown it in the oil.", "Add the marinade and cook for 2 hours until the beef is tender.", "Mix the flour and sugar with a little water and stir into the sauce until thick."));
        recipes.add(recipe("Spätzle with Bacon", ingredients(ing("flour", 300, GRAM), ing("egg", 3, PIECE), ing("milk", 100, MILLILITRE), ing("bacon", 150, GRAM), ing("onion", 1, PIECE), ing("butter", 15, GRAM)), "Mix the flour, eggs and milk into a thick batter.", "Press the batter through a colander into boiling water. Lift out the spätzle when they float.", "Fry the chopped bacon and onion in the butter.", "Mix in the spätzle."));

        // returns list to be seeded sequentially
        return recipes;

    }

    // --- Helpers ---

    // Returns new ingredient obj
    private static RecipeIngredient ing(String name, double quantity, Unit unit) { return new RecipeIngredient(name, quantity, unit); }

    // Returns an array list of ellipsis stacked ingredients
    private static List<RecipeIngredient> ingredients(RecipeIngredient... items) { return Arrays.asList(items); }

    // Returns new recipe obj with a list of ingredients and an ellipsis stack of steps
    private static Recipe recipe(String name, List<RecipeIngredient> ingredients, String... steps) { return new Recipe(name, ingredients, Arrays.asList(steps)); }

}