package com.example.spm_java.model;

import com.example.spm_java.util.IntentKeys;

import java.time.LocalDate;

// Ingredient stored by user
public class PantryItem {

    // --- Attributes ---
    private long id;
    private String name;
    private double quantity;
    private Unit unit;
    private LocalDate expiryDate;

    // --- Constructors ---

    // Saved item
    public PantryItem(long _id, String _name, double _quantity, Unit _unit, LocalDate _expiryDate) {

        id = _id;
        name = _name;
        quantity = _quantity;
        unit = _unit;
        expiryDate = _expiryDate;

    }

    // New item
    public PantryItem(String name, double quantity, Unit unit, LocalDate expiryDate) { this(IntentKeys.NO_ID, name, quantity, unit, expiryDate); }


    // --- Getters & Setters ---
    // Not sure what ill use so i added all
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    // --- Methods ---

    // Returns whether a pantry item is expired or not
    public boolean isExpired() { return expiryDate != null && expiryDate.isBefore(LocalDate.now()); }

}
