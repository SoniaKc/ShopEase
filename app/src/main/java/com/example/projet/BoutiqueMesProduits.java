package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoutiqueMesProduits extends Activity {
    private RecyclerView recyclerView;
    private ProduitAdapter adapter;
    private List<Produit> produitList = new ArrayList<>();
    private ApiService apiService;
    private String loginBoutique;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_mes_produits);

        loginBoutique = getIntent().getStringExtra("id");

        recyclerView = findViewById(R.id.produitRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new ProduitAdapter(this, produitList);
        recyclerView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);

        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageHandler.getBoutiqueAndHandleAllImages(apiService, loginBoutique, photoProfil);

        loadProduits();

        FloatingActionButton ajouterProduit = findViewById(R.id.btnAddProduit);
        ajouterProduit.setOnClickListener(v -> {
            Intent intent = new Intent(this, BoutiqueAjouterProduit.class);
            intent.putExtra("id", loginBoutique);
            startActivity(intent);
        });

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navVentes = findViewById(R.id.navVentes);
        LinearLayout navProfile2 = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueMesProduits.class);
            i.putExtra("id", loginBoutique);
            startActivity(i);
        });

        navVentes.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueHistoriqueVentes.class);
            i.putExtra("id", loginBoutique);
            startActivity(i);
        });

        navProfile2.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueProfilAccueil.class);
            i.putExtra("id", loginBoutique);
            startActivity(i);
        });
    }

    private void loadProduits() {
        Call<List<Produit>> call = apiService.getAllProduitsByBoutique(loginBoutique);
        call.enqueue(new Callback<List<Produit>>() {
            @Override
            public void onResponse(Call<List<Produit>> call, Response<List<Produit>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    produitList.clear();
                    produitList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(BoutiqueMesProduits.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Produit>> call, Throwable t) {
            }
        });
    }
}
