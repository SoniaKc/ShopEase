package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projet.bdd.ClientTable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientInscription extends Activity {
    private EditText identifiant, mot_de_passe, nom, prenom, email, date_naissance;
    private Button btn_inscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_inscription);

        identifiant = findViewById(R.id.inputID);
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

            if (Strmot_de_passe.length() < 6) {
                mot_de_passe.setError("Le mot de passe doit contenir au moins 6 caractères");
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
                Client newClient = new Client();
                newClient.login = Stridentifiant;
                newClient.password = Strmot_de_passe;
                newClient.nom = Strnom;
                newClient.prenom = Strprenom;
                newClient.email = Stremail;
                newClient.date_naissance = Strdate_naissance;
                newClient.telephone = ""; // facultatif

                Parametre params = new Parametre();
                params.login=Stridentifiant;
                params.langue = "Français";
                params.notifications="push, email";
                params.cookies = "Accepter";
                params.type = "client";


                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<Void> call2 = apiService.addParametre(params);
                call2.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call2, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ClientInscription.this, "paramètres initialisés !", Toast.LENGTH_SHORT).show();
                            Call<Void> call = apiService.addClient(newClient);

                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(ClientInscription.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(ClientInscription.this, Connexion.class);
                                        i.putExtra("type", "client");
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(ClientInscription.this, "Erreur: identifiant peut-être déjà utilisé", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(ClientInscription.this, "Échec de la connexion au serveur", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(ClientInscription.this, "Erreur : paramètrenon initialisés veuillez réitérer votre inscription", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ClientInscription.this, "Erreur de connexion au serveur", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });


    }
}
