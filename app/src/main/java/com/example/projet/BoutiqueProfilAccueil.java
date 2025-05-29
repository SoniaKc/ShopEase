package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projet.bdd.BoutiqueTable;

public class BoutiqueProfilAccueil extends Activity {
    String identifiant;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_profil_acceuil);

        identifiant = getIntent().getStringExtra("id");

        BoutiqueTable shop = BoutiqueTable.getInstance();
        TextView titre = findViewById(R.id.titre);
        titre.setText(shop.getNom(identifiant));

        LinearLayout informations = findViewById(R.id.infos);
        LinearLayout mesProduits = findViewById(R.id.btnMyProducts);
        LinearLayout historique = findViewById(R.id.btnSalesHistory);
        LinearLayout params = findViewById(R.id.btnSettings);
        LinearLayout faq = findViewById(R.id.btnFaq);
        LinearLayout aPropos = findViewById(R.id.btnAbout);
        LinearLayout mentionsLegales = findViewById(R.id.btnLegal);

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
                Intent intent = new Intent(getApplicationContext(), ClientParametres.class);
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
                Intent intent = new Intent(getApplicationContext(), ClientApropos.class);
                intent.putExtra("id", identifiant);
                startActivity(intent);
            }
        });

        mentionsLegales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClientMentionsLegales.class);
                intent.putExtra("id", identifiant);
                startActivity(intent);
            }
        });
    }
}
