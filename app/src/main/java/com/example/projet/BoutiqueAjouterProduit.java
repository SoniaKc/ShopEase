package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projet.bdd.BoutiqueTable;
import com.example.projet.bdd.ProduitTable;

public class BoutiqueAjouterProduit extends Activity {
    String identifiant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_ajouter_produit);

        BoutiqueTable shop = BoutiqueTable.getInstance();
        ProduitTable produits = ProduitTable.getInstance();

        Bundle info = getIntent().getExtras();
        assert info != null;
        this.identifiant = info.getString("id");

        Button valider = findViewById(R.id.btnValider);
        Button supprimer = findViewById(R.id.btnSupprimer);

        EditText nomProduit = findViewById(R.id.nomProduit);
        EditText categorieProduit = findViewById(R.id.categorieProduit);
        EditText reductionProduit = findViewById(R.id.reductionProduit);
        EditText prixProduit = findViewById(R.id.prixProduit);
        EditText descriptionProduit = findViewById(R.id.descriptionProduit);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = nomProduit.getText().toString().trim();
                String categorie = categorieProduit.getText().toString().trim();
                String reduction = reductionProduit.getText().toString().trim();
                String prix = prixProduit.getText().toString().trim();
                String description = descriptionProduit.getText().toString().trim();
                if(!nom.isEmpty()) {
                    boolean temp = produits.insertProduit(identifiant, nom, categorie, prix, description);
                    if(temp){
                        Toast.makeText(BoutiqueAjouterProduit.this, "nouveau produit mis en ligne", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), BoutiqueMesProduits.class);
                        intent.putExtra("log", identifiant);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(BoutiqueAjouterProduit.this, "échec lors de la mise en ligne du nouveau produit", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoutiqueMesProduits.class);
                intent.putExtra("log", identifiant);
                startActivity(intent);
            }
        });
    }
}
