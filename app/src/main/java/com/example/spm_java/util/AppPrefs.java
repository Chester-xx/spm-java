package com.example.spm_java.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.spm_java.model.Unit;

// User settings kept in SharedPreferences, so they survive the app being closed
public class AppPrefs {

    // --- Attributes ---
    // Items expiring within this many days are shown as "expiring soon"
    public static final int EXPIRING_SOON_DAYS = 2;

    private static final String FILE_NAME = "spm_prefs";
    private static final String KEY_EXPIRING_ALERTS = "expiring_alerts_enabled";
    private static final String KEY_DEFAULT_UNIT = "default_unit";

    private final SharedPreferences prefs;

    // --- Constructor ---
    public AppPrefs(Context context) { prefs = context.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE); }

    // --- Methods ---

    // Expiring-soon highlighting is on until the user switches it off
    public boolean isExpiringAlertsEnabled() { return prefs.getBoolean(KEY_EXPIRING_ALERTS, true); }

    public void setExpiringAlertsEnabled(boolean enabled) { prefs.edit().putBoolean(KEY_EXPIRING_ALERTS, enabled).apply(); }

    // Unit pre-selected on the add ingredient form, grams until the user picks another
    public Unit getDefaultUnit() {

        try {
            return Unit.getUnitFromSymbol(prefs.getString(KEY_DEFAULT_UNIT, Unit.GRAM.getSymbol()));
        } catch (IllegalArgumentException e) {
            // Falls back if the stored symbol is not a known unit
            return Unit.GRAM;
        }

    }

    // Sets the default user specified unit by their preference
    public void setDefaultUnit(Unit unit) { prefs.edit().putString(KEY_DEFAULT_UNIT, unit.getSymbol()).apply(); }

}