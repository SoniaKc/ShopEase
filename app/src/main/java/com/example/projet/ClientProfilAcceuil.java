package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projet.bdd.ClientTable;

public class ClientProfilAcceuil extends Activity {
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
            Intent intent = new Intent(this, ClientProfilInfos.class);
            intent.putExtra("id", identifiant);
            startActivity(intent);
        });

        addresses.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClientAdresses.class);
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
    }
}