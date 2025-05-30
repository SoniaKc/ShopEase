package com.example.projet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BoutiqueLigneVenteAdapter extends RecyclerView.Adapter<BoutiqueLigneVenteAdapter.ViewHolder> {

    private List<LigneVente> ligneVenteList;
    private Context context;

    public BoutiqueLigneVenteAdapter(Context context, List<LigneVente> ligneVenteList) {
        this.context = context;
        this.ligneVenteList = ligneVenteList;
    }

    @NonNull
    @Override
    public BoutiqueLigneVenteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ligne_vente, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoutiqueLigneVenteAdapter.ViewHolder holder, int position) {
        LigneVente ligne = ligneVenteList.get(position);

        holder.nomProduit.setText(ligne.nom_produit);
        holder.quantite.setText("Quantité : " + ligne.quantite);
        holder.adresse.setText("Adresse : " + ligne.nom_adresse);
        holder.paiement.setText("Paiement : " + ligne.nom_paiement);
        holder.total.setText("Total : " + ligne.total);
        holder.statut.setText("Statut : " + ligne.statut);
        holder.dateVente.setText("Date : " + ligne.date_vente);
    }

    @Override
    public int getItemCount() {
        return ligneVenteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomProduit, quantite, adresse, paiement, total, statut, dateVente;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomProduit = itemView.findViewById(R.id.nomProduit);
            quantite = itemView.findViewById(R.id.quantite);
            adresse = itemView.findViewById(R.id.adresse);
            paiement = itemView.findViewById(R.id.paiement);
            total = itemView.findViewById(R.id.total);
            statut = itemView.findViewById(R.id.statut);
            dateVente = itemView.findViewById(R.id.dateVente);
        }
    }
}
