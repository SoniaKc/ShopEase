package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientPanier extends Activity {

    private RecyclerView recyclerView;
    private PanierAdapter adapter;
    private List<PanierDisplayItem> panierDisplayItems = new ArrayList<>();
    private ApiService apiService;
    private String idClient;
    private TextView totalPanier;
    String identifiant;
    double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_panier);


        identifiant = getIntent().getStringExtra("id");


        recyclerView = findViewById(R.id.recyclerPanier);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PanierAdapter(panierDisplayItems, this::onDeleteClicked, this::onQuantityChanged);
        recyclerView.setAdapter(adapter);

        totalPanier = findViewById(R.id.totalPanier);

        idClient = getIntent().getStringExtra("id");
        apiService = ApiClient.getClient().create(ApiService.class);

        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageHandler.getClientAndHandleAllImages(apiService, identifiant, photoProfil);

        loadPanier();

        Button btnPayer = findViewById(R.id.btnPayer);
        btnPayer.setOnClickListener(v -> {
            if (total > 0.0){
                Intent i = new Intent(this, ClientPaiementPanier.class);
                i.putExtra("id", identifiant);
                i.putExtra("total",total);
                startActivity(i);
            }
            else {
                Toast.makeText(ClientPanier.this, "Il n'y a rien dans le panier", Toast.LENGTH_SHORT).show();
            }
        });

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

    private void loadPanier() {
        TextView panierVide = findViewById(R.id.panierVide);
        apiService.getFullCart(idClient).enqueue(new Callback<List<Panier>>() {
            @Override
            public void onResponse(Call<List<Panier>> call, Response<List<Panier>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    panierDisplayItems.clear();

                    if (response.body().isEmpty()) {
                        panierVide.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        totalPanier.setText("Valeur totale : 0 $");
                    } else {
                        panierVide.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        List<Panier> panierList = response.body();
                        final int totalItems = panierList.size();
                        final int[] loadedCount = {0};

                        for (Panier item : panierList) {
                            apiService.getProduit(item.login_boutique, item.nom_produit)
                                    .enqueue(new Callback<Produit>() {
                                        @Override
                                        public void onResponse(Call<Produit> call, Response<Produit> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                Produit produit = response.body();
                                                produit.prix = produit.prix.replaceAll("[€$£¥₹]", "");
                                                produit.reduction = produit.reduction.replaceAll("[€$£¥₹]", "");
                                                PanierDisplayItem displayItem = new PanierDisplayItem(produit, item);
                                                panierDisplayItems.add(displayItem);
                                                adapter.notifyDataSetChanged();

                                                loadedCount[0]++;
                                                if (loadedCount[0] == totalItems) {
                                                    updateTotal();
                                                }
                                            }
                                        }


                                        @Override
                                        public void onFailure(Call<Produit> call, Throwable t) {
                                            Toast.makeText(ClientPanier.this, "Erreur chargement produit", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Panier>> call, Throwable t) {
            }
        });
    }


    private void updateTotal() {
        total = 0;
        for (PanierDisplayItem item : panierDisplayItems) {
            try {
                int qte = Integer.parseInt(item.getPanier().quantite.trim());
                String prixStr = item.getProduit().prix.replace(",", ".");
                double prix = Double.parseDouble(prixStr.trim());
                double reduc = 0.0;
                if (!item.getProduit().reduction.equals("")){
                    String reducStr = item.getProduit().reduction.replace(",", ".").trim();
                    reduc = Double.parseDouble(reducStr);
                }
                double sousTotal = (prix - reduc) * qte;
                total += sousTotal;
             } catch (NumberFormatException e) {
                System.err.println("Erreur conversion prix ou quantité pour " + item.getProduit().nom);
            }
        }
        totalPanier.setText("Valeur totale : " + total + " $");}


    private void onDeleteClicked(PanierDisplayItem item) {
        apiService.removeFromCart(item.getProduit().login_boutique, item.getProduit().nom, idClient)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        panierDisplayItems.remove(item);
                        adapter.notifyDataSetChanged();
                        updateTotal();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ClientPanier.this, "Erreur suppression", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onQuantityChanged(PanierDisplayItem item, int newQuantity) {
        item.getPanier().quantite = String.valueOf(newQuantity);
        item.getPanier().idClient = idClient;

        apiService.updateCartItemQuantity(item.getPanier()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    adapter.notifyDataSetChanged();
                    updateTotal();
                } else {
                    Toast.makeText(ClientPanier.this, "Erreur mise à jour quantité", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClientPanier.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
