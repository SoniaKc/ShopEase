package com.example.projet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientAccueil extends Activity {

    private String identifiant;
    private RecyclerView produitRecyclerView;

    private ProduitPopulaireAdapter adapter;
    private List<Produit> produitList = new ArrayList<>();
    private ApiService apiService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_layout_accueil);

        identifiant = getIntent().getStringExtra("id");

        produitRecyclerView = findViewById(R.id.recyclerArticlesPopulaires);
        produitRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new ProduitPopulaireAdapter(this, produitList, identifiant);
        produitRecyclerView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);
        loadProduitsPopulaires();
        setupTopBottomNavigation();
    }

    private void setupTopBottomNavigation() {
        // TOP
        ImageView navCart = findViewById(R.id.cartIcon);

        navCart.setOnClickListener(v -> {
            Intent i = new Intent(ClientAccueil.this, ClientPanier.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        // BOTTOM
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navFavorites = findViewById(R.id.navFavorites);
        LinearLayout navProfile2 = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(ClientAccueil.this, ClientAccueil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navFavorites.setOnClickListener(v -> {
            Intent i = new Intent(ClientAccueil.this, ClientFavoris.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navProfile2.setOnClickListener(v -> {
            Intent i = new Intent(ClientAccueil.this, ClientProfilAcceuil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });
    }


    private void loadProduitsPopulaires() {
        Call<List<Produit>> call = apiService.getAllProduits("boutique1");
        call.enqueue(new Callback<List<Produit>>() {
            @Override
            public void onResponse(Call<List<Produit>> call, Response<List<Produit>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    produitList.clear();
                    produitList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ClientAccueil.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Produit>> call, Throwable t) {
                Toast.makeText(ClientAccueil.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
