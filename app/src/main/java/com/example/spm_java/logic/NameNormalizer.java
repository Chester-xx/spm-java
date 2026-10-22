package com.example.spm_java.logic;

import java.util.Locale;

// Reduces an ingredient name to a comparable form
// E.g. tomatoes -> tomato
//      eggs -> egg
public class NameNormalizer {

    // --- Constructor ---
    private NameNormalizer() { }

    // --- Methods ---

    // Lower-cases, trims, collapses spaces and removes plural forms
    public static String normalize(String name) {

        if (name == null) return "";

        String result = name.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");

        // tomatoes -> tomato, potatoes -> potato
        if (result.length() > 3 && result.endsWith("oes")) return result.substring(0, result.length() - 2);

        // eggs -> egg, oats -> oat (but not words that end in "ss")
        if (result.length() > 2 && result.endsWith("s") && !result.endsWith("ss")) return result.substring(0, result.length() - 1);

        // I did not have complicated naming schemes, so there may be missing test cases
        // Return the string shortened
        return result;

    }

}
