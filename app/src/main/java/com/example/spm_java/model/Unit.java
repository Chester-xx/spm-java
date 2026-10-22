package com.example.spm_java.model;

// Measurement units localed by family grouping and base unit
public enum Unit {

    // --- Item Units | Attributes ---
    GRAM("g", Family.MASS, 1.0),
    KILOGRAM("kg", Family.MASS, 1000.0),
    MILLILITRE("ml", Family.VOLUME, 1.0),
    LITRE("l", Family.VOLUME, 1000.0),
    TEASPOON("tsp", Family.VOLUME, 5.0),
    TABLESPOON("tbsp", Family.VOLUME, 15.0),
    CUP("cup", Family.VOLUME, 250.0),
    PIECE("pcs", Family.COUNT, 1.0);

    // Related conversions only
    public enum Family { MASS, VOLUME, COUNT }

    private final String symbol;
    private final Family family;
    private final double baseValue;

    // --- Constructor ---
    Unit(String _symbol, Family _family, double _baseValue) {

        symbol = _symbol;
        family = _family;
        baseValue = _baseValue;

    }

    // --- Getters ---
    public String getSymbol() { return symbol; }
    public Family getFamily() { return family; }
    public double getBaseValue(double amount) { return amount * baseValue; }

    // --- Methods ---

    // Returns whether the unit is compatible with another unit by comparing their families
    public boolean isCompatible(Unit unit) { return family == unit.family; }

    // Returns the unit based on its compared symbol
    public static Unit getUnitFromSymbol(String symbol) throws IllegalArgumentException {

        for (Unit unit : values()) { if (unit.symbol.equals(symbol)) return unit; }
        throw new IllegalArgumentException("Invalid unit: " + symbol);

    }

    // Returns the string representation of unit, just the symbol
    @Override
    public String toString() { return symbol; }

}
