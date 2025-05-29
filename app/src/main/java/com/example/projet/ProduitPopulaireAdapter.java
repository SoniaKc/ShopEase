package com.example.projet;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProduitPopulaireAdapter extends RecyclerView.Adapter<ProduitPopulaireAdapter.ViewHolder> {
    private List<Produit> produits;
    private Context context;
    private String identifiant;

    public ProduitPopulaireAdapter(Context context, List<Produit> produits, String identifiant) {
        this.context = context;
        this.produits = produits;
        this.identifiant = identifiant;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_produit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produit produit = produits.get(position);

        // possibilité de charger une image avec Glide si URL)
        holder.imageProduit.setImageResource(R.drawable.img1);
        holder.titreProduit.setText(produit.nom);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ClientPageProduit.class);
            intent.putExtra("id", identifiant);
            intent.putExtra("login_boutique", produit.login_boutique);
            intent.putExtra("nomProduit", produit.nom);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return produits.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduit;
        TextView titreProduit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduit = itemView.findViewById(R.id.imageProduit);
            titreProduit = itemView.findViewById(R.id.titreProduit);
        }
    }
}
