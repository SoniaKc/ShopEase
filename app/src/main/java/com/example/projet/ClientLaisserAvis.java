package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientLaisserAvis extends Activity {

    RatingBar noteInput;
    private EditText commentaireInput;
    private String nomProduit, loginBoutique, identifiant;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_laisser_avis);

        noteInput = findViewById(R.id.note_input);
        commentaireInput = findViewById(R.id.commentaire_input);
        Button envoyerBtn = findViewById(R.id.envoyer_avis);

        nomProduit = getIntent().getStringExtra("nom_produit");
        loginBoutique = getIntent().getStringExtra("login_boutique");
        identifiant = getIntent().getStringExtra("id");

        apiService = ApiClient.getClient().create(ApiService.class);

        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageHandler.getClientAndHandleAllImages(apiService, identifiant, photoProfil);

        TextView tvNomProduit = findViewById(R.id.nom_produit);
        tvNomProduit.setText(nomProduit);

        envoyerBtn.setOnClickListener(v -> {
            String note = String.valueOf(noteInput.getRating());
            String commentaire = commentaireInput.getText().toString().trim();

            if (note.isEmpty() || commentaire.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                return;
            }

            Commentaire avis = new Commentaire();
            avis.nom_produit = nomProduit;
            avis.login_boutique = loginBoutique;
            avis.idClient = identifiant;
            avis.note = note;
            avis.commentaire = commentaire;


            Call<Void> call = apiService.addCommentaire(avis);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ClientLaisserAvis.this, "Avis envoyé avec succès !", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ClientLaisserAvis.this, "Vous avez déjà laissé un avis sur cet article", Toast.LENGTH_LONG).show();
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                            android.util.Log.e("AVIS_ERROR", "Erreur body : " + errorBody);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ClientLaisserAvis.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
                }
            });
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
