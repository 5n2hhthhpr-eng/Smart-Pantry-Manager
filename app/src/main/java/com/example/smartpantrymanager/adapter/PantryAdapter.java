package com.example.smartpantrymanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.model.PantryItem;

import java.util.ArrayList;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private Context context;
    private ArrayList<PantryItem> pantryItems;
    private OnPantryItemActionListener listener;

    public interface OnPantryItemActionListener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    public PantryAdapter(
            Context context,
            ArrayList<PantryItem> pantryItems,
            OnPantryItemActionListener listener) {

        this.context = context;
        this.pantryItems = pantryItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_pantry, parent, false);

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position) {

        PantryItem item = pantryItems.get(position);

        holder.txtIngredientName.setText(item.getName());

        holder.txtIngredientQuantity.setText(
                "Quantity: " + formatQuantity(item.getQuantity()) + item.getUnit()
        );

        if (item.getExpiryDate() == null || item.getExpiryDate().isEmpty()) {

            holder.txtIngredientExpiry.setText(
                    "Expiry Date: Not specified"
            );

        } else {

            holder.txtIngredientExpiry.setText(
                    "Expiry Date: " + item.getExpiryDate()
            );
        }

        holder.btnEditIngredient.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(item);
            }
        });

        holder.btnDeleteIngredient.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(item);
            }
        });
    }

    private String formatQuantity(double quantity) {
        if (quantity == (long) quantity) {
            return String.valueOf((long) quantity);
        }
        return String.valueOf(quantity);
    }

    @Override
    public int getItemCount() {

        return pantryItems.size();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {

        TextView txtIngredientName;
        TextView txtIngredientQuantity;
        TextView txtIngredientExpiry;

        View btnEditIngredient;
        View btnDeleteIngredient;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtIngredientName = itemView.findViewById(R.id.txtIngredientName);
            txtIngredientQuantity = itemView.findViewById(R.id.txtIngredientQuantity);
            txtIngredientExpiry = itemView.findViewById(R.id.txtIngredientExpiry);
            btnEditIngredient = itemView.findViewById(R.id.btnEditIngredient);
            btnDeleteIngredient = itemView.findViewById(R.id.btnDeleteIngredient);
        }
    }
}
