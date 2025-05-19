package com.example.projet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.projet.bdd.BoutiqueTable;

// Pb ici sur comment récupérer le nouveau identifiant modifié

public class BoutiqueProfilInfos extends Activity {
    String identifiant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_profil_infos);

        BoutiqueTable shop = BoutiqueTable.getInstance();

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

        Nom.setText(shop.getNom(identifiant));
        Email.setText(shop.getEmail(identifiant));
        Tel.setText(shop.getTel(identifiant));
        Id.setText(identifiant);
        Mdp.setText(shop.getMdp(identifiant));

        FormeJuridique.setText(shop.getFormeJuridique(identifiant));
        Siret.setText(shop.getSiret(identifiant));
        SiegeSocial.setText(shop.getSiegeSocial(identifiant));
        PaysEnregitrement.setText(shop.getPaysEnregistrement(identifiant));


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


        BtnModifNom.setOnClickListener(b -> { modif("nom", (id, val) -> BoutiqueTable.getInstance().setNom(id, val)); });
        BtnModifEmail.setOnClickListener(b -> { modif("email", (id, val) -> BoutiqueTable.getInstance().setEmail(id, val)); });
        BtnModifTel.setOnClickListener(b -> { modif("téléphone", (id, val) -> BoutiqueTable.getInstance().setTel(id, val)); });
        BtnModifId.setOnClickListener(b -> { modif("identifiant", (id, val) -> BoutiqueTable.getInstance().setLogin(id, val));});
        BtnModifMdp.setOnClickListener(b -> { modif("mot de passe", (id, val) -> BoutiqueTable.getInstance().setPassword(id, val)); });

        BtnModifFormeJuridique.setOnClickListener(b -> { modif("forme juridique", (id, val) -> BoutiqueTable.getInstance().setFormeJuridique(id, val)); });
        BtnModifSiret.setOnClickListener(b -> { modif("SIRET", (id, val) -> BoutiqueTable.getInstance().setSiret(id, val)); });
        BtnModifSiegeSocial.setOnClickListener(b -> { modif("siège social", (id, val) -> BoutiqueTable.getInstance().setSiegeSocial(id, val));});
        BtnModifPaysEnregistrement.setOnClickListener(b -> { modif("pays d'enregistrement", (id, val) -> BoutiqueTable.getInstance().setPaysEnregistrement(id, val));});
        BtnModifIban.setOnClickListener(b -> { modif("IBAN", (id, val) -> BoutiqueTable.getInstance().setIban(id, val)); });



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
        BoutiqueTable shop = BoutiqueTable.getInstance();

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.modif, null);

        EditText editNom = dialogView.findViewById(R.id.EditModif);
        TextView TVmodif = dialogView.findViewById(R.id.TVmodif);

        TVmodif.setText("Entrez un nouveau " + param + " pour votre boutique :");

        AlertDialog.Builder builder = new AlertDialog.Builder(BoutiqueProfilInfos.this);
        builder.setView(dialogView);

        builder.setPositiveButton(R.string.Confirmer, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String nouveauParam = editNom.getText().toString().trim();
                if (!nouveauParam.isEmpty()) {
                    boolean success = fonction.appliquer(identifiant, nouveauParam);
                    if (success) {
                        Toast.makeText(BoutiqueProfilInfos.this, param + " modifié.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BoutiqueProfilInfos.this, "Échec lors de la modification du " + param + ".", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BoutiqueProfilInfos.this, param + " ne peut pas être vide.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(R.string.Retour, null);
        builder.create().show();


    }

}
