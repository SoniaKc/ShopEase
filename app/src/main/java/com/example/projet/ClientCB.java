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
        this.cardsContainer = findViewById(R.id.cards_container); // Ajoutez cet ID dans votre layout

        // TOP NAVIGATION BAR
        ImageView navCart = findViewById(R.id.cartIcon);
        navCart.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientPanier.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        // BOTTOM NAVIGATION BAR
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

        // Charger les cartes
        loadPaymentCards();

        // Bouton Ajouter une carte
        Button addCardButton = findViewById(R.id.add_card_button);
        addCardButton.setOnClickListener(v -> {
            // Ajoutez ici le code pour ajouter une nouvelle carte
            Intent i = new Intent(this, ClientAddPayment.class); // Créez cette activité
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
                    Log.e("API_ERROR", "Code: " + response.code() + " - " + response.message());
                    Toast.makeText(ClientCB.this,
                            "Erreur serveur: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Paiement>> call, Throwable t) {
                Log.e("API_FAILURE", "Erreur réseau", t);
                Toast.makeText(ClientCB.this,
                        "Erreur réseau: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
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
                    View cardView = getLayoutInflater().inflate(R.layout.payment_card_item, null);

                    TextView cardName = cardView.findViewById(R.id.card_name);
                    TextView cardNumber = cardView.findViewById(R.id.card_number);
                    TextView cardHolder = cardView.findViewById(R.id.card_holder);
                    TextView cardExpiry = cardView.findViewById(R.id.card_expiry);

                    // Debug logging
                    Log.d("CARD_DATA", "Nom: " + paiement.nom_carte);
                    Log.d("CARD_DATA", "Numéro: " + paiement.numero);

                    cardName.setText(paiement.nom_carte != null ? paiement.nom_carte : "N/A");
                    cardNumber.setText("•••• •••• •••• " +
                            (paiement.numero != null && paiement.numero.length() > 12
                                    ? paiement.numero.substring(12)
                                    : "****"));
                    cardHolder.setText(paiement.nom_personne_carte != null ? paiement.nom_personne_carte : "N/A");
                    cardExpiry.setText(paiement.date_expiration != null ? paiement.date_expiration : "N/A");

                    cardsContainer.addView(cardView);
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
                    loadPaymentCards(); // Recharger la liste
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

    private void testAvecDonneesMock() {
        List<Paiement> paiementsMock = new ArrayList<>();
        Paiement mock1 = new Paiement();
        mock1.nom_carte = "VISA";
        mock1.numero = "1234567812345678";
        mock1.nom_personne_carte = "Jean Dupont";
        mock1.date_expiration = "12/25";

        paiementsMock.add(mock1);

        displayPaymentCards(paiementsMock);
    }
}