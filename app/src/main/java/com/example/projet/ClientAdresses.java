package com.example.projet;

import android.app.Activity;
import android.os.Bundle;

public class ClientAdresses extends Activity {
    String identifiant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_adresses);

        identifiant = getIntent().getStringExtra("id");
    }
}
