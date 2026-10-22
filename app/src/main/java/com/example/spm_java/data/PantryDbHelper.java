package com.example.spm_java.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PantryDbHelper extends SQLiteOpenHelper {

    // --- Constructor ---
    public PantryDbHelper(Context context) { super(context, "spm.db", null, 1); }

    // --- Methods ---

    // Cascade delete ingredients on recipe
    @Override
    public void onConfigure(SQLiteDatabase db) {

        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);

    }

    // Initialize database when needed
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("Create Table pantry_item (id Integer Primary Key AutoIncrement, name Text Not Null, quantity Real Not Null, unit Text Not Null, expiry_date Text)");
        db.execSQL("CREATE Table recipe (id Integer Primary Key AutoIncrement, name Text Not Null, steps Text Not Null)");
        db.execSQL("Create Table recipe_ingredient (id Integer Primary Key AutoIncrement, recipe_id Integer Not Null References recipe(id) On Delete Cascade, name Text Not Null, quantity Real Not Null, unit Text Not Null)");

        // Seed data method
        DatabaseSeeder.seed(db);

    }

    // Simple reset, acceptable while the schema is still at version 1
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("Drop Table If Exists pantry_item");
        db.execSQL("Drop Table If Exists recipe");
        db.execSQL("Drop Table If Exists recipe_ingredient");
        onCreate(db);

    }


}
