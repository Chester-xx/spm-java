package com.example.spm_java.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.spm_java.model.PantryItem;
import com.example.spm_java.model.Unit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PantryRepository {

    // --- Attributes ---
    private final PantryDbHelper helper;

    // --- Constructor ---
    public PantryRepository(Context context) { helper = new PantryDbHelper(context.getApplicationContext()); }

    // --- Methods ---

    // Inserts item into db returning id or -1 on fail
    public long insert(PantryItem item) {

        // Insert & get new id
        long id = helper.getWritableDatabase().insert("pantry_item", null, toValues(item));
        item.setId(id);

        // Return id
        return id;

    }

    // Updates item from db returning true or false whether an item was updated
    public boolean update(PantryItem item) { return helper.getWritableDatabase().update("pantry_item", toValues(item), "id = ?", new String[]{ String.valueOf(item.getId()) }) > 0; }

    // Deletes an item from db returning true or false whether an item was deleted
    public boolean delete(long id) { return helper.getWritableDatabase().delete("pantry_item", "id = ?", new String[]{ String.valueOf(id) }) > 0; }

    // Returns item or null by finding item in db
    public PantryItem getById(long id) {

        // Return null if not found, otherwise return item obj by its found id
        try (Cursor cursor = helper.getWritableDatabase().query("pantry_item", null, "id = ?", new String[]{ String.valueOf(id) }, null, null, null)) {
            return cursor.moveToFirst() ? fromCursor(cursor) : null;
        }

    }

    // Returns every item from db, ordered by name
    public List<PantryItem> getAll() {

        // Declare returning obj
        List<PantryItem> items = new ArrayList<>();

        // Query db for all items ordered by name
        try (Cursor cursor = helper.getReadableDatabase().query("pantry_item", null, null, null, null, null, "name COLLATE NOCASE ASC")) {
            while (cursor.moveToNext()) items.add(fromCursor(cursor));
        }

        // Return item
        return items;

    }

    // --- Helpers ---

    // Map values to their corresponding types (name, quantity, unit, expiry date)
    private ContentValues toValues(PantryItem item) {

        // Add values to container
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("quantity", item.getQuantity());
        values.put("unit", item.getUnit().getSymbol());

        // Check if date is null, otherwise parse date to string
        if (item.getExpiryDate() == null) values.putNull("expiry_date");
        else values.put("expiry_date", item.getExpiryDate().toString());

        // Return container
        return values;

    }

    // Read pantry item from cursor position to new obj
    private PantryItem fromCursor(Cursor cursor) throws IllegalArgumentException {

        // Get expiry to string
        String expiry = cursor.getString(cursor.getColumnIndexOrThrow("expiry_date"));

        // Return new pantry item with the cursor position values (date can be null)
        return new PantryItem(cursor.getLong(cursor.getColumnIndexOrThrow("id")), cursor.getString(cursor.getColumnIndexOrThrow("name")), cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")), Unit.getUnitFromSymbol(cursor.getString(cursor.getColumnIndexOrThrow("unit"))), expiry == null ? null : LocalDate.parse(expiry));

    }

}
