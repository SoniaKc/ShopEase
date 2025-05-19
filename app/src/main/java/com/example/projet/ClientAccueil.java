package com.example.projet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ClientAccueil extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_layout_accueil);

        String Stridentifiant = getIntent().getStringExtra("id");

        // TOP NAVIGATION BAR
        ImageView navCart = findViewById(R.id.cartIcon);

        navCart.setOnClickListener(v -> {
            Intent i = new Intent(ClientAccueil.this, ClientPanier.class);
            i.putExtra("id", Stridentifiant);
            startActivity(i);
        });


        // BOTTOM NAVIGATION BAR
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navFavorites = findViewById(R.id.navFavorites);
        LinearLayout navProfile2 = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(ClientAccueil.this, ClientAccueil.class);
            i.putExtra("id", Stridentifiant);
            startActivity(i);
        });

        navFavorites.setOnClickListener(v -> {
            Intent i = new Intent(ClientAccueil.this, ClientFavoris.class);
            i.putExtra("id", Stridentifiant);
            startActivity(i);
        });

        navProfile2.setOnClickListener(v -> {
            Intent i = new Intent(ClientAccueil.this, ClientProfilAcceuil.class);
            i.putExtra("id", Stridentifiant);
            startActivity(i);
        });
    }
}
