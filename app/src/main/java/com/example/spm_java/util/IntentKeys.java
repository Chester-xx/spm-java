package com.example.spm_java.util;

public final class IntentKeys {

    // Identifier
    private static final String P = "com.example.spm_java.extra.";

    // Parse edit ID / add ID to new activity
    public static final String EXTRA_PANTRY_ITEM_ID = P + "PANTRY_ITEM_ID";

    // Parse ID for display details
    public static final String EXTRA_RECIPE_ID = P + "RECIPE_ID";

    // Default null ID presenter
    public static final long NO_ID = -1L;

    // Constructor
    private IntentKeys() { }
}
