package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoutiqueHistoriqueVentes extends Activity {

    String identifiant;
    RecyclerView recyclerCommandes;
    BoutiqueLigneVenteAdapter adapter;
    List<LigneVente> listeLignes = new ArrayList<>();
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_historique_ventes);

        apiService = ApiClient.getClient().create(ApiService.class);
        identifiant = getIntent().getStringExtra("id");

        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageHandler.getBoutiqueAndHandleAllImages(apiService, identifiant, photoProfil);

        recyclerCommandes = findViewById(R.id.recyclerCommandes);
        recyclerCommandes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BoutiqueLigneVenteAdapter(this, listeLignes);
        recyclerCommandes.setAdapter(adapter);

        chargerCommandesDepuisApi();
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navVentes = findViewById(R.id.navVentes);
        LinearLayout navProfile2 = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueProfilAccueil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navVentes.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueHistoriqueVentes.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navProfile2.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueProfilInfos.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });
    }

    private void chargerCommandesDepuisApi() {
        Call<Map<String, List<LigneVente>>> call = apiService.getByBoutique(identifiant);
        call.enqueue(new Callback<Map<String, List<LigneVente>>>() {
            @Override
            public void onResponse(Call<Map<String, List<LigneVente>>> call, Response<Map<String, List<LigneVente>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LigneVente> lignes = new ArrayList<>();
                    for (Map.Entry<String, List<LigneVente>> entry : response.body().entrySet()) {
                        lignes.addAll(entry.getValue());
                    }
                    listeLignes.clear();
                    listeLignes.addAll(lignes);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(BoutiqueHistoriqueVentes.this, "Erreur: " + response.code() + " - " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<LigneVente>>> call, Throwable t) {
                Toast.makeText(BoutiqueHistoriqueVentes.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
