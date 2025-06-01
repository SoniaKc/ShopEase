package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.Gson;

public class ClientCommandeDetail extends Activity {

    private TextView totalCommande, nomCommande, dateCommande, statutCommande, adresseCommande, paiementCommande;
    private ApiService apiService;
    private String idTransaction, identifiant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_commande_detail);

        nomCommande = findViewById(R.id.idTransaction);
        totalCommande = findViewById(R.id.total);
        dateCommande = findViewById(R.id.dateVente);
        statutCommande = findViewById(R.id.statut);
        adresseCommande = findViewById(R.id.adresse);
        paiementCommande = findViewById(R.id.paiement);

        Gson gson = new Gson();
        String jsonCommande = getIntent().getStringExtra("commandeEntiereJson");
        ClientCommandeEntiere commande = gson.fromJson(jsonCommande, ClientCommandeEntiere.class);

        identifiant = commande.idClient;
        idTransaction = commande.idTransaction;

        nomCommande.append(idTransaction);
        totalCommande.append(commande.total);
        dateCommande.append(commande.date_vente);
        statutCommande.append(commande.statut);
        adresseCommande.append(commande.nom_adresse);
        paiementCommande.append(commande.nom_paiement);

        apiService = ApiClient.getClient().create(ApiService.class);

        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageHandler.getClientAndHandleAllImages(apiService, identifiant, photoProfil);



        List<LigneCommande> lignes = new ArrayList<>();
        for (int i = 0; i < commande.nom_produit.size(); i++) {
            lignes.add(new LigneCommande(
                    commande.login_boutique.get(i),
                    commande.nom_produit.get(i),
                    commande.quantite.get(i),
                    commande.idClient
            ));
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerCommandes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ClientDetailVenteAdapter adapter = new ClientDetailVenteAdapter(this, lignes);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

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

    public class LigneCommande {
        public String loginBoutique;
        public String nomProduit;
        public String quantite;
        public String idClient;

        public LigneCommande(String loginBoutique, String nomProduit, String quantite, String idClient) {
            this.loginBoutique = loginBoutique;
            this.nomProduit = nomProduit;
            this.quantite = quantite;
            this.idClient = identifiant;
        }
    }
}
