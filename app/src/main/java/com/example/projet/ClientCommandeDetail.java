package com.example.projet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.Gson;

public class ClientCommandeDetail extends AppCompatActivity {

    private TextView totalCommandeView;
    private LinearLayout produitsContainer;
    private ApiService apiService;
    private String idTransaction, identifiant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_commande_detail);

        totalCommandeView = findViewById(R.id.total_commande);
        produitsContainer = findViewById(R.id.produits_container);

        Gson gson = new Gson();
        String jsonCommande = getIntent().getStringExtra("commandeEntiereJson");
        CommandeEntiere commande = gson.fromJson(jsonCommande, CommandeEntiere.class);

        identifiant = commande.idClient;
        Log.e("json", jsonCommande);
        Log.e("AVIS_ERROR2e", "Erreur body : " + identifiant);

        idTransaction = commande.idTransaction;
        apiService = ApiClient.getClient().create(ApiService.class);
        Log.d("DEBUG", "idTransaction reçu : " + idTransaction);


        fetchVenteDetails(idTransaction);

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

    }

    private void fetchVenteDetails(String idTransaction) {
        Call<List<Vente>> call = apiService.getByIdTransaction(idTransaction);
        call.enqueue(new Callback<List<Vente>>() {
            @Override
            public void onResponse(Call<List<Vente>> call, Response<List<Vente>> response) {
                Log.d("DEBUG", "Code HTTP : " + response.code());
                Log.d("DEBUG", "Body : " + new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Vente> ventes = response.body();

                    double totalCommande = 0.0;
                    try {
                        String totalStr = ventes.get(0).total.replace("€", "").trim();
                        totalCommande = Double.parseDouble(totalStr);
                        totalCommandeView.setText("Total commande : " + String.format("%.2f", totalCommande) + " €");
                    } catch (Exception ignored) {
                        totalCommandeView.setText("Total commande : Erreur");
                    }

                    for (Vente vente : ventes) {
                        fetchProduitEtAfficher(vente);
                    }
                } else {
                    Toast.makeText(ClientCommandeDetail.this, "Commande introuvable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Vente>> call, Throwable t) {
                Log.e("DEBUG", "Erreur réseau : ", t);
                Toast.makeText(ClientCommandeDetail.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchProduitEtAfficher(Vente vente) {
        Call<Produit> produitCall = apiService.getProduit(vente.login_boutique, vente.nom_produit);
        produitCall.enqueue(new Callback<Produit>() {
            @Override
            public void onResponse(Call<Produit> call, Response<Produit> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Produit produit = response.body();

                    Log.d("DEBUG", "Produit reçu : " + new Gson().toJson(produit));

                    double prixUnitaire = 0.0;
                    int quantite = 0;
                    double totalProduit = 0.0;

                    try {
                        String prixStr = produit.prix.replace("€", "").trim();
                        String quantiteStr = vente.quantite.trim();

                        prixUnitaire = Double.parseDouble(prixStr.replace(",", "."));
                        quantite = Integer.parseInt(quantiteStr);
                        totalProduit = prixUnitaire * quantite;

                    } catch (Exception e) {
                        Log.e("DEBUG", "Erreur parsing quantité ou prix", e);
                        Toast.makeText(ClientCommandeDetail.this, "Erreur lecture produit : " + produit.nom, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 🧾 TextView avec les infos du produit
                    TextView produitView = new TextView(ClientCommandeDetail.this);
                    produitView.setText(produit.nom + " | Qté: " + quantite + " | Total: " +
                            String.format("%.2f", totalProduit) + " €");
                    produitView.setTextSize(16);
                    produitView.setPadding(8, 16, 8, 4);

                    // ▶ Clic pour accéder à la page produit
                    produitView.setOnClickListener(v -> {
                        Intent intent = new Intent(ClientCommandeDetail.this, ClientPageProduit.class);
                        intent.putExtra("nom_produit", produit.nom);
                        intent.putExtra("login_boutique", produit.login_boutique);
                        intent.putExtra("id", identifiant);
                        startActivity(intent);
                    });

                    // ✍️ Bouton "Laisser un avis"
                    Button avisButton = new Button(ClientCommandeDetail.this);
                    avisButton.setText("Laisser un avis");
                    avisButton.setPadding(8, 4, 8, 16);
                    avisButton.setOnClickListener(v -> {
                        Intent intent = new Intent(ClientCommandeDetail.this, ClientLaisserAvis.class);
                        intent.putExtra("nom_produit", produit.nom);
                        intent.putExtra("login_boutique", produit.login_boutique);
                        Log.e("AVIS_ERROR1er", "Erreur body : " + identifiant);
                        intent.putExtra("id", identifiant);
                        startActivity(intent);
                    });

                    // 👇 Ajout des vues au layout
                    produitsContainer.addView(produitView);
                    produitsContainer.addView(avisButton);

                } else {
                    Log.e("DEBUG", "Produit introuvable : " + vente.nom_produit);
                    Toast.makeText(ClientCommandeDetail.this, "Produit introuvable : " + vente.nom_produit, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Produit> call, Throwable t) {
                Log.e("DEBUG", "Erreur réseau fetch produit", t);
                Toast.makeText(ClientCommandeDetail.this, "Erreur lors du chargement des produits", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
