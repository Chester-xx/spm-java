package com.example.spm_java.ui.pantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spm_java.R;
import com.example.spm_java.model.PantryItem;
import com.google.android.material.color.MaterialColors;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Binds pantry items to the rows of the pantry list
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    // Listener for screen actions
    public interface Listener {
        void onItemClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    // --- Attributes ---
    private final List<PantryItem> items = new ArrayList<>();
    private final Listener listener;

    // Formats decimals with trailing 0s to whole number
    private final DecimalFormat quantityFormat = new DecimalFormat("0.##", DecimalFormatSymbols.getInstance(Locale.ROOT));

    // --- Constructor ---
    public PantryAdapter(Listener listener) { this.listener = listener; }

    // --- Methods ---

    // Replaces the shown items
    public void setItems(List<PantryItem> newItems) {

        // Clear, add and notify change
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();

    }

    // View holder event creation
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Set inflation and return view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(view);

    }

    // View holder event binding
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Get item position in list
        PantryItem item = items.get(position);

        // Set name and quantity with unit symbol
        holder.name.setText(item.getName());
        holder.quantity.setText(quantityFormat.format(item.getQuantity()) + " " + item.getUnit().getSymbol());

        // No expiry line when no date was entered, expired items are shown in the error color
        if (item.getExpiryDate() == null) { holder.expiry.setVisibility(View.GONE); }
        else {

            // Check expiry
            boolean expired = item.isExpired();

            // Set label depending on expired
            int label = expired ? R.string.pantry_item_expired : R.string.pantry_item_expires;

            // Set color depending on expired
            int colorAttr = expired ? androidx.appcompat.R.attr.colorError : com.google.android.material.R.attr.colorOnSurfaceVariant;

            // Show expiry data, set text and color
            holder.expiry.setVisibility(View.VISIBLE);
            holder.expiry.setText(holder.itemView.getContext().getString(label, item.getExpiryDate().toString()));
            holder.expiry.setTextColor(MaterialColors.getColor(holder.expiry, colorAttr));

        }

        // Add event listeners to on click and on delete
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.delete.setOnClickListener(v -> listener.onDeleteClick(item));

    }

    // Returns the amount of items in the pantry
    @Override
    public int getItemCount() { return items.size(); }

    // --- View holder ---
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // --- Attributes ---
        final TextView name;
        final TextView quantity;
        final TextView expiry;
        final ImageButton delete;

        // --- Constructor ---
        ViewHolder(@NonNull View itemView) {

            super(itemView);

            // Set obj displayed text
            name = itemView.findViewById(R.id.text_name);
            quantity = itemView.findViewById(R.id.text_quantity);
            expiry = itemView.findViewById(R.id.text_expiry);
            delete = itemView.findViewById(R.id.btn_delete);

        }

    }

}