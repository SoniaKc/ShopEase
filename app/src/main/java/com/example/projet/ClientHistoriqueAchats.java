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

public class ClientHistoriqueAchats extends Activity {

    String identifiant;
    RecyclerView recyclerCommandes;
    ClientCommandeEntiereAdapter adapter;
    List<CommandeEntiere> listeCommandes = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_historique_achats);

        identifiant = getIntent().getStringExtra("id");

        recyclerCommandes = findViewById(R.id.recyclerCommandes);
        recyclerCommandes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClientCommandeEntiereAdapter(this, listeCommandes);
        recyclerCommandes.setAdapter(adapter);

        chargerCommandesDepuisApi();
        setupTopBottomNavigation();
    }

    private void setupTopBottomNavigation() {
        // TOP
        ImageView navCart = findViewById(R.id.cartIcon);

        navCart.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientPanier.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        // BOTTOM
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navFavorites = findViewById(R.id.navFavorites);
        LinearLayout navProfile2 = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientAccueil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navFavorites.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientFavoris.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navProfile2.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientProfilAcceuil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });
    }



    private void chargerCommandesDepuisApi() {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<Map<String, List<LigneVente>>> call = apiService.getByClient(identifiant);
        call.enqueue(new Callback<Map<String, List<LigneVente>>>() {
            @Override
            public void onResponse(Call<Map<String, List<LigneVente>>> call, Response<Map<String, List<LigneVente>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CommandeEntiere> commandes = new ArrayList<>();
                    for (Map.Entry<String, List<LigneVente>> entry : response.body().entrySet()) {
                        CommandeEntiere commande = new CommandeEntiere();
                        commande.idTransaction = entry.getKey();
                        commande.login_boutique = new ArrayList<>();
                        commande.idClient = identifiant;
                        commande.nom_produit = new ArrayList<>();
                        commande.quantite = new ArrayList<>();

                        List<LigneVente> lignes = entry.getValue();
                        for (LigneVente ligne : lignes) {
                            commande.login_boutique.add(ligne.login_boutique);
                            commande.nom_produit.add(ligne.nom_produit);
                            commande.quantite.add(ligne.quantite);
                        }

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
                    Toast.makeText(ClientHistoriqueAchats.this, "Erreur: " + response.code() + " - " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<LigneVente>>> call, Throwable t) {
                Toast.makeText(ClientHistoriqueAchats.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
