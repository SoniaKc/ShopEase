package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientProfilAcceuil extends Activity {
    String identifiant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_profil_acceuil);


        identifiant = getIntent().getStringExtra("id");

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getClient(identifiant).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Client currentClient = response.body();

                    TextView bienvenue = findViewById(R.id.bienvenue);
                    ImageView photoProfil = findViewById(R.id.profilePhoto);
                    ImageView mainPhotoProfil = findViewById(R.id.mainProfilePhoto);

                    bienvenue.setText("Bonjour, " + currentClient.prenom + " " + currentClient.nom+ " :)");

                    ImageHandler.handleAllImages(currentClient.image, photoProfil, mainPhotoProfil);

                } else {
                    Toast.makeText(ClientProfilAcceuil.this, "Client introuvable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(ClientProfilAcceuil.this, "Erreur serveur", Toast.LENGTH_SHORT).show();
            }
        });


        LinearLayout informations = findViewById(R.id.infos);
        LinearLayout addresses = findViewById(R.id.addresses);
        LinearLayout cartesBancaires = findViewById(R.id.cartesBancaires);
        LinearLayout historique = findViewById(R.id.history);
        LinearLayout params = findViewById(R.id.btnSettings);
        LinearLayout avis = findViewById(R.id.avis);
        LinearLayout faq = findViewById(R.id.btnFaq);
        LinearLayout aPropos = findViewById(R.id.btnAbout);
        LinearLayout mentions = findViewById(R.id.btnLegal);
        Button deconnexion = findViewById(R.id.btnDeconnexion);

        informations.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClientProfilInfos.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        addresses.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClientAdresse.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        cartesBancaires.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClientCB.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        historique.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClientHistoriqueAchats.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        params.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClientParametres.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        avis.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClientAvis.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        faq.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClientFaq.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        aPropos.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClientApropos.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        mentions.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClientMentionsLegales.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        deconnexion.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

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
}