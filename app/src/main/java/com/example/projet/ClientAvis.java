package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientAvis extends Activity {
    String identifiant;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_avis);

        apiService = ApiClient.getClient().create(ApiService.class);
        identifiant = getIntent().getStringExtra("id");

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


        Call<List<Commentaire>> call = apiService.getCommentairesByClient(identifiant);
        call.enqueue(new Callback<List<Commentaire>>() {
            @Override
            public void onResponse(Call<List<Commentaire>> call, Response<List<Commentaire>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // Récupérer la liste des commentaires (à remplacer par votre appel API)
                        List<Commentaire> commentaires = response.body();
                        // Créer l'adapter
                        CommentaireAdapter adapter = new CommentaireAdapter(ClientAvis.this, commentaires);
                        // Configurer la ListView
                        ListView listView = findViewById(R.id.listeCommentaires);
                        listView.setAdapter(adapter);
                    } else {
                        Log.e("API_ERROR", "Réponse vide");
                        Toast.makeText(ClientAvis.this, "Aucune donnée reçue", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API_ERROR", "Code: " + response.code() + " - " + response.message());
                    Toast.makeText(ClientAvis.this,
                            "Erreur serveur: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Commentaire>> call, Throwable t) {
                Log.e("API_FAILURE", "Erreur réseau", t);
                Toast.makeText(ClientAvis.this,
                        "Erreur réseau: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });




    }

    // Méthode temporaire pour les tests - à remplacer par votre appel API
    private List<Commentaire> getCommentaires() {
        List<Commentaire> commentaires = new ArrayList<>();

        // Exemple de données
        Commentaire c1 = new Commentaire();
        c1.nom_produit = "Produit 1";
        c1.note = "4";
        c1.commentaire = "Très bon produit";

        Commentaire c2 = new Commentaire();
        c2.nom_produit = "Produit 2";
        c2.note = "5";
        c2.commentaire = "Excellent !";

        commentaires.add(c1);
        commentaires.add(c2);

        return commentaires;
    }
}
