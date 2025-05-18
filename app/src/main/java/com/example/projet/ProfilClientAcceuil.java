package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projet.bdd.ClientTable;

public class ProfilClientAcceuil extends Activity {
    String identifiant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_profil_acceuil);


        Bundle info = getIntent().getExtras();
        if (info != null) {
            this.identifiant = info.getString("id");
        }

        ClientTable client = ClientTable.getInstance();
        TextView bienvenue = findViewById(R.id.bienvenue);
        bienvenue.setText("Bonjour, " + client.getNom(identifiant)+ " :)");

        LinearLayout informations = findViewById(R.id.infos);
        LinearLayout addresses = findViewById(R.id.addresses);
        LinearLayout historique = findViewById(R.id.history);
        LinearLayout params = findViewById(R.id.btnSettings);
        LinearLayout faq = findViewById(R.id.btnFaq);
        LinearLayout aPropos = findViewById(R.id.btnAbout);
        LinearLayout mentions = findViewById(R.id.btnLegal);

        // Gestion des clics
        informations.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfilClient.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        addresses.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdressesListe.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        historique.setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoriqueAchats.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        params.setOnClickListener(v -> {
            Intent intent = new Intent(this, Parametres.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        faq.setOnClickListener(v -> {
            Intent intent = new Intent(this, FaqClient.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        aPropos.setOnClickListener(v -> {
            Intent intent = new Intent(this, Apropos.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        mentions.setOnClickListener(v -> {
            Intent intent = new Intent(this, MentionsLegales.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });
    }
}