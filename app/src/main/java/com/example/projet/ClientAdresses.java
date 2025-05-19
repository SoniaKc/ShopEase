package com.example.projet;

import android.app.Activity;
import android.os.Bundle;

public class ClientAdresses extends Activity {
    String identifiant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_adresses);

        Bundle info = getIntent().getExtras();
        assert info != null;
        this.identifiant = info.getString("id");
    }
}
