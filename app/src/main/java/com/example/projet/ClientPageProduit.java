package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientPageProduit extends Activity {

    String identifiant;
    ApiService apiService;

    String login_boutique;
    String nomProduit;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_page_produit);

        identifiant = getIntent().getStringExtra("id");
        login_boutique = getIntent().getStringExtra("login_boutique");
        nomProduit = getIntent().getStringExtra("nomProduit");
        apiService = ApiClient.getClient().create(ApiService.class);

        TextView nom = findViewById(R.id.nomProduit);
        TextView prix = findViewById(R.id.prix);
        TextView reduction = findViewById(R.id.reduction);
        TextView description = findViewById(R.id.description);
        TextView categories = findViewById(R.id.categories);

        apiService.getProduit(login_boutique,nomProduit).enqueue(new Callback<Produit>() {
            @Override
            public void onResponse(Call<Produit> call, Response<Produit> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Produit produit = response.body();

                    nom.setText(produit.nom);
                    prix.setText(produit.prix);
                    reduction.setText(produit.reduction);
                    description.setText(produit.description);
                    categories.setText(produit.categories);
                }
            }

            @Override
            public void onFailure(Call<Produit> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Erreur de chargement du produit", Toast.LENGTH_SHORT).show();
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recyclerCommentaires);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Commentaire> commentaires = new ArrayList<>();
        CommentaireAdapter adapter = new CommentaireAdapter(this, commentaires);
        recyclerView.setAdapter(adapter);



        apiService.getCommentairesByProduit(login_boutique,nomProduit).enqueue(new Callback<List<Commentaire>>() {
            @Override
            public void onResponse(Call<List<Commentaire>> call, Response<List<Commentaire>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentaires.clear();
                    commentaires.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Commentaire>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Erreur de chargement des commentaires", Toast.LENGTH_SHORT).show();
            }
        });


        // NAVIGATION
        ImageView navCart = findViewById(R.id.cartIcon);
        navCart.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientPanier.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent i = new Intent(this, ClientAccueil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        findViewById(R.id.navFavorites).setOnClickListener(v -> {
            Intent i = new Intent(this, ClientFavoris.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        findViewById(R.id.navProfile).setOnClickListener(v -> {
            Intent i = new Intent(this, ClientProfilAcceuil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

    }
}
