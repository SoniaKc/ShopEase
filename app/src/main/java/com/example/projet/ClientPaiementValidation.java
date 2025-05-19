package com.example.projet;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.projet.bdd.ClientTable;

public class ClientPaiementValidation extends Activity {
    String identifiant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_paiement_validation);

        ClientTable client = ClientTable.getInstance();

        Bundle info = getIntent().getExtras();
        assert info != null;
        this.identifiant = info.getString("id");

        TextView nomPrenom = findViewById(R.id.nomPrenom);
        nomPrenom.setText(client.getNom(identifiant) + " " + client.getPrenom(identifiant));

    }
}
