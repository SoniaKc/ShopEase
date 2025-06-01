package com.example.projet;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BoutiqueDetailVenteAdapter extends RecyclerView.Adapter<BoutiqueDetailVenteAdapter.ViewHolder> {

    private final Context context;
    private final List<BoutiqueCommandeDetail.LigneCommande> lignes;
    private final ApiService apiService;

    public BoutiqueDetailVenteAdapter(Context context, List<BoutiqueCommandeDetail.LigneCommande> lignes) {
        this.context = context;
        this.lignes = lignes;
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    @NonNull
    @Override
    public BoutiqueDetailVenteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ligne_vente, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoutiqueDetailVenteAdapter.ViewHolder holder, int position) {
        BoutiqueCommandeDetail.LigneCommande ligne = lignes.get(position);

        holder.nomProduit.setText(ligne.nomProduit);
        holder.quantite.setText("Quantité : " + ligne.quantite);

        ImageHandler.getProduitAndHandleAllImages(apiService, ligne.loginBoutique, ligne.nomProduit, holder.imageProduit);

    }

    @Override
    public int getItemCount() {
        return lignes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduit;
        TextView nomProduit;
        TextView quantite;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduit = itemView.findViewById(R.id.imageProduit);
            nomProduit = itemView.findViewById(R.id.nomProduit);
            quantite = itemView.findViewById(R.id.quantite);
        }
    }
}
