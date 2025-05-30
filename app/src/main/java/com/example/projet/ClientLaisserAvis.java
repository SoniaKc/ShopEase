package com.example.projet;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientLaisserAvis extends AppCompatActivity {

    private EditText noteInput, commentaireInput;
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
        Log.e("AVIS_ERROR1er", "Erreur body : " + identifiant);

        apiService = ApiClient.getClient().create(ApiService.class);

        envoyerBtn.setOnClickListener(v -> {
            String note = noteInput.getText().toString().trim();
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
            Log.e("AVIS_ERROR", "Erreur body : " + nomProduit);
            Log.e("AVIS_ERROR", "Erreur body : " + loginBoutique);
            Log.e("AVIS_ERROR", "Erreur body : " + identifiant);
            Log.e("AVIS_ERROR", "Erreur body : " + note);
            Log.e("AVIS_ERROR", "Erreur body : " + commentaire);


            Call<Void> call = apiService.addCommentaire(avis);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ClientLaisserAvis.this, "Avis envoyé avec succès !", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ClientLaisserAvis.this, "Échec de l'envoi. Code : " + response.code(), Toast.LENGTH_LONG).show();
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
    }
}
