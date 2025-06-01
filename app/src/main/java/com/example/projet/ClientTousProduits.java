package com.example.projet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientTousProduits extends Activity {

    private String identifiant;
    private RecyclerView produitRecyclerView;

    private ProduitPopulaireAdapter adapter;
    private List<Produit> produitList = new ArrayList<>();
    private ApiService apiService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_tous_produits);

        identifiant = getIntent().getStringExtra("id");

        produitRecyclerView = findViewById(R.id.recyclerTousProduits);
        produitRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new ProduitPopulaireAdapter(this, produitList, identifiant);
        produitRecyclerView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);

        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageHandler.getClientAndHandleAllImages(apiService, identifiant, photoProfil);

        String but = getIntent().getStringExtra("but");
        if (but.equals("allProduits")){
            loadAllProduits();
        }else{
            loadProduitsRecherche(but);
        }

        SearchView searchView = findViewById(R.id.barreRecherche);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String recherche) {
                Intent i = new Intent(getApplicationContext(), ClientTousProduits.class);
                i.putExtra("but",recherche);
                i.putExtra("id", identifiant);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        setupTopBottomNavigation();
    }

    private void setupTopBottomNavigation() {
        // TOP
        ImageView navCart = findViewById(R.id.cartIcon);

        navCart.setOnClickListener(v -> {
            Intent i = new Intent(ClientTousProduits.this, ClientPanier.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        // BOTTOM
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navFavorites = findViewById(R.id.navFavorites);
        LinearLayout navProfile2 = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(ClientTousProduits.this, ClientAccueil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navFavorites.setOnClickListener(v -> {
            Intent i = new Intent(ClientTousProduits.this, ClientFavoris.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navProfile2.setOnClickListener(v -> {
            Intent i = new Intent(ClientTousProduits.this, ClientProfilAcceuil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });
    }


    private void loadAllProduits() {
        Call<List<Produit>> call = apiService.getAllProduits();
        call.enqueue(new Callback<List<Produit>>() {
            @Override
            public void onResponse(Call<List<Produit>> call, Response<List<Produit>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    produitList.clear();
                    produitList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ClientTousProduits.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Produit>> call, Throwable t) {
                Toast.makeText(ClientTousProduits.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadProduitsRecherche(String recherche) {
        Call<List<Produit>> call = apiService.getProduitsRecherche(recherche);
        call.enqueue(new Callback<List<Produit>>() {
            @Override
            public void onResponse(Call<List<Produit>> call, Response<List<Produit>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    produitList.clear();
                    produitList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ClientTousProduits.this, "Aucun produit ne correspond à votre recherche", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Produit>> call, Throwable t) {
                Toast.makeText(ClientTousProduits.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
