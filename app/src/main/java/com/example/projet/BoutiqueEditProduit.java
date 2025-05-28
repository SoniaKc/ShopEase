package com.example.projet;

import android.app.Activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoutiqueEditProduit extends Activity {
    private List<String> selectedItems = new ArrayList<>();
    private List<String> allItems = Arrays.asList("Informatique", "Electronique", "Livre","Animaux","Jeux enfant", "Jeux de sociétés", "Papetterie");
    EditText nomProduit, reductionProduit, prixProduit, descriptionProduit;
    TextView TVcategories;
    String Categories ="";
    String identifiant, nom_Produit;
    ApiService apiService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_ajouter_produit);

        identifiant = getIntent().getStringExtra("id");
        nom_Produit = getIntent().getStringExtra("nomProduit");

        apiService = ApiClient.getClient().create(ApiService.class);

        Button valider = findViewById(R.id.btnValider);
        Button supprimer = findViewById(R.id.btnSupprimer);

        nomProduit = findViewById(R.id.nomProduit);
        TVcategories = findViewById(R.id.TVcategorie);
        reductionProduit = findViewById(R.id.reductionProduit);
        prixProduit = findViewById(R.id.prixProduit);
        descriptionProduit = findViewById(R.id.descriptionProduit);

        loadProduitDetails();

        TVcategories.setOnClickListener(v -> showCheckboxPopup());

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

                    apiService = ApiClient.getClient().create(ApiService.class);
                    Call<Void> call = apiService.addProduit(produit);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(BoutiqueEditProduit.this, "Produit ajouté avec succès", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), BoutiqueMesProduits.class);
                                intent.putExtra("id", identifiant);
                                startActivity(intent);
                            } else {
                                Toast.makeText(BoutiqueEditProduit.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(BoutiqueEditProduit.this, "Échec réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(BoutiqueEditProduit.this, "Nom requis", Toast.LENGTH_SHORT).show();
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



        // BOTTOM NAVIGATION BAR
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

    private void loadProduitDetails() {

        Log.e("LOAD PRODUIT DETAILS", "ID = "+identifiant);
        Log.e("LOAD PRODUIT DETAILS", "NOM = "+nom_Produit);

        Call<Produit> call = apiService.getProduit(identifiant, nom_Produit);
        call.enqueue(new Callback<Produit>() {
            @Override
            public void onResponse(Call<Produit> call, Response<Produit> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Produit produit = response.body();
                    nomProduit.setText(produit.nom);
                    reductionProduit.setText(produit.reduction);
                    prixProduit.setText(produit.prix);
                    descriptionProduit.setText(produit.description);
                    TVcategories.setText(produit.categories);
                } else {
                    Toast.makeText(BoutiqueEditProduit.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Produit> call, Throwable t) {
                Toast.makeText(BoutiqueEditProduit.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCheckboxPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_checkbox_list, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                TVcategories.getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Configuration de la ListView
        ListView listView = popupView.findViewById(R.id.checkBoxListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_checkbox, R.id.textViewItem, allItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CheckBox checkBox = view.findViewById(R.id.checkBoxItem);
                checkBox.setChecked(selectedItems.contains(allItems.get(position)));
                return view;
            }
        };
        listView.setAdapter(adapter);

        // Gestion des clics sur les items
        listView.setOnItemClickListener((parent, view, position, id) -> {
            CheckBox checkBox = view.findViewById(R.id.checkBoxItem);
            checkBox.setChecked(!checkBox.isChecked());

            String item = allItems.get(position);
            if (checkBox.isChecked() && !selectedItems.contains(item)) {
                selectedItems.add(item);
            } else {
                selectedItems.remove(item);
            }
        });
        // Bouton Valider
        Button btnValider = popupView.findViewById(R.id.btnValider);
        btnValider.setOnClickListener(v -> {
            updateSelectionText();
            popupWindow.dismiss();
        });

        // Afficher le popup
        popupWindow.showAsDropDown(TVcategories);
    }

    private void updateSelectionText() {
        if (selectedItems.isEmpty()) {
            TVcategories.setText("Aucune sélection");
        } else {
            Categories = TextUtils.join(", ", selectedItems);
            TVcategories.setText(TextUtils.join(", ", selectedItems));
        }
    }
}
