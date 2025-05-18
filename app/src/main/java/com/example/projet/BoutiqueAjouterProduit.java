package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projet.bdd.BoutiqueTable;

public class BoutiqueAjouterProduit extends Activity {
    String identifiant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_ajouter_produit);

        BoutiqueTable shop = BoutiqueTable.getInstance();

        identifiant = getIntent().getStringExtra("id");

        Button valider = findViewById(R.id.btnValider);
        Button supprimer = findViewById(R.id.btnSupprimer);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoutiqueMesProduits.class);
                intent.putExtra("id", identifiant);
                startActivity(intent);
            }
        });

        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoutiqueMesProduits.class);
                intent.putExtra("id", identifiant);
                startActivity(intent);
            }
        });
    }
}
