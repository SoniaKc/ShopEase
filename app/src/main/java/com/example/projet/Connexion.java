package com.example.projet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class Connexion extends Activity {
    String type;
    EditText identifiant, mdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connexion);

        Bundle info = getIntent().getExtras();
        assert info != null;
        this.type = info.getString("type");

        identifiant = findViewById(R.id.identifiant);
        mdp = findViewById(R.id.mdp);

        String Stridentifiant = identifiant.getText().toString();
        String Strmdp = mdp.getText().toString();


        if(Objects.equals(type, "boutique")){
            BDDboutique shop = new BDDboutique(this);
            boolean temp = shop.getShopExistence(Stridentifiant,Strmdp);
            if (temp){
                Toast.makeText(this, "Connexion réussie.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Connexion.this, ProfilBoutique.class);
                i.putExtra("log", Stridentifiant);
                startActivity(i);
            }
            else {
                Toast.makeText(this, "Erreur lors de la connexion.", Toast.LENGTH_SHORT).show();
            }
        }

        if(Objects.equals(type, "client")){
            BDDclient bdd = new BDDclient(this);
            boolean temp = bdd.getClientExistence(Stridentifiant,Strmdp);
            if (temp){
                Toast.makeText(this, "Connexion réussie.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Connexion.this, AccueilClient.class);
                i.putExtra("log", Stridentifiant);
                startActivity(i);
            }
            else {
                Toast.makeText(this, "Erreur lors de la connexion.", Toast.LENGTH_SHORT).show();
            }
        }




    }
}
