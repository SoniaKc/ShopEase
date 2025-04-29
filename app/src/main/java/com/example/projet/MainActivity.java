package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    Button btnInscClient;
    Button btnInscBoutique;
    Button btnConnexionClient;
    Button btnConnexionBoutique;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        btnInscClient = findViewById(R.id.btnClientRegister);
        btnInscBoutique = findViewById(R.id.btnShopRegister);
        btnConnexionClient = findViewById(R.id.btnClientLogin);
        btnConnexionBoutique = findViewById(R.id.btnShopLogin);

        btnInscClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, InscriptionClient.class);
                startActivity(i);
            }
        }
        );

        btnInscBoutique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, InscriptionBoutique.class);
                startActivity(i);
            }
        }
        );

        btnConnexionClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Connexion.class);
                i.putExtra("type", "client");
                startActivity(i);
            }
        }
        );

        btnConnexionBoutique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Connexion.class);
                i.putExtra("type", "boutique");
                startActivity(i);
            }
        });
    }
}
