package com.example.projet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet.bdd.AdressesTable;
import com.example.projet.bdd.PayementsTable;

import java.util.ArrayList;
import java.util.List;

public class ClientPaiementPanier extends Activity {
    private String identifiant;
    private String selectedCard;
    private String selectedAddress = "Adresse 1 : 49 rue de l'Olivette, 34089 St Jacques, France";
    private PayementsTable payementsTable;
    private AdressesTable adressesTable;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_paiement_panier);

        // Initialisation des données
        Bundle info = getIntent().getExtras();
        if (info != null) {
            this.identifiant = info.getString("id");
        }

        payementsTable = PayementsTable.getInstance();
        adressesTable = AdressesTable.getInstance();

        selectedCard = "Carte 1 : " + getMaskedCardNumber("4111111111111111"); // Valeur par défaut

        // Initialisation des vues
        Button carte = findViewById(R.id.carte);
        Button adresse = findViewById(R.id.adresse);
        Button validerPayer = findViewById(R.id.validerPayer);
        EditText code = findViewById(R.id.code);
        TextView TVcarte = findViewById(R.id.TVcarte);
        TextView TVadresse = findViewById(R.id.TVadresse);

        TVcarte.setText(selectedCard);
        TVadresse.setText(selectedAddress);

        // Gestion du clic sur "Choisir une autre carte"
        carte.setOnClickListener(b -> showCardSelectionDialog());
        adresse.setOnClickListener(v -> showAddressSelectionDialog());

        // Gestion du clic sur "Valider et Payer"
        validerPayer.setOnClickListener(b -> {
            String textCode = code.getText().toString().trim();
            if (isValidPromoCode(textCode)) {
                processPayment();
            } else {
                Toast.makeText(this, "Code promotion invalide", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCardSelectionDialog() {
        // Récupérer les cartes de la base de données
        List<String> cards = getCardsFromDatabase();

        if (cards.isEmpty()) {
            Toast.makeText(this, "Aucune carte enregistrée", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créer un dialogue personnalisé
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisissez une carte");

        // Créer la vue pour la liste
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_card_selection, null);
        builder.setView(dialogView);

        ListView listView = dialogView.findViewById(R.id.listViewCards);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, cards);
        listView.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        // Gérer la sélection
        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedCard = cards.get(position);
            TextView TVcarte = findViewById(R.id.TVcarte);
            TVcarte.setText(selectedCard);
            dialog.dismiss();
        });

        dialog.show();
    }

    private List<String> getCardsFromDatabase() {
        List<String> cards = new ArrayList<>();

        // Récupérer les noms de cartes depuis la base de données
        String cardsList = payementsTable.getListeNomsCartes(identifiant);
        if (cardsList != null && !cardsList.isEmpty()) {
            String[] cardNames = cardsList.split(", ");
            for (String cardName : cardNames) {
                // Formater l'affichage de la carte
                String cardNumber = payementsTable.getCvc(identifiant, cardName); // À adapter selon votre structure
                String maskedCard = getMaskedCardNumber(cardNumber);
                String cardHolder = payementsTable.getNomPersonneCarte(identifiant, cardName);
                String expDate = payementsTable.getDateExpiration(identifiant, cardName);

                cards.add(cardName + " : " + maskedCard + "\n" +
                        cardHolder + " - " + expDate);
            }
        }
        return cards;
    }

    private String getMaskedCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    private boolean isValidPromoCode(String code) {
        return code.equalsIgnoreCase("Sonia") || code.isEmpty();
    }

    private void processPayment() {
        // Implémentez votre logique de paiement ici
        Toast.makeText(this, "Paiement effectué avec la carte : " + selectedCard, Toast.LENGTH_SHORT).show();

        // Redirection vers l'activité de confirmation
        Intent intent = new Intent(this, ClientPaiementValidation.class);
        intent.putExtra("id", identifiant);
        startActivity(intent);
    }












    private void showAddressSelectionDialog() {
        List<String> adresses = getAddressesFromDatabase();

        if (adresses.isEmpty()) {
            Toast.makeText(this, "Aucune adresse enregistrée", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisissez une adresse");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_card_selection, null); // Réutilise le même layout
        builder.setView(dialogView);

        ListView listView = dialogView.findViewById(R.id.listViewCards); // Même ID, réutilisé
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, adresses);
        listView.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedAddress = adresses.get(position);
            TextView TVadresse = findViewById(R.id.TVadresse);
            TVadresse.setText(selectedAddress);
            dialog.dismiss();
        });

        dialog.show();
    }
    private List<String> getAddressesFromDatabase() {
        List<String> adresses = new ArrayList<>();

        String nomsAdresses = adressesTable.getListeNomsAdresses(identifiant);
        if (nomsAdresses != null && !nomsAdresses.isEmpty()) {
            String[] noms = nomsAdresses.split(", ");
            for (String nom : noms) {
                String numero = adressesTable.getNumero(identifiant, nom);
                String rue = adressesTable.getNomRue(identifiant, nom);
                String cp = adressesTable.getCodePostal(identifiant, nom);
                String ville = adressesTable.getVille(identifiant, nom);
                String pays = adressesTable.getPays(identifiant, nom);

                String adresseFormatee = nom + " : " + numero + " " + rue + ", " + cp + " " + ville + ", " + pays;
                adresses.add(adresseFormatee);
            }
        }

        return adresses;
    }

}