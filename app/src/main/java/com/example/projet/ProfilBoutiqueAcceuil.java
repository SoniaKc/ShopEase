package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projet.bdd.BoutiqueTable;

public class ProfilBoutiqueAcceuil extends Activity {
    String identifiant;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_boutique_acceuil);

        Bundle info = getIntent().getExtras();
        assert info !=null;
        this.identifiant =info.getString("id");

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
                Intent intent = new Intent(getApplicationContext(), ProfilBoutiqueInfos.class);
                intent.putExtra("log", identifiant);
                startActivity(intent);
            }
        });

        mesProduits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MesProduits.class);
                intent.putExtra("log", identifiant);
                startActivity(intent);
            }
        });

        historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Historique.class);
                intent.putExtra("log", identifiant);
                startActivity(intent);
            }
        });

        params.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Parametres.class);
                intent.putExtra("log", identifiant);
                startActivity(intent);
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FaqBoutique.class);
                intent.putExtra("log", identifiant);
                startActivity(intent);
            }
        });

        aPropos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Apropos.class);
                intent.putExtra("log", identifiant);
                startActivity(intent);
            }
        });

        mentionsLegales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MentionsLegales.class);
                intent.putExtra("log", identifiant);
                startActivity(intent);
            }
        });
    }
}
