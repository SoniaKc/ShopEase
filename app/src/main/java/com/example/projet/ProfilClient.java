package com.example.projet;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.projet.bdd.ClientTable;

public class ProfilClient extends Activity {
    String identifiant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_profil_infos);

        ClientTable client = ClientTable.getInstance();

        Bundle info = getIntent().getExtras();
        assert info != null;
        this.identifiant = info.getString("id");

        // Initialisation des TextView
        TextView nom = findViewById(R.id.nom);
        TextView email = findViewById(R.id.email);
        TextView tel = findViewById(R.id.tel);
        TextView identifiantView = findViewById(R.id.identifiant);
        TextView mdp = findViewById(R.id.mdp);

        // Remplissage des données
        nom.setText("Prénom Nom : " + client.getNom(identifiant));
        email.setText("E-mail : " + client.getEmail(identifiant));
        tel.setText("Téléphone : " + client.getTel(identifiant));
        identifiantView.setText("Identifiant : " + identifiant);
        mdp.setText("Mot de Passe : " + "********"); // Masqué pour la sécurité

        // Initialisation des boutons de modification
        ImageButton btnModifNom = findViewById(R.id.BtnModifNom);
        ImageButton btnModifEmail = findViewById(R.id.BtnModifEmail);
        ImageButton btnModifTel = findViewById(R.id.BtnModifTel);
        ImageButton btnModifId = findViewById(R.id.BtnModifId);
        ImageButton btnModifMdp = findViewById(R.id.BtnModifMdp);

        // Définition des listeners
        btnModifNom.setOnClickListener(b -> {
            modif("nom complet", (id, val) -> { ClientTable.getInstance().setNom(id, val);
                return true;}); });

        btnModifEmail.setOnClickListener(b -> { modif("email", (id, val) -> { ClientTable.getInstance().setEmail(id, val);
                return true;}); });

        btnModifTel.setOnClickListener(b -> { modif("téléphone", (id, val) -> { ClientTable.getInstance().setTel(id, val);
                return true; }); });

        btnModifId.setOnClickListener(b -> {
            modif("identifiant", (id, val) -> {
                boolean success = ClientTable.getInstance().setLogin(id, val);
                if(success) { identifiant = val;}
                return success; });
        });

        btnModifMdp.setOnClickListener(b -> { modif("mot de passe", (id, val) -> { ClientTable.getInstance().setPassword(id, val);
                return true; }); });
    }

    public interface ClientModifier {
        boolean appliquer(String identifiant, String nouvelleValeur);
    }


    public void modif(String param, ClientModifier fonction) {
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
                    recreate(); // Rafraîchir l'activité pour afficher les nouvelles valeurs
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
}