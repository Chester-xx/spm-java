package com.example.spm_java.ui.settings;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.spm_java.R;
import com.example.spm_java.model.Unit;
import com.example.spm_java.ui.base.BaseActivity;
import com.example.spm_java.util.AppPrefs;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

// Settings screen: every change is saved straight away through AppPrefs
public class SettingsActivity extends BaseActivity {

    // --- Base screen ---
    @Override
    protected int getContentLayoutId() { return R.layout.activity_settings; }

    @Override
    protected int getSelectedNavItemId() { return R.id.nav_settings; }

    // --- Lifecycle ---
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AppPrefs prefs = new AppPrefs(this);

        // Expiring-soon switch shows the saved value and saves any change
        MaterialSwitch alertsSwitch = findViewById(R.id.switch_expiring_alerts);
        alertsSwitch.setChecked(prefs.isExpiringAlertsEnabled());
        alertsSwitch.setOnCheckedChangeListener((button, checked) -> prefs.setExpiringAlertsEnabled(checked));

        // Hint under the switch states how many days count as "soon"
        ((TextView) findViewById(R.id.text_expiring_hint)).setText(getString(R.string.settings_expiring_hint, AppPrefs.EXPIRING_SOON_DAYS));

        // Default unit dropdown is filled from the fixed Unit list and shows the saved unit
        MaterialAutoCompleteTextView unitDropdown = findViewById(R.id.dropdown_default_unit);
        unitDropdown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Unit.values()));
        unitDropdown.setText(prefs.getDefaultUnit().getSymbol(), false);

        // Picking a unit from the list saves it
        unitDropdown.setOnItemClickListener((parent, view, position, id) -> prefs.setDefaultUnit((Unit) parent.getItemAtPosition(position)));

    }

}