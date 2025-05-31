package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientAdresse extends Activity {
    String identifiant;
    ApiService apiService;
    LinearLayout addressContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_adresses);

        Log.d("TESTBIDON", "Reçu " + " cartes");
        identifiant = getIntent().getStringExtra("id");
        Log.d("TESTBIDON2", "Reçu " + " cartes");
        apiService = ApiClient.getClient().create(ApiService.class);
        Log.d("TESTBIDON3", "Reçu " + " cartes");
        this.addressContainer = findViewById(R.id.address_container);
        Log.d("TESTBIDON4", "Reçu " + " cartes");


        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageHandler.getClientAndHandleAllImages(apiService, identifiant, photoProfil);


        ImageView navCart = findViewById(R.id.cartIcon);
        navCart.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientPanier.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent i = new Intent(this, ClientAccueil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        findViewById(R.id.navFavorites).setOnClickListener(v -> {
            Intent i = new Intent(this, ClientFavoris.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        findViewById(R.id.navProfile).setOnClickListener(v -> {
            Intent i = new Intent(this, ClientProfilAcceuil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        loadAdresses();

        Button addAddressBtn = findViewById(R.id.add_address_button);
        addAddressBtn.setOnClickListener(v -> {
            Intent i = new Intent(ClientAdresse.this, ClientAddAdresse.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

    }

    private void loadAdresses() {
        Log.d("TESTBIDON5", "Reçu " + " cartes");
        Call<List<Adresse>> call = apiService.getAllAdresse(identifiant);
        Log.d("TESTBIDON6", "Reçu " + " cartes");
        call.enqueue(new Callback<List<Adresse>>() {
            @Override
            public void onResponse(Call<List<Adresse>> call, Response<List<Adresse>> response) {
                Log.d("TESTBIDON7", "Reçu " + " cartes");
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("API_DEBUG", "Reçu " + response.body().size() + " cartes");
                        displayAdresses(response.body());
                    } else {
                        Log.e("API_ERROR", "Réponse vide");
                        Toast.makeText(ClientAdresse.this, "Aucune donnée reçue", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API_ERROR", "Code: " + response.code() + " - " + response.message());
                    Toast.makeText(ClientAdresse.this,
                            "Erreur serveur: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Adresse>> call, Throwable t) {
                Log.e("API_FAILURE", "Erreur réseau", t);
                Toast.makeText(ClientAdresse.this,
                        "Erreur réseau: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayAdresses(List<Adresse> adresses) {
        runOnUiThread(() -> {
            addressContainer.removeAllViews();

            if (adresses == null || adresses.isEmpty()) {
                TextView emptyView = new TextView(this);
                emptyView.setText("Aucune adresse enregistrée.");
                addressContainer.addView(emptyView);
                return;
            }

            for (Adresse adresse : adresses) {
                try {
                    Log.d("ADRESSE_DEBUG", "Affichage adresse: " + new Gson().toJson(adresse));

                    View adresseView = getLayoutInflater().inflate(R.layout.adresse_item, addressContainer, false);

                    TextView nom = adresseView.findViewById(R.id.nom_adresse);
                    TextView rue = adresseView.findViewById(R.id.rue_adresse);
                    TextView ville = adresseView.findViewById(R.id.code_ville_pays);

                    // Protection contre les null
                    String nomText = adresse.nom_adresse != null ? adresse.nom_adresse : "Adresse sans nom";
                    String rueText = (adresse.numero != null ? adresse.numero + " " : "") +
                            (adresse.nom_rue != null ? adresse.nom_rue : "");
                    String villeText = (adresse.code_postal != null ? adresse.code_postal + ", " : "") +
                            (adresse.ville != null ? adresse.ville + ", " : "") +
                            (adresse.pays != null ? adresse.pays : "");

                    nom.setText(nomText);
                    rue.setText(rueText);
                    ville.setText(villeText);

                    Button editBtn = adresseView.findViewById(R.id.edit_button);
                    Button deleteBtn = adresseView.findViewById(R.id.delete_button);

                    editBtn.setOnClickListener(v -> {
                        Intent i = new Intent(ClientAdresse.this, ClientEditAdresse.class);
                        i.putExtra("id", identifiant);
                        i.putExtra("adresse_id", adresse.nom_adresse);
                        startActivity(i);
                    });

                    deleteBtn.setOnClickListener(v -> {
                        deleteAdresse(adresse.nom_adresse);
                    });

                    addressContainer.addView(adresseView);
                } catch (Exception e) {
                    Log.e("ADRESSE_ERROR", "Erreur affichage", e);
                    Toast.makeText(this, "Erreur affichage adresse", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void deleteAdresse(String nomAdresse) {
        Call<Void> call = apiService.deleteAdresse(identifiant, nomAdresse);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ClientAdresse.this, "Adresse supprimée", Toast.LENGTH_SHORT).show();
                    loadAdresses(); // Recharger la liste
                } else {
                    Toast.makeText(ClientAdresse.this,
                            "Erreur: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClientAdresse.this,
                        "Échec: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
