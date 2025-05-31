package com.example.projet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Connexion extends Activity {
    String type;
    EditText identifiant, mdp;
    ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connexion);

        type = getIntent().getStringExtra("type");

        identifiant = findViewById(R.id.inputID);
        mdp = findViewById(R.id.mot_de_passe);
        Button btn_connexion = findViewById(R.id.Connexion);

        apiService = ApiClient.getClient().create(ApiService.class);


        btn_connexion.setOnClickListener(b -> {
            String Stridentifiant = identifiant.getText().toString();
            String Strmdp = mdp.getText().toString();

            if (Objects.equals(type, "boutique")) {
                apiService.getBoutique(Stridentifiant).enqueue(new Callback<Boutique>() {
                    @Override
                    public void onResponse(Call<Boutique> call, Response<Boutique> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Boutique boutique = response.body();
                            if (boutique.password.equals(Strmdp)) {
                                Intent i = new Intent(Connexion.this, BoutiqueProfilAccueil.class);
                                i.putExtra("id", boutique.login);
                                startActivity(i);
                            } else {
                                Toast.makeText(Connexion.this, "Mot de passe incorrect.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Connexion.this, "Identifiant inconnu.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boutique> call, Throwable t) {
                        Toast.makeText(Connexion.this, "Erreur réseau.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if (Objects.equals(type, "client")) {
                Log.d("id", "Type = " + Stridentifiant);
                apiService.getClient(Stridentifiant).enqueue(new Callback<Client>() {
                    @Override
                    public void onResponse(Call<Client> call, Response<Client> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Client client = response.body();
                            if (client.password.equals(Strmdp)) {
                                Intent i = new Intent(Connexion.this, ClientAccueil.class);
                                i.putExtra("id", client.login);
                                startActivity(i);
                            } else {
                                Toast.makeText(Connexion.this, "Mot de passe incorrect.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Connexion.this, "Identifiant inconnu.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Client> call, Throwable t) {
                        Toast.makeText(Connexion.this, "Erreur réseau.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
