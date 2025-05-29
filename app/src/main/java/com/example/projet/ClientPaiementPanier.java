package com.example.projet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import java.util.stream.Collectors;


import java.util.ArrayList;
import java.util.List;

public class ClientPaiementPanier extends Activity {
    private String identifiant;
    double total;
    private String selectedCard;
    private String selectedAddress;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_paiement_panier);

        // Initialisation des données
        identifiant = getIntent().getStringExtra("id");
        total = getIntent().getDoubleExtra("total",0.0);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Initialisation des vues
        Button carte = findViewById(R.id.carte);
        Button adresse = findViewById(R.id.adresse);
        Button validerPayer = findViewById(R.id.validerPayer);
        EditText code = findViewById(R.id.code);

        loadDefaultCard();
        loadDefaultAddress();


        // Gestion du clic sur "Choisir une autre carte"
        carte.setOnClickListener(b -> showCardSelectionDialog());
        adresse.setOnClickListener(v -> showAddressSelectionDialog());

        // Gestion du clic sur "Valider et Payer"
        validerPayer.setOnClickListener(b -> {
            String textCode = code.getText().toString().trim();
            if (isValidPromoCode(textCode)) {
                processPayment();
            } else {
                Toast.makeText(this, "Code promotion invalide", Toast.LENGTH_SHORT).show();
            }
        });


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

    }

    private void loadDefaultCard() {
        getCardsFromDatabase(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> cards = response.body();
                if (cards != null && !cards.isEmpty()) {
                    selectedCard = cards.get(0); // première carte
                    TextView TVcarte = findViewById(R.id.TVcarte);
                    TVcarte.setText(selectedCard);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(ClientPaiementPanier.this, "Impossible de charger les cartes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDefaultAddress() {
        getAddressesFromDatabase(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> adresses = response.body();
                if (adresses != null && !adresses.isEmpty()) {
                    selectedAddress = adresses.get(0); // première adresse
                    TextView TVadresse = findViewById(R.id.TVadresse);
                    TVadresse.setText(selectedAddress);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(ClientPaiementPanier.this, "Impossible de charger les adresses", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showCardSelectionDialog() {
        getCardsFromDatabase(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> cards = response.body();
                if (cards == null || cards.isEmpty()) {
                    Toast.makeText(ClientPaiementPanier.this, "Aucune carte enregistrée", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ClientPaiementPanier.this);
                builder.setTitle("Choisissez une carte");

                View dialogView = getLayoutInflater().inflate(R.layout.dialog_card_selection, null);
                builder.setView(dialogView);

                ListView listView = dialogView.findViewById(R.id.listViewCards);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ClientPaiementPanier.this,
                        android.R.layout.simple_list_item_1, cards);
                listView.setAdapter(adapter);

                AlertDialog dialog = builder.create();

                listView.setOnItemClickListener((parent, view, position, id) -> {
                    selectedCard = cards.get(position);
                    TextView TVcarte = findViewById(R.id.TVcarte);
                    TVcarte.setText(selectedCard);
                    dialog.dismiss();
                });

                dialog.show();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(ClientPaiementPanier.this, "Erreur de chargement des cartes", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getCardsFromDatabase(Callback<List<String>> callback) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllPaiement(identifiant).enqueue(new Callback<List<Paiement>>() {
            @Override
            public void onResponse(Call<List<Paiement>> call, Response<List<Paiement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> cards = response.body().stream().map(paiement -> {
                        String masked = getMaskedCardNumber(paiement.numero);
                        return paiement.nom_carte + " : " + masked + "\n" +
                                paiement.nom_personne_carte + " - " + paiement.date_expiration;
                    }).collect(Collectors.toList());
                    callback.onResponse(null, Response.success(cards));
                } else {
                    callback.onFailure(null, new Throwable("Erreur de récupération"));
                }
            }

            @Override
            public void onFailure(Call<List<Paiement>> call, Throwable t) {
                callback.onFailure(null, t);
            }
        });
    }


    private String getMaskedCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    private boolean isValidPromoCode(String code) {
        return code.equalsIgnoreCase("Sonia") || code.isEmpty();
    }

    private void processPayment() {
        // Implémentez votre logique de paiement ici
        Toast.makeText(this, "Paiement effectué avec la carte : " + selectedCard, Toast.LENGTH_SHORT).show();

        // Redirection vers l'activité de confirmation
        Intent intent = new Intent(this, ClientPaiementValidation.class);
        intent.putExtra("id", identifiant);
        startActivity(intent);
    }




    private void showAddressSelectionDialog() {
        getAddressesFromDatabase(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> adresses = response.body();
                if (adresses == null || adresses.isEmpty()) {
                    Toast.makeText(ClientPaiementPanier.this, "Aucune adresse enregistrée", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ClientPaiementPanier.this);
                builder.setTitle("Choisissez une adresse");

                View dialogView = getLayoutInflater().inflate(R.layout.dialog_card_selection, null);
                builder.setView(dialogView);

                ListView listView = dialogView.findViewById(R.id.listViewCards);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ClientPaiementPanier.this,
                        android.R.layout.simple_list_item_1, adresses);
                listView.setAdapter(adapter);

                AlertDialog dialog = builder.create();

                listView.setOnItemClickListener((parent, view, position, id) -> {
                    selectedAddress = adresses.get(position);
                    TextView TVadresse = findViewById(R.id.TVadresse);
                    TVadresse.setText(selectedAddress);
                    dialog.dismiss();
                });

                dialog.show();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(ClientPaiementPanier.this, "Erreur de chargement des adresses", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getAddressesFromDatabase(Callback<List<String>> callback) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllAdresse(identifiant).enqueue(new Callback<List<Adresse>>() {
            @Override
            public void onResponse(Call<List<Adresse>> call, Response<List<Adresse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> adresses = response.body().stream().map(a ->
                            a.nom_adresse + " : " + a.numero + " " + a.nom_rue + ", " + a.code_postal + " " + a.ville + ", " + a.pays
                    ).collect(Collectors.toList());
                    callback.onResponse(null, Response.success(adresses));
                } else {
                    callback.onFailure(null, new Throwable("Erreur de récupération"));
                }
            }

            @Override
            public void onFailure(Call<List<Adresse>> call, Throwable t) {
                callback.onFailure(null, t);
            }
        });
    }


}