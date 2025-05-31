package com.example.projet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

public class BoutiqueCommandeEntiereAdapter extends RecyclerView.Adapter<BoutiqueCommandeEntiereAdapter.ViewHolder> {
    private List<BoutiqueCommandeEntiere> commandes;
    private Context context;
    private String identifiant;

    public BoutiqueCommandeEntiereAdapter(Context context, List<BoutiqueCommandeEntiere> commandes, String identifiant) {
        this.context = context;
        this.commandes = commandes;
        this.identifiant = identifiant;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_historique_commande, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BoutiqueCommandeEntiere commande = commandes.get(position);

        holder.statutCommande.setText(commande.statut);
        holder.dateCommande.setText(commande.date_vente);

        String statut = commande.statut.trim();

        if (statut.equalsIgnoreCase("En cours")) {
            holder.statutCommande.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        } else if (statut.equalsIgnoreCase("Commande Acceptée") || statut.equalsIgnoreCase("Commande Livrée")) {
            holder.statutCommande.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else if (statut.equalsIgnoreCase("Commande Refusée")) {
            holder.statutCommande.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.statutCommande.setTextColor(context.getResources().getColor(android.R.color.black));
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        ImageHandler.getProduitAndHandleAllImages(apiService, identifiant, commande.nom_produit.get(0), holder.imageProduit);

        holder.itemView.setOnClickListener(v -> {
            Gson gson = new Gson();
            String jsonCommande = gson.toJson(commande);

            Intent intent = new Intent(context, BoutiqueCommandeDetail.class);
            intent.putExtra("id",identifiant);
            intent.putExtra("commandeEntiereJson", jsonCommande);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return commandes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduit;
        TextView statutCommande;

        TextView dateCommande;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduit = itemView.findViewById(R.id.imageProduit);
            statutCommande = itemView.findViewById(R.id.statutCommande);
            dateCommande = itemView.findViewById(R.id.dateCommande);
        }
    }
}

