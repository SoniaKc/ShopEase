package com.example.projet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projet.bdd.BoutiqueTable;
import com.example.projet.bdd.ClientTable;

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
        Button btn_connexion = findViewById(R.id.Connexion);


        btn_connexion.setOnClickListener(b -> {
            String Stridentifiant = identifiant.getText().toString();
            String Strmdp = mdp.getText().toString();
            if (Objects.equals(type, "boutique")) {
                BoutiqueTable shop = BoutiqueTable.getInstance();
                boolean temp = shop.getShopExistence(Stridentifiant, Strmdp);
                if (temp) {
                    Toast.makeText(this, "Connexion réussie.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Connexion.this, ProfilBoutique.class);
                    i.putExtra("log", Stridentifiant);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Erreur lors de la connexion.", Toast.LENGTH_SHORT).show();
                }
            }

            if (Objects.equals(type, "client")) {
                ClientTable clientTable = ClientTable.getInstance();
                boolean temp = clientTable.getClientExistence(Stridentifiant, Strmdp);
                if (temp) {
                    Toast.makeText(this, "Connexion réussie.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Connexion.this, AccueilClient.class);
                    i.putExtra("log", Stridentifiant);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Erreur lors de la connexion.", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}
