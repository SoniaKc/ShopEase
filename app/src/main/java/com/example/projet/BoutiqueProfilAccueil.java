package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projet.bdd.BoutiqueTable;

public class BoutiqueProfilAccueil extends Activity {
    String identifiant;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_profil_acceuil);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        identifiant = getIntent().getStringExtra("id");

        TextView titre = findViewById(R.id.titre);
        titre.setText(identifiant);

        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageView mainProfilePhoto = findViewById(R.id.mainProfilePhoto);
        ImageHandler.getBoutiqueAndHandleAllImages(apiService, identifiant, photoProfil, mainProfilePhoto);

        LinearLayout informations = findViewById(R.id.infos);
        LinearLayout mesProduits = findViewById(R.id.btnMyProducts);
        LinearLayout historique = findViewById(R.id.btnSalesHistory);
        LinearLayout params = findViewById(R.id.btnSettings);
        LinearLayout faq = findViewById(R.id.btnFaq);
        LinearLayout aPropos = findViewById(R.id.btnAbout);
        LinearLayout mentionsLegales = findViewById(R.id.btnLegal);
        Button deconnexion = findViewById(R.id.btnDeconnexion);


        informations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoutiqueProfilInfos.class);
                intent.putExtra("id", identifiant);
                startActivity(intent);
            }
        });

        mesProduits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoutiqueMesProduits.class);
                intent.putExtra("id", identifiant);
                startActivity(intent);
            }
        });

        historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoutiqueHistoriqueVentes.class);
                intent.putExtra("id", identifiant);
                startActivity(intent);
            }
        });

        params.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoutiqueParametres.class);
                intent.putExtra("id", identifiant);
                startActivity(intent);
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoutiqueFaq.class);
                intent.putExtra("id", identifiant);
                startActivity(intent);
            }
        });

        aPropos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoutiqueApropos.class);
                intent.putExtra("id", identifiant);
                startActivity(intent);
            }
        });

        mentionsLegales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoutiqueMentionsLegales.class);
                intent.putExtra("id", identifiant);
                startActivity(intent);
            }
        });

        deconnexion.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

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
}
