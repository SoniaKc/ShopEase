package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projet.bdd.BoutiqueTable;

public class AjouterProduit extends Activity {
    String identifiant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_produit);

        BoutiqueTable shop = BoutiqueTable.getInstance();

        Bundle info = getIntent().getExtras();
        assert info != null;
        this.identifiant = info.getString("id");

        Button valider = findViewById(R.id.btnValider);
        Button supprimer = findViewById(R.id.btnSupprimer);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MesProduits.class);
                intent.putExtra("log", identifiant);
                startActivity(intent);
            }
        });

        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MesProduits.class);
                intent.putExtra("log", identifiant);
                startActivity(intent);
            }
        });
    }
}
