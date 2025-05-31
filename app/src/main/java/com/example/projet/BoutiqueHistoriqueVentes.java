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
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoutiqueHistoriqueVentes extends Activity {

    String identifiant;
    RecyclerView recyclerCommandes;
    BoutiqueCommandeEntiereAdapter adapter;
    List<BoutiqueCommandeEntiere> listeCommandes = new ArrayList<>();
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
        adapter = new BoutiqueCommandeEntiereAdapter(this, listeCommandes, identifiant);
        recyclerCommandes.setAdapter(adapter);

        chargerCommandesDepuisApi();
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navVentes = findViewById(R.id.navVentes);
        LinearLayout navProfile2 = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueMesProduits.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navVentes.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueHistoriqueVentes.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navProfile2.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueProfilAccueil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });
    }

    private void chargerCommandesDepuisApi() {
        Call<Map<String, List<Vente>>> call = apiService.getByBoutique(identifiant);
        call.enqueue(new Callback<Map<String, List<Vente>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Vente>>> call, Response<Map<String, List<Vente>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BoutiqueCommandeEntiere> commandes = new ArrayList<>();
                    for (Map.Entry<String, List<Vente>> entry : response.body().entrySet()) {
                        BoutiqueCommandeEntiere commande = new BoutiqueCommandeEntiere();
                        commande.idTransaction = entry.getKey();
                        commande.nom_produit = new ArrayList<>();
                        commande.quantite = new ArrayList<>();

                        List<Vente> lignes = entry.getValue();
                        for (Vente ligne : lignes) {
                            commande.nom_produit.add(ligne.nom_produit);
                            commande.quantite.add(ligne.quantite);
                        }

                        commande.idClient = lignes.get(0).idClient;
                        commande.total = lignes.get(0).total;
                        commande.date_vente = lignes.get(0).date_vente;
                        commande.statut = lignes.get(0).statut;
                        commandes.add(commande);
                    }

                    listeCommandes.clear();
                    listeCommandes.addAll(commandes);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(BoutiqueHistoriqueVentes.this, "Erreur: " + response.code() + " - " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<Vente>>> call, Throwable t) {
                Toast.makeText(BoutiqueHistoriqueVentes.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
