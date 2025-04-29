package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projet.bdd.ClientTable;

public class InscriptionClient extends Activity {
    private EditText identifiant, mot_de_passe, nom, prenom, email, date_naissance;
    private Button btn_inscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription_client);

        ClientTable clientTable = ClientTable.getInstance();

        identifiant = findViewById(R.id.identifiant);
        mot_de_passe = findViewById(R.id.mot_de_passe);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        email = findViewById(R.id.email);
        date_naissance = findViewById(R.id.date_naissance);
        btn_inscription = findViewById(R.id.btn_inscription);

        btn_inscription.setOnClickListener(b -> {
            String Stridentifiant = identifiant.getText().toString();
            String Strmot_de_passe = mot_de_passe.getText().toString();
            String Strnom = nom.getText().toString();
            String Strprenom = prenom.getText().toString();
            String Strdate_naissance = date_naissance.getText().toString();
            String Stremail = email.getText().toString();

            boolean temp = true;

            if (!Stridentifiant.matches("^[A-Za-z][A-Za-z0-9]{0,9}$")) {
                identifiant.setError("Le login doit commencer par une lettre et être ≤ 10 caractères");
                temp = false;
            }
            if (clientTable.getUserClientLogin(Stridentifiant)) {
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
            if (!Strprenom.matches("^[A-Za-zÀ-ÖØ-öø-ÿ -]+$")) {
                prenom.setError("Le prénom doit contenir uniquement des lettres");
                temp = false;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(Stremail).matches()) {
                email.setError("Adresse email invalide");
                temp = false;
            }

            if (temp) {
                boolean inserted = clientTable.insertUser(Stridentifiant, Strmot_de_passe, Strnom, Strprenom, Strdate_naissance, Stremail);
                if (inserted) {
                    Toast.makeText(this, "Paramètres initilisés !", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(InscriptionClient.this, Connexion.class);
                    i.putExtra("type", "client");
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Erreur lors de l'inscription.", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }
}
