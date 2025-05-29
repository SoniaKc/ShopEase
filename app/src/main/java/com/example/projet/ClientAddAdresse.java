package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientAddAdresse extends Activity {
    private EditText numeroInput, rueInput, codePostalInput, villeInput, paysInput, nom_adresseInput;
    private Button saveButton;
    private String identifiant;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_add_adresse); // Ensure this layout includes the new inputs

        apiService = ApiClient.getClient().create(ApiService.class);
        identifiant = getIntent().getStringExtra("id");

        nom_adresseInput = findViewById(R.id.nom_adresse_input);
        numeroInput = findViewById(R.id.numero_input);
        rueInput = findViewById(R.id.rue_input);
        codePostalInput = findViewById(R.id.code_postal_input);
        villeInput = findViewById(R.id.ville_input);
        paysInput = findViewById(R.id.pays_input);
        saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(v -> saveAdresse());
    }

    private void saveAdresse() {
        String nom_adresse = nom_adresseInput.getText().toString().trim();
        String numero = numeroInput.getText().toString().trim();
        String rue = rueInput.getText().toString().trim();
        String codePostal = codePostalInput.getText().toString().trim();
        String ville = villeInput.getText().toString().trim();
        String pays = paysInput.getText().toString().trim();

        if (numero.isEmpty() || rue.isEmpty() || codePostal.isEmpty() || ville.isEmpty() || pays.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        Adresse newAdresse = new Adresse();
        newAdresse.login = identifiant;
        newAdresse.nom_adresse = nom_adresse;
        newAdresse.numero = numero;
        newAdresse.nom_rue = rue;
        newAdresse.code_postal = codePostal;
        newAdresse.ville = ville;
        newAdresse.pays = pays;

        Call<Void> call = apiService.addAdresse(newAdresse);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ClientAddAdresse.this, "Adresse ajoutée avec succès", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    Intent intent = new Intent(ClientAddAdresse.this, ClientAdresse.class);
                    intent.putExtra("id", identifiant);
                    startActivity(intent);
                } else {
                    Toast.makeText(ClientAddAdresse.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClientAddAdresse.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
