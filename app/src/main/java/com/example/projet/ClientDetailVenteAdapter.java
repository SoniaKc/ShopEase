package com.example.projet;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
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

public class ClientDetailVenteAdapter extends RecyclerView.Adapter<ClientDetailVenteAdapter.ViewHolder> {

    private final Context context;
    private final List<ClientCommandeDetail.LigneCommande> lignes;
    private final ApiService apiService;

    public ClientDetailVenteAdapter(Context context, List<ClientCommandeDetail.LigneCommande> lignes) {
        this.context = context;
        this.lignes = lignes;
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    @NonNull
    @Override
    public ClientDetailVenteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ligne_vente, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientDetailVenteAdapter.ViewHolder holder, int position) {
        ClientCommandeDetail.LigneCommande ligne = lignes.get(position);

        holder.nomProduit.setText(ligne.nomProduit);
        holder.quantite.setText("Quantité : " + ligne.quantite);

        ImageHandler.getProduitAndHandleAllImages(apiService, ligne.loginBoutique, ligne.nomProduit, holder.imageProduit);


        holder.loginBoutique.removeAllViews();
        TextView boutiqueName = new TextView(context);
        boutiqueName.setText("Boutique : "+ligne.loginBoutique);
        boutiqueName.setTextSize(16);
        boutiqueName.setTextColor(ContextCompat.getColor(context, R.color.black));
        boutiqueName.setPadding(8, 8, 8, 8);
        holder.loginBoutique.addView(boutiqueName);


        holder.buttonAjouterAvis.removeAllViews();
        Button avisButton = new Button(context);
        avisButton.setText("Laisser un avis");
        avisButton.setPadding(8, 4, 8, 16);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(24);
        drawable.setColor(ContextCompat.getColor(context, R.color.alterne1));
        avisButton.setBackground(drawable);

        avisButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ClientLaisserAvis.class);
            intent.putExtra("nom_produit", ligne.nomProduit);
            intent.putExtra("login_boutique", ligne.loginBoutique);
            intent.putExtra("id", ligne.idClient);
            context.startActivity(intent);
        });

        holder.buttonAjouterAvis.addView(avisButton);
    }

    @Override
    public int getItemCount() {
        return lignes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduit;
        TextView nomProduit;
        TextView quantite;
        LinearLayout buttonAjouterAvis, loginBoutique;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduit = itemView.findViewById(R.id.imageProduit);
            nomProduit = itemView.findViewById(R.id.nomProduit);
            loginBoutique = itemView.findViewById(R.id.loginBoutique);
            quantite = itemView.findViewById(R.id.quantite);
            buttonAjouterAvis = itemView.findViewById(R.id.buttonAjouterAvis);
        }
    }
}
