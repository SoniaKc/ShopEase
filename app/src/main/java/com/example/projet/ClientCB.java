package com.example.projet;

import android.app.Activity;
import android.os.Bundle;

public class ClientCB extends Activity {
    String identifiant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_moyens_paiement);

        Bundle info = getIntent().getExtras();
        assert info != null;
        this.identifiant = info.getString("id");
    }
}
