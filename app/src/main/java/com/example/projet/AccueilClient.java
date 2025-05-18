package com.example.projet;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AccueilClient extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_layout_accueil);


        // TOP NAVIGATION BAR
        ImageView navCart = findViewById(R.id.cartIcon);
        ImageView navProfile = findViewById(R.id.profilePhoto);

        navCart.setOnClickListener(v -> {
            // Aller au panier
        });

        navProfile.setOnClickListener(v -> {
            // Aller au profil
        });

        // BOTTOM NAVIGATION BAR
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navFavorites = findViewById(R.id.navFavorites);
        LinearLayout navProfile2 = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            // Aller à l’accueil
        });

        navFavorites.setOnClickListener(v -> {
            // Aller aux favoris
        });

        navProfile2.setOnClickListener(v -> {
            // Aller au profil
        });
    }
}
