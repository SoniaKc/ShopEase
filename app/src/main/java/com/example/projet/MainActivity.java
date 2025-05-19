package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.projet.bdd.DBHelper;

public class MainActivity extends Activity {
    Button btnInscClient;
    Button btnInscBoutique;
    Button btnConnexionClient;
    Button btnConnexionBoutique;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        DBHelper dbHelper = new DBHelper(getApplicationContext(), null);
        if(dbHelper.getWritableDatabase().isOpen()){
            Log.d("SONIAAAAAAA","OKOK");
        }


        btnInscClient = findViewById(R.id.btnClientRegister);
        btnInscBoutique = findViewById(R.id.btnShopRegister);
        btnConnexionClient = findViewById(R.id.btnClientLogin);
        btnConnexionBoutique = findViewById(R.id.btnShopLogin);

        btnInscClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ClientInscription.class);
                startActivity(i);
            }
        }
        );

        btnInscBoutique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, BoutiqueInscription.class);
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
