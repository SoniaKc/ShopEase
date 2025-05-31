package com.example.projet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PanierAdapter extends RecyclerView.Adapter<PanierAdapter.ViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(PanierDisplayItem item);
    }

    public interface OnQuantityChangeListener {
        void onQuantityChange(PanierDisplayItem item, int newQuantity);
    }

    private List<PanierDisplayItem> items;
    private OnDeleteClickListener deleteClickListener;
    private OnQuantityChangeListener quantityChangeListener;

    public PanierAdapter(List<PanierDisplayItem> items,
                         OnDeleteClickListener deleteClickListener,
                         OnQuantityChangeListener quantityChangeListener) {
        this.items = items;
        this.deleteClickListener = deleteClickListener;
        this.quantityChangeListener = quantityChangeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_panier, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PanierDisplayItem item = items.get(position);
        Produit produit = item.getProduit();
        Panier panier = item.getPanier();

        holder.nomProduit.setText(produit.nom);
        holder.description.setText(produit.description);
        holder.prixUnitaire.setText("Prix : " + produit.prix + " $");
        holder.quantite.setText("Quantité : " + panier.quantite);
        ImageHandler.handleProduitImages(produit.image, holder.image);

        try {
            String prixStr = produit.prix.replace(",", ".").trim();
            double prix = Double.parseDouble(prixStr);

            int quantiteInt = Integer.parseInt(panier.quantite.trim());

            double total = prix * quantiteInt;
            holder.totalProduit.setText("Total : " + total + " $");
        } catch (NumberFormatException e) {
            holder.totalProduit.setText("Total : -");
        }


        holder.btnSupprimer.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(item);
            }
        });

        holder.btnDecrease.setOnClickListener(v -> {
            int currentQty = Integer.parseInt(item.getPanier().quantite);
            if (currentQty > 1) {
                int newQty = currentQty - 1;
                quantityChangeListener.onQuantityChange(item, newQty);
            }
        });

        holder.btnIncrease.setOnClickListener(v -> {
            int currentQty = Integer.parseInt(item.getPanier().quantite);
            int newQty = currentQty + 1;
            quantityChangeListener.onQuantityChange(item, newQty);
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomProduit, description, prixUnitaire, quantite, totalProduit;
        ImageView btnSupprimer, image;
        Button btnDecrease, btnIncrease;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomProduit = itemView.findViewById(R.id.nomProduit);
            description = itemView.findViewById(R.id.descriptionProduit);
            prixUnitaire = itemView.findViewById(R.id.prixUnitaire);
            quantite = itemView.findViewById(R.id.quantiteProduit);
            image = itemView.findViewById(R.id.imageProduit);
            totalProduit = itemView.findViewById(R.id.prixTotal);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease =itemView.findViewById(R.id.btnIncrease);
            btnSupprimer = itemView.findViewById(R.id.deleteIcon);
        }
    }
}
