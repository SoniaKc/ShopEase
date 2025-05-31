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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientCB extends Activity {
    String identifiant;
    ApiService apiService;
    LinearLayout cardsContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_moyens_paiement);

        apiService = ApiClient.getClient().create(ApiService.class);
        identifiant = getIntent().getStringExtra("id");
        this.cardsContainer = findViewById(R.id.cards_container);

        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageHandler.getClientAndHandleAllImages(apiService, identifiant, photoProfil);

        setupTopBottomNavigation();
        loadPaymentCards();

        Button addCardButton = findViewById(R.id.add_card_button);
        addCardButton.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientAddPayment.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });
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

    private void loadPaymentCards() {
        Log.d("API_DEBUG", "Tentative de récupération des cartes pour: " + identifiant);
        Call<List<Paiement>> call = apiService.getAllPaiement(identifiant);
        call.enqueue(new Callback<List<Paiement>>() {
            @Override
            public void onResponse(Call<List<Paiement>> call, Response<List<Paiement>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("API_DEBUG", "Reçu " + response.body().size() + " cartes");
                        displayPaymentCards(response.body());
                    } else {
                        Log.e("API_ERROR", "Réponse vide");
                        Toast.makeText(ClientCB.this, "Aucune donnée reçue", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ClientCB.this, "Erreur serveur: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Paiement>> call, Throwable t) {
                Toast.makeText(ClientCB.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayPaymentCards(List<Paiement> paiements) {
        runOnUiThread(() -> {
            cardsContainer.removeAllViews();

            if (paiements.isEmpty()) {
                TextView emptyView = new TextView(this);
                emptyView.setText("Aucune carte enregistrée");
                cardsContainer.addView(emptyView);
                return;
            }

            for (Paiement paiement : paiements) {
                try {
                    View cardView = getLayoutInflater().inflate(R.layout.payment_card_item, cardsContainer, false);

                    TextView cardName = cardView.findViewById(R.id.card_name);
                    TextView cardNumber = cardView.findViewById(R.id.card_number);
                    TextView cardHolder = cardView.findViewById(R.id.card_holder);
                    TextView cardExpiry = cardView.findViewById(R.id.card_expiry);

                    cardName.setText(paiement.nom_carte != null ? paiement.nom_carte : "N/A");
                    cardNumber.setText("•••• •••• •••• " + (paiement.numero != null && paiement.numero.length() > 12 ? paiement.numero.substring(12) : "****"));
                    cardHolder.setText(paiement.nom_personne_carte != null ? paiement.nom_personne_carte : "N/A");
                    cardExpiry.setText(paiement.date_expiration != null ? paiement.date_expiration : "N/A");

                    cardsContainer.addView(cardView);

                    Button editButton = cardView.findViewById(R.id.edit_button);
                    Button deleteButton = cardView.findViewById(R.id.delete_button);

                    deleteButton.setOnClickListener(v -> {
                        deletePaymentCard(paiement.nom_carte);
                    });

                    editButton.setOnClickListener(v -> {
                        Intent i = new Intent(ClientCB.this, ClientEditPayement.class);
                        i.putExtra("id", identifiant);
                        i.putExtra("carte_nom", paiement.nom_carte);
                        startActivity(i);
                    });

                } catch (Exception e) {
                    Log.e("CARD_ERROR", "Erreur affichage carte", e);
                }


            }
        });
    }

    private void deletePaymentCard(String cardName) {
        Call<Void> call = apiService.deletePaiement(identifiant, cardName);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ClientCB.this, "Carte supprimée", Toast.LENGTH_SHORT).show();
                    loadPaymentCards();
                } else {
                    Toast.makeText(ClientCB.this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClientCB.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

}