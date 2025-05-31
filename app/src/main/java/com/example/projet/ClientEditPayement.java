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

public class ClientEditPayement extends Activity {
    private EditText cardNumber, cardHolder, cardExpiry, cardCvv;
    private TextView cardName;
    private Button saveButton, deleteButton;
    private String identifiant, originalCardName;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_edit_payement);

        apiService = ApiClient.getClient().create(ApiService.class);

        identifiant = getIntent().getStringExtra("id");
        originalCardName = getIntent().getStringExtra("carte_nom");

        cardName = findViewById(R.id.card_name_input);
        cardNumber = findViewById(R.id.card_number_input);
        cardHolder = findViewById(R.id.card_holder_input);
        cardExpiry = findViewById(R.id.expiry_date_input);
        cardCvv = findViewById(R.id.cvv_input);
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_button);

        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageHandler.getClientAndHandleAllImages(apiService, identifiant, photoProfil);

        loadPaymentCard();
        setupTopBottomNavigation();

        saveButton.setOnClickListener(v -> updatePaymentCard());
        deleteButton.setOnClickListener(v -> deletePaymentCard());
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

    private void loadPaymentCard() {
        Call<Paiement> call = apiService.getPaiement(identifiant, originalCardName);
        call.enqueue(new Callback<Paiement>() {
            @Override
            public void onResponse(Call<Paiement> call, Response<Paiement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Paiement paiement = response.body();
                    cardName.setText(paiement.nom_carte);
                    cardNumber.setText(paiement.numero);
                    cardHolder.setText(paiement.nom_personne_carte);
                    cardExpiry.setText(paiement.date_expiration);
                    cardCvv.setText(paiement.cvc);
                }
            }

            @Override
            public void onFailure(Call<Paiement> call, Throwable t) {
                Toast.makeText(ClientEditPayement.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePaymentCard() {
        String newCardName = cardName.getText().toString();
        String numeroCarte = cardNumber.getText().toString();
        String titulaire = cardHolder.getText().toString();
        String dateExpiration = cardExpiry.getText().toString();
        String cvv = cardCvv.getText().toString();

        if (newCardName.isEmpty() || numeroCarte.isEmpty() || titulaire.isEmpty() || dateExpiration.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        Paiement updatedPaiement = new Paiement();
        updatedPaiement.login = identifiant;
        updatedPaiement.nom_carte = newCardName;
        updatedPaiement.numero = numeroCarte;
        updatedPaiement.nom_personne_carte = titulaire;
        updatedPaiement.date_expiration = dateExpiration;
        updatedPaiement.cvc = cvv;

        Call<Void> call = apiService.updatePaiement(updatedPaiement);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    setResult(RESULT_OK);

                    Intent i = new Intent(getBaseContext(), ClientCB.class);
                    i.putExtra("id", identifiant);
                    startActivity(i);
                } else {
                    Toast.makeText(ClientEditPayement.this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClientEditPayement.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePaymentCard() {
        Call<Void> call = apiService.deletePaiement(identifiant, originalCardName);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    setResult(RESULT_OK);
                    Intent i = new Intent(getBaseContext(), ClientCB.class);
                    i.putExtra("id", identifiant);
                    startActivity(i);
                } else {
                    Toast.makeText(ClientEditPayement.this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClientEditPayement.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}