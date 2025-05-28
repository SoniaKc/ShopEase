package com.example.projet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Pb ici sur comment récupérer le nouveau identifiant modifié

public class BoutiqueProfilInfos extends Activity {
    String identifiant;
    ApiService apiService;
    Boutique currentBoutique;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_profil_infos);

        apiService = ApiClient.getClient().create(ApiService.class);
        identifiant = getIntent().getStringExtra("id");

        TextView Nom = findViewById(R.id.nom);
        TextView Email = findViewById(R.id.email);
        TextView Tel = findViewById(R.id.tel);
        TextView Id = findViewById(R.id.id);
        TextView Mdp = findViewById(R.id.mdp);

        TextView FormeJuridique = findViewById(R.id.formeJuridique);
        TextView Siret = findViewById(R.id.siret);
        TextView SiegeSocial = findViewById(R.id.siegeSocial);
        TextView PaysEnregitrement = findViewById(R.id.paysEnregitrement);

        apiService.getBoutique(identifiant).enqueue(new Callback<Boutique>() {
            @Override
            public void onResponse(Call<Boutique> call, Response<Boutique> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentBoutique = response.body();

                    Nom.setText("Nom : " + currentBoutique.nom);
                    Email.setText("Email : " + currentBoutique.email);
                    Tel.setText("Tel : " + currentBoutique.telephone);
                    Id.setText("Identifiant : " + currentBoutique.login);
                    Mdp.setText("Mot de passe : " + "********"); // Mot de passe masqué
                    FormeJuridique.setText("Forme Juridique : " + currentBoutique.forme_juridique);
                    Siret.setText("Siret : " + currentBoutique.siret);
                    SiegeSocial.setText("Siege Social : " + currentBoutique.siege_social);
                    PaysEnregitrement.setText("Pays d' Enregitrement : " + currentBoutique.pays_enregistrement);
                } else {
                    Toast.makeText(BoutiqueProfilInfos.this, "Boutique introuvable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boutique> call, Throwable t) {
                Toast.makeText(BoutiqueProfilInfos.this, "Erreur serveur", Toast.LENGTH_SHORT).show();
            }
        });


        ImageButton BtnModifNom = findViewById(R.id.BtnModifNom);
        ImageButton BtnModifEmail = findViewById(R.id.BtnModifEmail);
        ImageButton BtnModifTel = findViewById(R.id.BtnModifTel);
        ImageButton BtnModifId = findViewById(R.id.BtnModifId);
        ImageButton BtnModifMdp = findViewById(R.id.BtnModifMdp);

        ImageButton BtnModifFormeJuridique = findViewById(R.id.BtnModifFormeJuridique);
        ImageButton BtnModifSiret = findViewById(R.id.BtnModifSiret);
        ImageButton BtnModifSiegeSocial = findViewById(R.id.BtnModifSiegeSocial);
        ImageButton BtnModifPaysEnregistrement = findViewById(R.id.BtnModifPaysEnregistrement);
        ImageButton BtnModifIban = findViewById(R.id.BtnModifIban);

        Button btnSupprimer = findViewById(R.id.btnSupprimer);


        BtnModifNom.setOnClickListener(b -> modif("nom", (id, val) -> {
            currentBoutique.nom = val;
            return updateBoutique(currentBoutique);
        }));
        BtnModifEmail.setOnClickListener(b -> modif("email", (id, val) -> {
            currentBoutique.email = val;
            return updateBoutique(currentBoutique);
        }));
        BtnModifTel.setOnClickListener(b -> modif("téléphone", (id, val) -> {
            currentBoutique.telephone = val;
            return updateBoutique(currentBoutique);
        }));
        BtnModifId.setOnClickListener(b -> modif("identifiant", (id, val) -> {
            currentBoutique.login = val;
            identifiant = val;
            return updateBoutique(currentBoutique);
        }));
        BtnModifMdp.setOnClickListener(b -> modif("mot de passe", (id, val) -> {
            currentBoutique.password = val;
            return updateBoutique(currentBoutique);
        }));
        BtnModifFormeJuridique.setOnClickListener(b -> modif("forme juridique", (id, val) -> {
            currentBoutique.forme_juridique = val;
            return updateBoutique(currentBoutique);
        }));
        BtnModifSiret.setOnClickListener(b -> modif("SIRET", (id, val) -> {
            currentBoutique.siret = val;
            return updateBoutique(currentBoutique);
        }));
        BtnModifSiegeSocial.setOnClickListener(b -> modif("siège social", (id, val) -> {
            currentBoutique.siege_social = val;
            return updateBoutique(currentBoutique);
        }));
        BtnModifPaysEnregistrement.setOnClickListener(b -> modif("pays d'enregistrement", (id, val) -> {
            currentBoutique.pays_enregistrement = val;
            return updateBoutique(currentBoutique);
        }));
        BtnModifIban.setOnClickListener(b -> modif("IBAN", (id, val) -> {
            currentBoutique.iban = val;
            return updateBoutique(currentBoutique);
        }));

        btnSupprimer.setOnClickListener(v -> {
            new AlertDialog.Builder(BoutiqueProfilInfos.this)
                    .setTitle("Confirmation")
                    .setMessage("Voulez-vous vraiment supprimer votre compte boutique ? Cette action est irréversible.")
                    .setPositiveButton("Supprimer", (dialog, which) -> supprimerCompte())
                    .setNegativeButton("Annuler", null)
                    .show();
        });



        // BOTTOM NAVIGATION BAR
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
    public interface BoutiqueModifier {
        boolean appliquer(String identifiant, String nouvelleValeur);
    }


    public void modif(String param, BoutiqueModifier fonction) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.modif, null);

        EditText editText = dialogView.findViewById(R.id.EditModif);
        TextView textView = dialogView.findViewById(R.id.TVmodif);

        textView.setText("Entrez un nouveau " + param + " :");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        builder.setPositiveButton(R.string.Confirmer, (dialog, id) -> {
            String nouvelleValeur = editText.getText().toString().trim();
            if (!nouvelleValeur.isEmpty()) {
                boolean success = fonction.appliquer(identifiant, nouvelleValeur);
                if (success) {
                    Toast.makeText(this, param + " modifié avec succès", Toast.LENGTH_SHORT).show();
                    recreate(); // Rafraîchir la vue
                } else {
                    Toast.makeText(this, "Échec de la modification", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Le champ ne peut pas être vide", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.Retour, null);
        builder.create().show();
    }

    private boolean updateBoutique(Boutique boutique) {
        final boolean[] success = {false};
        apiService.updateBoutique(boutique).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                success[0] = response.isSuccessful();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                success[0] = false;
            }
        });
        return true; // Idéalement, gérer la réponse asynchrone proprement
    }

    private void supprimerCompte() {
        apiService.deleteBoutique(identifiant).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BoutiqueProfilInfos.this, "Compte supprimé.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BoutiqueProfilInfos.this, Connexion.class); // ou MainActivity
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(BoutiqueProfilInfos.this, "Erreur lors de la suppression.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(BoutiqueProfilInfos.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
