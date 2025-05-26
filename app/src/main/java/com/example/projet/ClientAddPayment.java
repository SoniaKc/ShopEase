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

public class ClientAddPayment extends Activity {
    private EditText cardName, cardNumber, cardHolder, cardExpiry, cardCvv;
    private Button saveButton;
    private String identifiant;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_add_payement);

        apiService = ApiClient.getClient().create(ApiService.class);
        identifiant = getIntent().getStringExtra("id");

        cardName = findViewById(R.id.card_name_input);
        cardNumber = findViewById(R.id.card_number_input);
        cardHolder = findViewById(R.id.card_holder_input);
        cardExpiry = findViewById(R.id.card_expiry_input);
        cardCvv = findViewById(R.id.card_cvv_input);
        saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(v -> savePaymentCard());
    }

    private void savePaymentCard() {
        String nomCarte = cardName.getText().toString();
        String numeroCarte = cardNumber.getText().toString();
        String titulaire = cardHolder.getText().toString();
        String dateExpiration = cardExpiry.getText().toString();
        String cvv = cardCvv.getText().toString();

        if (nomCarte.isEmpty() || numeroCarte.isEmpty() || titulaire.isEmpty() || dateExpiration.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        Paiement newPaiement = new Paiement();
        newPaiement.login=identifiant;
        newPaiement.nom_carte = nomCarte;
        newPaiement.numero = numeroCarte;
        newPaiement.nom_personne_carte  = titulaire;
        newPaiement.date_expiration = dateExpiration;
        newPaiement.cvc = cvv;

        Call<Void> call = apiService.addPaiement(newPaiement);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ClientAddPayment.this, "Carte ajoutée avec succès", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ClientAddPayment.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClientAddPayment.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}