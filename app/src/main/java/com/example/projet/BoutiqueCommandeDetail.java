package com.example.projet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoutiqueCommandeDetail extends AppCompatActivity {

    private TextView totalCommandeView;
    private LinearLayout produitsContainer;
    private ApiService apiService;
    private String idTransaction, identifiant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_commande_detail);

        totalCommandeView = findViewById(R.id.total_commande);
        produitsContainer = findViewById(R.id.produits_container);

        // 🆕 Récupérer directement depuis l'intent
        idTransaction = getIntent().getStringExtra("idTransaction");
        identifiant = getIntent().getStringExtra("id");

        apiService = ApiClient.getClient().create(ApiService.class);

        fetchVenteDetails(idTransaction);
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navVentes = findViewById(R.id.navVentes);
        LinearLayout navProfile2 = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueProfilAccueil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navVentes.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueHistoriqueVentes.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navProfile2.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueProfilInfos.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });
    }

    private void fetchVenteDetails(String idTransaction) {
        Call<List<Vente>> call = apiService.getByIdTransaction(idTransaction);
        call.enqueue(new Callback<List<Vente>>() {
            @Override
            public void onResponse(Call<List<Vente>> call, Response<List<Vente>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Vente> ventes = response.body();

                    // Total global
                    try {
                        double totalCommande = Double.parseDouble(
                                ventes.get(0).total.replace("€", "").replace(",", ".").trim()
                        );
                        totalCommandeView.setText("Total commande : " + String.format("%.2f", totalCommande) + " €");
                    } catch (Exception e) {
                        totalCommandeView.setText("Total commande : Erreur");
                    }

                    for (Vente vente : ventes) {
                        fetchProduitEtAfficher(vente);
                    }

                } else {
                    Toast.makeText(BoutiqueCommandeDetail.this, "Commande introuvable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Vente>> call, Throwable t) {
                Toast.makeText(BoutiqueCommandeDetail.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
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

                    double prixUnitaire;
                    int quantite;
                    double totalProduit;

                    try {
                        prixUnitaire = Double.parseDouble(produit.prix.replace(",", ".").replace("€", "").trim());
                        quantite = Integer.parseInt(vente.quantite.trim());
                        totalProduit = prixUnitaire * quantite;
                    } catch (Exception e) {
                        Toast.makeText(BoutiqueCommandeDetail.this, "Erreur lecture produit : " + produit.nom, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    TextView produitView = new TextView(BoutiqueCommandeDetail.this);
                    produitView.setText(produit.nom + " | Qté: " + quantite + " | Total: " + String.format("%.2f", totalProduit) + " €");
                    produitView.setTextSize(16);
                    produitView.setPadding(8, 16, 8, 16);

                    produitView.setOnClickListener(v -> {
                        Intent intent = new Intent(BoutiqueCommandeDetail.this, ClientPageProduit.class);
                        intent.putExtra("nom_produit", produit.nom);
                        intent.putExtra("login_boutique", produit.login_boutique);
                        intent.putExtra("id", identifiant);
                        startActivity(intent);
                    });

                    produitsContainer.addView(produitView);
                } else {
                    Toast.makeText(BoutiqueCommandeDetail.this, "Produit introuvable : " + vente.nom_produit, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Produit> call, Throwable t) {
                Toast.makeText(BoutiqueCommandeDetail.this, "Erreur lors du chargement des produits", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
