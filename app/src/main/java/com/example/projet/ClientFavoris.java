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

public class ClientFavoris extends Activity {
    private String identifiant;
    private RecyclerView recyclerView;
    private FavorisAdapter adapter;
    private List<Favoris> favorisList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_favoris);

        identifiant = getIntent().getStringExtra("id");

        recyclerView = findViewById(R.id.recyclerFavoris);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FavorisAdapter(favorisList, this, new FavorisAdapter.OnFavoriAction() {
            @Override
            public void onDeleteClicked(Favoris favori) {
                deleteFavori(favori);
            }

            @Override
            public void onCartClicked(Favoris favori) {
                ajouterAuPanier(favori);
                deleteFavori(favori);
            }
        });

        recyclerView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);
        loadFavoris();

        setupNavigation();
    }

    private void loadFavoris() {
        apiService.getAllFavoris(identifiant).enqueue(new Callback<List<Favoris>>() {
            @Override
            public void onResponse(Call<List<Favoris>> call, Response<List<Favoris>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    favorisList.clear();
                    favorisList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ClientFavoris.this, "Aucun favori trouvé", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Favoris>> call, Throwable t) {
                Toast.makeText(ClientFavoris.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFavori(Favoris favori) {
        apiService.deleteFavori(favori.idClient, favori.login_boutique, favori.nom_produit).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                favorisList.remove(favori);
                adapter.notifyDataSetChanged();
                Toast.makeText(ClientFavoris.this, "Favori supprimé", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClientFavoris.this, "Erreur de suppression", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ajouterAuPanier(Favoris favori) {
        // Ex. appeler apiService.ajouterAuPanier si tu as une méthode API
        Toast.makeText(this, "Ajouté au panier : Produit ID " , Toast.LENGTH_SHORT).show();
    }

    private void setupNavigation() {
        ImageView navCart = findViewById(R.id.cartIcon);
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navFavorites = findViewById(R.id.navFavorites);
        LinearLayout navProfile2 = findViewById(R.id.navProfile);

        navCart.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientPanier.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientAccueil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navFavorites.setOnClickListener(v -> recreate()); // éviter nouvelle instance
        navProfile2.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientProfilAcceuil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });
    }
}
