package com.example.projet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavorisAdapter extends RecyclerView.Adapter<FavorisAdapter.ViewHolder> {

    public interface OnFavoriAction {
        void onDeleteClicked(Favoris favori);
        void onCartClicked(Favoris favori);
    }

    private final List<Favoris> favorisList;
    private final Context context;
    private final OnFavoriAction action;

    public FavorisAdapter(List<Favoris> favorisList, Context context, OnFavoriAction action) {
        this.favorisList = favorisList;
        this.context = context;
        this.action = action;
    }

    @NonNull
    @Override
    public FavorisAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favori, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavorisAdapter.ViewHolder holder, int position) {
        Favoris favori = favorisList.get(position);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Produit> call = apiService.getProduit(favori.login_boutique,favori.nom_produit);

        call.enqueue(new Callback<Produit>() {
            @Override
            public void onResponse(@NonNull Call<Produit> call, @NonNull Response<Produit> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Produit produit = response.body();
                    holder.nom.setText(produit.nom);
                    holder.prix.setText(produit.prix + " $");
                    ImageHandler.handleProduitImages(produit.image, holder.image);
                } else {
                    holder.nom.setText("Produit inconnu");
                    holder.prix.setText("-");
                    ImageHandler.handleProduitImages(null, holder.image);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Produit> call, @NonNull Throwable t) {
                holder.nom.setText("Erreur de chargement");
                holder.prix.setText("-");
                Toast.makeText(context, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });

        holder.delete.setOnClickListener(v -> action.onDeleteClicked(favori));
        holder.cart.setOnClickListener(v -> action.onCartClicked(favori));
    }

    @Override
    public int getItemCount() {
        return favorisList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nom, prix;
        ImageView delete, cart, image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.nomProduit);
            prix = itemView.findViewById(R.id.prixProduit);
            delete = itemView.findViewById(R.id.deleteBtn);
            cart = itemView.findViewById(R.id.cartBtn);
            image = itemView.findViewById(R.id.imageProduit);
        }
    }
}
