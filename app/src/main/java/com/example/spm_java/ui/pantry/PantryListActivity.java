package com.example.spm_java.ui.pantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm_java.R;
import com.example.spm_java.data.PantryRepository;
import com.example.spm_java.model.PantryItem;
import com.example.spm_java.ui.base.BaseActivity;
import com.example.spm_java.util.IntentKeys;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

    // Shows every pantry item, and starts adding, editing and deleting
    public class PantryListActivity extends BaseActivity implements PantryAdapter.Listener {

        // --- Attributes ---
        private PantryRepository repository;
        private PantryAdapter adapter;
        private RecyclerView recycler;
        private TextView textEmpty;

        // --- Base screen ---
        @Override
        protected int getContentLayoutId() { return R.layout.activity_pantry_list; }

        @Override
        protected int getSelectedNavItemId() { return R.id.nav_pantry; }

        // --- Lifecycle ---
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            // Get db context + adapter
            repository = new PantryRepository(this);
            adapter = new PantryAdapter(this);

            // Get views
            recycler = findViewById(R.id.recycler_pantry);
            textEmpty = findViewById(R.id.text_empty);
            recycler.setAdapter(adapter);

            // Add event listeners to add new ingredient, and remove all expired
            findViewById(R.id.btn_add).setOnClickListener(v -> startActivity(new Intent(this, AddEditIngredientActivity.class)));
            findViewById(R.id.btn_remove_expired).setOnClickListener(v -> confirmRemoveExpired());

        }

        // Reload screen on re-arrival
        @Override
        protected void onResume() {

            super.onResume();
            loadItems();

        }

        // --- Adapter events ---

        // Open edit form on ingredient
        @Override
        public void onItemClick(PantryItem item) {

            // Set new intent and send data
            Intent intent = new Intent(this, AddEditIngredientActivity.class);
            intent.putExtra(IntentKeys.EXTRA_PANTRY_ITEM_ID, item.getId());

            // Go to activity
            startActivity(intent);

        }

        // Delete an ingredient
        @Override
        public void onDeleteClick(PantryItem item) {

            // Dialog screen to prompt user on their deletion
            new MaterialAlertDialogBuilder(this)

                // Prompt user to confirm ingredient deletion
                .setTitle(R.string.delete_title)
                .setMessage(getString(R.string.delete_message, item.getName()))
                .setNegativeButton(R.string.dialog_cancel, null)

                // If the user agrees, delete the item from the db, and display succession
                .setPositiveButton(R.string.delete_confirm, (dialog, which) -> {
                    repository.delete(item.getId());
                    Toast.makeText(this, getString(R.string.delete_done, item.getName()), Toast.LENGTH_SHORT).show();
                    loadItems();
                })

                // Show dialog screen
                .show();

        }

        // --- Methods ---

        // Shows the list, or the empty message when there are no items
        private void loadItems() {

            // Get all ingredients
            List<PantryItem> items = repository.getAll();

            // Forward to adapter for display
            adapter.setItems(items);

            // Set visibility
            textEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
            recycler.setVisibility(items.isEmpty() ? View.GONE : View.VISIBLE);

        }

        // Method to confirm the deletion of expired ingredients
        private void confirmRemoveExpired() {

            // Expired count
            int expired = 0;

            // Count amount of expired ingredients
            for (PantryItem item : repository.getAll()) { if (item.isExpired()) expired++; }

            // If there are no expired ingredients, Notify and return
            if (expired == 0) {
                Toast.makeText(this, R.string.remove_expired_none, Toast.LENGTH_SHORT).show();
                return;
            }

            // Show dialog message to user
            new MaterialAlertDialogBuilder(this)

                // set title, message and buttons
                .setTitle(R.string.remove_expired_title)
                .setMessage(getString(R.string.remove_expired_message, expired))
                .setNegativeButton(R.string.dialog_cancel, null)

                // Add event to confirmation for deletion, delete all expired
                .setPositiveButton(R.string.remove_expired_confirm, (dialog, which) -> {
                    int removed = repository.deleteExpired();
                    Toast.makeText(this, getString(R.string.remove_expired_done, removed), Toast.LENGTH_SHORT).show();
                    loadItems();
                })

                // Show dialog message
                .show();

        }

    }