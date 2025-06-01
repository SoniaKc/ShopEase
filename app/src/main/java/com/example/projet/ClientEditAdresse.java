package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientEditAdresse extends Activity {
    private EditText numeroInput, rueInput, codePostalInput, villeInput, paysInput;
    private TextView adresseInput;
    private Button saveButton, deleteButton;
    private String identifiant;
    private String nomAdresse;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_edit_adresse);

        apiService = ApiClient.getClient().create(ApiService.class);
        identifiant = getIntent().getStringExtra("id");
        nomAdresse = getIntent().getStringExtra("adresse_id");

        adresseInput = findViewById(R.id.nom_adresse_input);
        numeroInput = findViewById(R.id.numero_input);
        rueInput = findViewById(R.id.rue_input);
        codePostalInput = findViewById(R.id.code_postal_input);
        villeInput = findViewById(R.id.ville_input);
        paysInput = findViewById(R.id.pays_input);
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_button);

        saveButton.setOnClickListener(v -> updateAdresse());
        deleteButton.setOnClickListener(v -> deleteAdresse());

        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageHandler.getClientAndHandleAllImages(apiService, identifiant, photoProfil);

        loadAdresseDetails();
        setupTopBottomNavigation();
    }

    private void setupTopBottomNavigation() {
        // TOP
        ImageView navCart = findViewById(R.id.cartIcon);

        navCart.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientPanier.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        // BOTTOM
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navFavorites = findViewById(R.id.navFavorites);
        LinearLayout navProfile2 = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientAccueil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navFavorites.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientFavoris.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navProfile2.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientProfilAcceuil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });
    }

    private void loadAdresseDetails() {
        Call<Adresse> call = apiService.getAdresse(identifiant, nomAdresse);
        call.enqueue(new Callback<Adresse>() {
            @Override
            public void onResponse(Call<Adresse> call, Response<Adresse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Adresse adresse = response.body();
                    adresseInput.setText(nomAdresse);
                    numeroInput.setText(adresse.numero);
                    rueInput.setText(adresse.nom_rue);
                    codePostalInput.setText(adresse.code_postal);
                    villeInput.setText(adresse.ville);
                    paysInput.setText(adresse.pays);
                } else {
                    Toast.makeText(ClientEditAdresse.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Adresse> call, Throwable t) {
                Toast.makeText(ClientEditAdresse.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAdresse() {
        String numero = numeroInput.getText().toString().trim();
        String rue = rueInput.getText().toString().trim();
        String codePostal = codePostalInput.getText().toString().trim();
        String ville = villeInput.getText().toString().trim();
        String pays = paysInput.getText().toString().trim();

        if (numero.isEmpty() || rue.isEmpty() || codePostal.isEmpty() || ville.isEmpty() || pays.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        Adresse updatedAdresse = new Adresse();
        updatedAdresse.login = identifiant;
        updatedAdresse.nom_adresse = nomAdresse;
        updatedAdresse.numero = numero;
        updatedAdresse.nom_rue = rue;
        updatedAdresse.code_postal = codePostal;
        updatedAdresse.ville = ville;
        updatedAdresse.pays = pays;

        Call<Void> call = apiService.updateAdresse(updatedAdresse);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(ClientEditAdresse.this, ClientAdresse.class);
                    intent.putExtra("id", identifiant);
                    startActivity(intent);
                } else {
                    Toast.makeText(ClientEditAdresse.this, "Échec de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClientEditAdresse.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAdresse() {
        Call<Void> call = apiService.deleteAdresse(identifiant, nomAdresse);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    setResult(RESULT_OK);
                    Intent i = new Intent(getBaseContext(), ClientCB.class);
                    i.putExtra("id", identifiant);
                    startActivity(i);
                } else {
                    Toast.makeText(ClientEditAdresse.this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClientEditAdresse.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
