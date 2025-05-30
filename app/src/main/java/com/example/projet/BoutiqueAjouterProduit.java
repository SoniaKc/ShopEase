package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet.Produit;
import com.example.projet.ApiClient;
import com.example.projet.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoutiqueAjouterProduit extends Activity {
    private List<String> selectedItems = new ArrayList<>();
    private List<String> allItems = Arrays.asList("Informatique", "Electronique", "Livre","Animaux","Jeux enfant", "Jeux de sociétés", "Papetterie");
    LinearLayout categorie;
    TextView TVcategorie;
    String Categories ="";
    String identifiant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_ajouter_produit);

        identifiant = getIntent().getStringExtra("id");
        Log.e("id", identifiant);


        Button valider = findViewById(R.id.btnValider);
        Button supprimer = findViewById(R.id.btnSupprimer);

        EditText nomProduit = findViewById(R.id.nomProduit);
        categorie = findViewById(R.id.categorie);
        TVcategorie = findViewById(R.id.TVcategorie);
        EditText reductionProduit = findViewById(R.id.reductionProduit);
        EditText prixProduit = findViewById(R.id.prixProduit);
        EditText descriptionProduit = findViewById(R.id.descriptionProduit);

        categorie.setOnClickListener(v -> showCheckboxPopup());

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = nomProduit.getText().toString().trim();
                String reduction = reductionProduit.getText().toString().trim();
                String prix = prixProduit.getText().toString().trim();
                String description = descriptionProduit.getText().toString().trim();

                if (!nom.isEmpty()) {
                    Produit produit = new Produit();
                    produit.login_boutique = identifiant;
                    produit.nom = nom;
                    produit.categories = Categories;
                    produit.reduction = reduction;
                    produit.prix = prix;
                    produit.description = description;

                    ApiService apiService = ApiClient.getClient().create(ApiService.class);
                    Call<Void> call = apiService.addProduit(produit);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(BoutiqueAjouterProduit.this, "Produit ajouté", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), BoutiqueMesProduits.class);
                                intent.putExtra("id", identifiant);
                                startActivity(intent);
                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    Toast.makeText(BoutiqueAjouterProduit.this, "Erreur: " + errorBody, Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(BoutiqueAjouterProduit.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(BoutiqueAjouterProduit.this, "Nom requis", Toast.LENGTH_SHORT).show();
                }
            }
        });

        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoutiqueMesProduits.class);
                intent.putExtra("id", identifiant);
                startActivity(intent);
            }
        });

        setupBottomNavigation();
    }

    private void showCheckboxPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_checkbox_list2, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        ListView listView = popupView.findViewById(R.id.checkBoxListView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_checkbox, R.id.textViewItem, allItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CheckBox checkBox = view.findViewById(R.id.checkBoxItem);
                checkBox.setChecked(selectedItems.contains(allItems.get(position)));

                checkBox.setOnClickListener(v -> {
                    String item = allItems.get(position);
                    if (checkBox.isChecked()) {
                        if (!selectedItems.contains(item)) selectedItems.add(item);
                    } else {
                        selectedItems.remove(item);
                    }
                });

                return view;
            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            CheckBox checkBox = view.findViewById(R.id.checkBoxItem);
            checkBox.toggle(); // Active le OnClickListener du checkbox
        });

        Button btnValider = popupView.findViewById(R.id.btnValider);
        btnValider.setOnClickListener(v -> {
            updateSelectionText();
            popupWindow.dismiss();
        });

        popupWindow.showAsDropDown(TVcategorie);
    }

    private void updateSelectionText() {
        if (selectedItems.isEmpty()) {
            TVcategorie.setText("Catégories : Aucune sélection");
        } else {
            Categories = TextUtils.join(", ", selectedItems);
            TVcategorie.setText("Catégories : " + Categories);
        }
    }

    private void setupBottomNavigation() {
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navVentes = findViewById(R.id.navVentes);
        LinearLayout navProfile2 = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueProfilAccueil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navVentes.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueHistoriqueVentes.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navProfile2.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueProfilInfos.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });
    }


}
