package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InscriptionBoutique extends Activity {
    private EditText identifiant, mot_de_passe, nom, siret, forme_juridique;
    private Button btn_inscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription_boutique);

        BDDboutique bdd = new BDDboutique(this);
        bdd.db = bdd.open();

        identifiant = findViewById(R.id.identifiant);
        mot_de_passe = findViewById(R.id.mdp);
        nom = findViewById(R.id.nom);
        siret = findViewById(R.id.siret);
        forme_juridique = findViewById(R.id.forme_juridique);
        btn_inscription = findViewById(R.id.inscription);

        btn_inscription.setOnClickListener(b -> {
            String Stridentifiant = identifiant.getText().toString();
            String Strmot_de_passe = mot_de_passe.getText().toString();
            String Strnom = nom.getText().toString();
            String Strsiret = siret.getText().toString();
            String Strforme_juridique = forme_juridique.getText().toString();

            boolean temp = true;

            if (!Stridentifiant.matches("^[A-Za-z][A-Za-z0-9]{0,9}$")) {
                identifiant.setError("Le login doit commencer par une lettre et être ≤ 10 caractères");
                temp = false;
            }
            if (bdd.getUserClientLogin(Stridentifiant)) {
                identifiant.setError("identifiant déjà pris");
                temp = false;
            }
            if (Strmot_de_passe.length() != 6) {
                mot_de_passe.setError("Le mot de passe doit contenir exactement 6 caractères");
                temp = false;
            }
            if (!Strnom.matches("^[A-Za-zÀ-ÖØ-öø-ÿ -]+$")) {
                nom.setError("Le nom doit contenir uniquement des lettres");
                temp = false;
            }

            if (temp) {
                boolean inserted = bdd.insertUser(Stridentifiant, Strmot_de_passe, Strnom, Strsiret, Strforme_juridique);
                if (inserted) {
                    Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(InscriptionBoutique.this, Connexion.class);
                    i.putExtra("type", "shop");
                    startActivity(i);

                } else {
                    Toast.makeText(this, "Erreur lors de l'inscription.", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }
}
