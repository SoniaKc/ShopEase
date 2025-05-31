package com.example.projet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoutiqueCommandeDetail extends Activity {

    private TextView totalCommande, nomCommande, dateCommande, statutCommande, clientCommande;
    private Button boutonValider, boutonRefuser;
    private ApiService apiService;
    private String idTransaction, identifiant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_commande_detail);

        nomCommande = findViewById(R.id.idTransaction);
        totalCommande = findViewById(R.id.total);
        dateCommande = findViewById(R.id.dateVente);
        statutCommande = findViewById(R.id.statut);
        clientCommande = findViewById(R.id.idClient);
        boutonValider = findViewById(R.id.save_button);
        boutonRefuser = findViewById(R.id.delete_button);

        identifiant = getIntent().getStringExtra("id");

        apiService = ApiClient.getClient().create(ApiService.class);

        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageHandler.getBoutiqueAndHandleAllImages(apiService, identifiant, photoProfil);

        Gson gson = new Gson();
        String jsonCommande = getIntent().getStringExtra("commandeEntiereJson");
        BoutiqueCommandeEntiere commande = gson.fromJson(jsonCommande, BoutiqueCommandeEntiere.class);

        idTransaction = commande.idTransaction;

        nomCommande.append(idTransaction);
        totalCommande.append(commande.total);
        dateCommande.append(commande.date_vente);
        statutCommande.append(commande.statut);
        clientCommande.append(commande.idClient);


        List<BoutiqueCommandeDetail.LigneCommande> lignes = new ArrayList<>();
        for (int i = 0; i < commande.nom_produit.size(); i++) {
            lignes.add(new BoutiqueCommandeDetail.LigneCommande(
                    commande.nom_produit.get(i),
                    commande.quantite.get(i)
            ));
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerCommandes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BoutiqueDetailVenteAdapter adapter = new BoutiqueDetailVenteAdapter(this, lignes);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);


        boutonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Void> call = apiService.updateStatut(commande.idTransaction, "Commande Acceptée");
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(BoutiqueCommandeDetail.this, "Commande Acceptée", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), BoutiqueHistoriqueVentes.class);
                            intent.putExtra("id", identifiant);
                            startActivity(intent);
                        } else {
                            //Toast.makeText(BoutiqueCommandeDetail.this, "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(BoutiqueCommandeDetail.this, "Échec réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        boutonRefuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BoutiqueCommandeDetail.this)
                        .setTitle("Confirmation")
                        .setMessage("Êtes-vous sûr de vouloir refuser cette commande ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Call<Void> call = apiService.updateStatut(commande.idTransaction, "Commande Refusée");
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(BoutiqueCommandeDetail.this, "Commande Refusée", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), BoutiqueHistoriqueVentes.class);
                                            intent.putExtra("id", identifiant);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(BoutiqueCommandeDetail.this, "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(BoutiqueCommandeDetail.this, "Échec réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Non", null)
                        .show();
            }
        });

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

    public class LigneCommande {
        public String loginBoutique;
        public String nomProduit;
        public String quantite;

        public LigneCommande(String nomProduit, String quantite) {
            this.loginBoutique = identifiant;
            this.nomProduit = nomProduit;
            this.quantite = quantite;
        }
    }

    /*
    private void fetchVenteDetails(String idTransaction) {
        Call<List<Vente>> call = apiService.getByIdTransaction(idTransaction);
        call.enqueue(new Callback<List<Vente>>() {
            @Override
            public void onResponse(Call<List<Vente>> call, Response<List<Vente>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Vente> ventes = response.body();
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
    }*/
}
