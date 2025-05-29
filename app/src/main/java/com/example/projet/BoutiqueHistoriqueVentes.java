package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoutiqueHistoriqueVentes extends Activity {

    String identifiant;
    RecyclerView recyclerCommandes;
    CommandeEntiereAdapter adapter;
    List<CommandeEntiere> listeCommandes = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_historique_ventes);

        identifiant = getIntent().getStringExtra("id");

        recyclerCommandes = findViewById(R.id.recyclerCommandes);
        recyclerCommandes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommandeEntiereAdapter(this, listeCommandes);
        recyclerCommandes.setAdapter(adapter);

        chargerCommandesDepuisApi();



        // BOTTOM NAVIGATION BAR
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

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<Map<String, List<LigneVente>>> call = apiService.getByBoutique(identifiant);
        call.enqueue(new Callback<Map<String, List<LigneVente>>>() {
            @Override
            public void onResponse(Call<Map<String, List<LigneVente>>> call, Response<Map<String, List<LigneVente>>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Log.d("API_BODY", new Gson().toJson(response.body()));



                    List<CommandeEntiere> commandes = new ArrayList<>();
                    for (Map.Entry<String, List<LigneVente>> entry : response.body().entrySet()) {
                        CommandeEntiere commande = new CommandeEntiere();
                        commande.idTransaction = entry.getKey();
                        commande.nom_produit = new ArrayList<>();
                        commande.quantite = new ArrayList<>();

                        List<LigneVente> lignes = entry.getValue();
                        for (LigneVente ligne : lignes) {
                            commande.nom_produit.add(ligne.nom_produit);
                            commande.quantite.add(ligne.quantite);
                        }

                        commande.idClient = lignes.get(0).idClient;
                        commande.nom_adresse = lignes.get(0).nom_adresse;
                        commande.nom_paiement = lignes.get(0).nom_paiement;
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
            public void onFailure(Call<Map<String, List<LigneVente>>> call, Throwable t) {
                Toast.makeText(BoutiqueHistoriqueVentes.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
