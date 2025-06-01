package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientPageProduit extends Activity {

    String identifiant;
    ApiService apiService;

    String login_boutique;
    String nomProduit;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_page_produit);

        identifiant = getIntent().getStringExtra("id");
        login_boutique = getIntent().getStringExtra("login_boutique");
        nomProduit = getIntent().getStringExtra("nomProduit");

        apiService = ApiClient.getClient().create(ApiService.class);

        TextView nom = findViewById(R.id.nomProduit);
        TextView prix = findViewById(R.id.prix);
        TextView reduction = findViewById(R.id.reduction);
        TextView description = findViewById(R.id.description);
        TextView categories = findViewById(R.id.categories);
        ImageView imageProduit = findViewById(R.id.imageProduit);

        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageHandler.getClientAndHandleAllImages(apiService, identifiant, photoProfil);

        apiService.getProduit(login_boutique,nomProduit).enqueue(new Callback<Produit>() {
            @Override
            public void onResponse(Call<Produit> call, Response<Produit> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Produit produit = response.body();

                    nom.setText(produit.nom);
                    prix.setText(produit.prix.replaceAll("[^\\d.,]", "") + " $");
                    if (!produit.reduction.equals("")){
                        reduction.setText("-" + produit.reduction.replaceAll("[^\\d.,]", "") + " $");
                    }
                    description.setText(produit.description);
                    categories.setText(produit.categories);
                    ImageHandler.handleAllImages(produit.image, imageProduit);
                }
            }

            @Override
            public void onFailure(Call<Produit> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Erreur de chargement du produit", Toast.LENGTH_SHORT).show();
            }
        });


        Call<List<Commentaire>> call = apiService.getCommentairesByProduit(login_boutique,nomProduit);
        call.enqueue(new Callback<List<Commentaire>>() {
            @Override
            public void onResponse(Call<List<Commentaire>> call, Response<List<Commentaire>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Commentaire> commentaires = response.body();
                        LinearLayout container = findViewById(R.id.commentairesContainer);
                        container.removeAllViews();

                        LayoutInflater inflater = LayoutInflater.from(ClientPageProduit.this);
                        for (Commentaire commentaire : commentaires) {
                            View view = inflater.inflate(R.layout.item_commentaire_page_produit, container, false);

                            TextView nomClient = view.findViewById(R.id.nomClient);
                            RatingBar noteProduit = view.findViewById(R.id.noteProduit);
                            TextView texteCommentaire = view.findViewById(R.id.texteCommentaire);

                            nomClient.setText(commentaire.idClient);
                            noteProduit.setRating(Float.parseFloat(commentaire.note));
                            texteCommentaire.setText(commentaire.commentaire);

                            container.addView(view);
                        }
                    } else {
                        //Toast.makeText(ClientPageProduit.this, "Aucune donnée reçue", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Toast.makeText(ClientPageProduit.this, "Erreur serveur: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Commentaire>> call, Throwable t) {
                //Toast.makeText(ClientPageProduit.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        Button ajouterFavoris = findViewById(R.id.btn_favoris);
        ajouterFavoris.setOnClickListener(v -> {
            Favoris favoris = new Favoris();

            favoris.idClient = identifiant;
            favoris.login_boutique = login_boutique;
            favoris.nom_produit = nomProduit;

            Call<Void> callAddFavoris = apiService.addFavori(favoris);
            callAddFavoris.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ClientPageProduit.this, "Ajouté aux favoris ", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                }
            });
        });


        Button ajouterPanier = findViewById(R.id.btn_panier);
        ajouterPanier.setOnClickListener(v -> {
            String quantite = (String) ((TextView)findViewById(R.id.tv_quantite)).getText();

            Panier panier = new Panier();

            panier.idClient = identifiant;
            panier.login_boutique = login_boutique;
            panier.nom_produit = nomProduit;
            panier.quantite = quantite;

            Call<Void> callAddPanier = apiService.addToCart(panier);
            callAddPanier.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ClientPageProduit.this, "Ajouté au panier", Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(ClientPageProduit.this, "Erreur serveur: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    //Toast.makeText(ClientPageProduit.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });


        Button plusQte = findViewById(R.id.btn_plus);
        plusQte.setOnClickListener(v -> {
            int quantite = Integer.parseInt((String) ((TextView)findViewById(R.id.tv_quantite)).getText());
            ((TextView) findViewById(R.id.tv_quantite)).setText(String.valueOf(quantite+1));
        });

        Button moinsQte = findViewById(R.id.btn_moins);
        moinsQte.setOnClickListener(v -> {
            int quantite = Integer.parseInt((String) ((TextView)findViewById(R.id.tv_quantite)).getText());
            ((TextView) findViewById(R.id.tv_quantite)).setText(String.valueOf(quantite-1));
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
}
