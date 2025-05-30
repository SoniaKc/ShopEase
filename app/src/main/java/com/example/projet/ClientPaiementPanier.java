package com.example.projet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;
import java.util.stream.Collectors;
import retrofit2.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import android.widget.Toast;

public class ClientPaiementPanier extends Activity {
    private String identifiant;
    double total;
    private String selectedCard;
    private String selectedAddress;

    private List<String> cardNameList = new ArrayList<>();
    private List<String> addressNameList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_paiement_panier);

        identifiant = getIntent().getStringExtra("id");
        total = getIntent().getDoubleExtra("total", 0.0);

        Button carte = findViewById(R.id.carte);
        Button adresse = findViewById(R.id.adresse);
        Button validerPayer = findViewById(R.id.validerPayer);
        EditText code = findViewById(R.id.code);

        loadDefaultCard();
        loadDefaultAddress();

        carte.setOnClickListener(b -> showCardSelectionDialog());
        adresse.setOnClickListener(v -> showAddressSelectionDialog());

        Spinner spinnerLivraison = findViewById(R.id.spinnerLivraison);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Standard - 3 à 5 jours", "Express - 24h", "Retrait en magasin"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLivraison.setAdapter(adapter);

        validerPayer.setOnClickListener(b -> {
            String textCode = code.getText().toString().trim();
            if (isValidPromoCode(textCode)) {
                processPayment();
            } else {
                Toast.makeText(this, "Code promotion invalide", Toast.LENGTH_SHORT).show();
            }
        });

        setupTopBottomNavigation();
    }

    private void setupTopBottomNavigation() {
        ImageView navCart = findViewById(R.id.cartIcon);

        navCart.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientPanier.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

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
                if (cardNameList != null && !cardNameList.isEmpty()) {
                    selectedCard = cardNameList.get(0);
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
                if (addressNameList != null && !addressNameList.isEmpty()) {
                    selectedAddress = addressNameList.get(0);
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
                    selectedCard = cardNameList.get(position);
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
                    selectedAddress = addressNameList.get(position);
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

    private void getCardsFromDatabase(Callback<List<String>> callback) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllPaiement(identifiant).enqueue(new Callback<List<Paiement>>() {
            @Override
            public void onResponse(Call<List<Paiement>> call, Response<List<Paiement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> displayList = new ArrayList<>();
                    cardNameList.clear();

                    for (Paiement paiement : response.body()) {
                        displayList.add(paiement.nom_carte + " : " + getMaskedCardNumber(paiement.numero));
                        cardNameList.add(paiement.nom_carte);
                    }
                    callback.onResponse(null, Response.success(displayList));
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

    private void getAddressesFromDatabase(Callback<List<String>> callback) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllAdresse(identifiant).enqueue(new Callback<List<Adresse>>() {
            @Override
            public void onResponse(Call<List<Adresse>> call, Response<List<Adresse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> displayList = new ArrayList<>();
                    addressNameList.clear();

                    for (Adresse adresse : response.body()) {
                        displayList.add(adresse.nom_adresse + " : " + adresse.numero + " " +
                                adresse.nom_rue + ", " + adresse.code_postal + " " +
                                adresse.ville + ", " + adresse.pays);
                        addressNameList.add(adresse.nom_adresse);
                    }

                    callback.onResponse(null, Response.success(displayList));
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
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.getFullCart(identifiant).enqueue(new Callback<List<Panier>>() {
            @Override
            public void onResponse(Call<List<Panier>> call, Response<List<Panier>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Panier> panierList = response.body();
                    for (Panier item : panierList) {
                        LigneVente vente = new LigneVente();
                        vente.login_boutique = item.login_boutique;
                        vente.idClient = item.idClient;
                        vente.nom_produit = item.nom_produit;
                        vente.quantite = item.quantite;
                        vente.nom_adresse = selectedAddress;
                        vente.nom_paiement = selectedCard;
                        vente.total = String.valueOf(total);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        vente.date_vente = sdf.format(new Date());
                        vente.statut = "En cours";

                        apiService.addVente(vente).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                apiService.removeFromCart(item.login_boutique, item.nom_produit, item.idClient)
                                        .enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                // OK
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                Toast.makeText(ClientPaiementPanier.this, "Erreur suppression panier", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(ClientPaiementPanier.this, "Erreur ajout vente", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    Toast.makeText(ClientPaiementPanier.this, "Paiement effectué avec la carte : " + selectedCard, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ClientPaiementPanier.this, ClientPaiementValidation.class);
                    intent.putExtra("id", identifiant);
                    startActivity(intent);
                } else {
                    Toast.makeText(ClientPaiementPanier.this, "Erreur récupération panier", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Panier>> call, Throwable t) {
                Toast.makeText(ClientPaiementPanier.this, "Erreur réseau panier", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
