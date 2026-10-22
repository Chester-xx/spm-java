package com.example.spm_java.ui.pantry;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.spm_java.R;
import com.example.spm_java.data.PantryRepository;
import com.example.spm_java.model.PantryItem;
import com.example.spm_java.model.Unit;
import com.example.spm_java.ui.base.BaseActivity;
import com.example.spm_java.util.IntentKeys;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Locale;

// Form for adding a new pantry item, or editing the one whose id was passed in the intent
public class AddEditIngredientActivity extends BaseActivity {

    // --- Attributes ---
    private PantryRepository repository;
    private PantryItem existing; // null when adding

    private TextInputLayout layoutName;
    private TextInputLayout layoutQuantity;
    private TextInputLayout layoutUnit;
    private TextInputEditText editName;
    private TextInputEditText editQuantity;
    private TextInputEditText editExpiry;
    private MaterialAutoCompleteTextView dropdownUnit;

    // --- Base screen ---
    @Override
    protected int getContentLayoutId() { return R.layout.activity_add_edit_ingredient; }

    @Override
    protected int getSelectedNavItemId() { return R.id.nav_pantry; }

    // --- Lifecycle ---
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // Init
        super.onCreate(savedInstanceState);
        repository = new PantryRepository(this);

        // Get vars
        layoutName = findViewById(R.id.layout_name);
        layoutQuantity = findViewById(R.id.layout_quantity);
        layoutUnit = findViewById(R.id.layout_unit);
        editName = findViewById(R.id.edit_name);
        editQuantity = findViewById(R.id.edit_quantity);
        editExpiry = findViewById(R.id.edit_expiry);
        dropdownUnit = findViewById(R.id.dropdown_unit);

        // Unit dropdown is filled from the fixed Unit list, grams by default
        dropdownUnit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Unit.values()));
        dropdownUnit.setText(Unit.GRAM.getSymbol(), false);

        // Get item id from intent
        long itemId = getIntent().getLongExtra(IntentKeys.EXTRA_PANTRY_ITEM_ID, IntentKeys.NO_ID);

        // If there is an id, then edit mode
        if (itemId != IntentKeys.NO_ID) {

            // Check if ingredient exists
            existing = repository.getById(itemId);

            // If not exists, throw error (access non-existing edit interface)
            if (existing == null) {

                Toast.makeText(this, R.string.error_not_found, Toast.LENGTH_SHORT).show();
                finish();
                return;

            }

            // Set title to edit
            ((TextView) findViewById(R.id.text_title)).setText(R.string.add_edit_title_edit);

            // After a rotation the form already holds the user's changes
            if (savedInstanceState == null) fillForm(existing);

        }

        // Show dtp when user clicks on edit expiry date
        editExpiry.setOnClickListener(v -> showDatePicker());

        // Add events to clear, cancel and save changes on edit
        findViewById(R.id.btn_clear_expiry).setOnClickListener(v -> editExpiry.setText(""));
        findViewById(R.id.btn_cancel).setOnClickListener(v -> finish());
        findViewById(R.id.btn_save).setOnClickListener(v -> save());

    }

    // --- Methods ---

    // Fills the edit form with the current ingredient data
    private void fillForm(PantryItem item) {

        // Get & set fields for name, quantity, unit, expiry
        editName.setText(item.getName());
        editQuantity.setText(new DecimalFormat("0.##", DecimalFormatSymbols.getInstance(Locale.ROOT)).format(item.getQuantity()));
        dropdownUnit.setText(item.getUnit().getSymbol(), false);
        editExpiry.setText(item.getExpiryDate() == null ? "" : item.getExpiryDate().toString());

    }

    // Method to allow user to pick a date for expiry
    private void showDatePicker() {

        // Declare vars
        LocalDate today = LocalDate.now();
        long todayMillis = toUtcMillis(today);
        LocalDate current = readExpiry();
        long selection = (current != null && !current.isBefore(today)) ? toUtcMillis(current) : todayMillis;

        // Build new calendar that forces date selection from today
        CalendarConstraints constraints = new CalendarConstraints.Builder().setStart(todayMillis).setValidator(DateValidatorPointForward.from(todayMillis)).build();

        // Build date picker for user use
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker().setSelection(selection).setCalendarConstraints(constraints).build();

        // Set expiry date to what user selected from date picker, formatting
        picker.addOnPositiveButtonClickListener(millis -> editExpiry.setText(Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate().toString()));

        // Show the picker after config
        picker.show(getSupportFragmentManager(), "expiry_picker");

    }

    // Standardize to UTC time for db storage
    private long toUtcMillis(LocalDate date) { return date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(); }

    // Validates every field, then creates or updates the item
    private void save() {

        // Set no error before checks
        layoutName.setError(null);
        layoutQuantity.setError(null);
        layoutUnit.setError(null);

        // Declare vars
        String name = String.valueOf(editName.getText()).trim();
        Double quantity = readQuantity();
        Unit unit = readUnit();

        // Check if name is empty, quantity is null or unit is null
        if (name.isEmpty()) layoutName.setError(getString(R.string.error_name_required));
        if (name.isEmpty() || quantity == null || unit == null) return;

        // Get expiry selected by user
        LocalDate expiry = readExpiry();
        boolean saved;

        // If null, create a new pantry item
        if (existing == null) { saved = repository.insert(new PantryItem(name, quantity, unit, expiry)) != -1; }
        else {
            // Otherwise, edit mode on existing pantry item
            existing.setName(name);
            existing.setQuantity(quantity);
            existing.setUnit(unit);
            existing.setExpiryDate(expiry);

            // Call update to db
            saved = repository.update(existing);

        }

        // Failed to save so error out
        if (!saved) {
            Toast.makeText(this, R.string.error_save_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        // Call finish method
        finish();

    }

    // Gets the user entered quantity, returns null if invalid
    private Double readQuantity() {

        // Standardize quantity format
        String text = String.valueOf(editQuantity.getText()).trim().replace(',', '.');

        try {

            // Try parse to double
            double value = Double.parseDouble(text);

            // Invalid format
            if (!Double.isFinite(value)) throw new NumberFormatException();

            // Quantity cannot be less than or equal to 0
            if (value <= 0) {
                layoutQuantity.setError(getString(R.string.error_quantity_positive));
                return null;
            }

            // Return the value
            return value;

        } catch (NumberFormatException e) {

            // Error out and return null
            layoutQuantity.setError(getString(R.string.error_quantity_invalid));
            return null;

        }

    }

    // Get unit from users selections
    private Unit readUnit() {

        // Attempt to get the unit from the drop-down
        try { return Unit.getUnitFromSymbol(String.valueOf(dropdownUnit.getText())); }

        // Catch incorrect attempt, throw error
        catch (IllegalArgumentException e) {
            layoutUnit.setError(getString(R.string.error_unit_required));
            return null;
        }

    }

    // Get expiry date, null if no date selected
    private LocalDate readExpiry() {

        String text = String.valueOf(editExpiry.getText()).trim();
        return text.isEmpty() ? null : LocalDate.parse(text);

    }

}