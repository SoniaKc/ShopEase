package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projet.bdd.BoutiqueTable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoutiqueInscription extends Activity {
    private EditText identifiant, mot_de_passe, nom, siret, forme_juridique, email;
    private Button btn_inscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_inscription);

        BoutiqueTable boutiqueTable = BoutiqueTable.getInstance();

        identifiant = findViewById(R.id.inputID);
        mot_de_passe = findViewById(R.id.mot_de_passe);
        nom = findViewById(R.id.nom);
        siret = findViewById(R.id.siret);
        email = findViewById(R.id.email);
        forme_juridique = findViewById(R.id.forme_juridique);
        btn_inscription = findViewById(R.id.inscription);

        btn_inscription.setOnClickListener(b -> {
            String Stridentifiant = identifiant.getText().toString();
            String Strmot_de_passe = mot_de_passe.getText().toString();
            String Strnom = nom.getText().toString();
            String Strsiret = siret.getText().toString();
            String Stremail = email.getText().toString();
            String Strforme_juridique = forme_juridique.getText().toString();

            boolean temp = true;

            if (!Stridentifiant.matches("^[A-Za-z][A-Za-z0-9]{0,9}$")) {
                identifiant.setError("Le login doit commencer par une lettre et être ≤ 10 caractères");
                temp = false;
            }
            if (boutiqueTable.getUserClientLogin(Stridentifiant)) {
                identifiant.setError("identifiant déjà pris");
                temp = false;
            }
            if (!Strnom.matches("^[A-Za-zÀ-ÖØ-öø-ÿ -]+$")) {
                nom.setError("Le nom doit contenir uniquement des lettres");
                temp = false;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(Stremail).matches()) {
                email.setError("Adresse email invalide");
                temp = false;
            }

            if (temp) {
                Boutique boutique = new Boutique();
                boutique.login = Stridentifiant;
                boutique.password = Strmot_de_passe;
                boutique.nom = Strnom;
                boutique.siret = Strsiret;
                boutique.forme_juridique = Strforme_juridique;
                boutique.email = Stremail;        // Champ requis par l’API
                boutique.telephone = "";    // Champ requis par l’API
                boutique.siege_social = "";
                boutique.pays_enregistrement = "";
                boutique.iban = "";

                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<Void> call = apiService.addBoutique(boutique);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(BoutiqueInscription.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(BoutiqueInscription.this, Connexion.class);
                            i.putExtra("type", "boutique");
                            startActivity(i);
                        } else {
                            Toast.makeText(BoutiqueInscription.this, "Erreur : identifiant peut-être déjà utilisé", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(BoutiqueInscription.this, "Erreur de connexion au serveur", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });


    }
}
