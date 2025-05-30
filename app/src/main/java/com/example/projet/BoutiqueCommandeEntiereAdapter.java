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
    private List<CommandeEntiere> commandes;
    private Context context;

    public BoutiqueCommandeEntiereAdapter(Context context, List<CommandeEntiere> commandes) {
        this.context = context;
        this.commandes = commandes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_historique_commande, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommandeEntiere commande = commandes.get(position);

        holder.statutCommande.setText(commande.statut);
        holder.dateCommande.setText(commande.date_vente);

        holder.itemView.setOnClickListener(v -> {
            Gson gson = new Gson();
            String jsonCommande = gson.toJson(commande);

            Intent intent = new Intent(context, BoutiqueCommandeDetail.class);
            intent.putExtra("commandeEntiereJson", jsonCommande);
            context.startActivity(intent);

        });

        // Image à gauche → celle du premier produit (si tu veux en charger une)
        // Par exemple :
        // Glide.with(context).load(...).into(holder.imageProduit);
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

