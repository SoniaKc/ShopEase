package com.example.projet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private List<PanierDisplayItem> items;
    private OnDeleteClickListener deleteClickListener;

    public PanierAdapter(List<PanierDisplayItem> items, OnDeleteClickListener deleteClickListener) {
        this.items = items;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_panier, parent, false); // Assure-toi d’avoir ce layout
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

        try {
            double total = Double.parseDouble(produit.prix) * Integer.parseInt(panier.quantite);
            holder.totalProduit.setText("Total : " + total + " $");
        } catch (NumberFormatException e) {
            holder.totalProduit.setText("Total : -");
        }

        holder.btnSupprimer.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomProduit, description, prixUnitaire, quantite, totalProduit;
        ImageView btnSupprimer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomProduit = itemView.findViewById(R.id.nomProduit);
            description = itemView.findViewById(R.id.descriptionProduit);
            prixUnitaire = itemView.findViewById(R.id.prixUnitaire);
            quantite = itemView.findViewById(R.id.quantiteProduit);
            totalProduit = itemView.findViewById(R.id.prixTotal);
            btnSupprimer = itemView.findViewById(R.id.deleteIcon);
        }
    }
}
