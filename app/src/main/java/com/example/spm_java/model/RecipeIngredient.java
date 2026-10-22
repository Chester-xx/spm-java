package com.example.spm_java.model;

public class RecipeIngredient {

    // --- Attributes ---
    private final String name;
    private final double quantity;
    private final Unit unit;

    // --- Constructor ---
    public RecipeIngredient(String _name, double _quantity, Unit _unit) {

        name = _name;
        quantity = _quantity;
        unit = _unit;

    }

    // --- Getters ---
    public String getName() { return name; }
    public double getQuantity() { return quantity; }
    public Unit getUnit() { return unit; }

}
