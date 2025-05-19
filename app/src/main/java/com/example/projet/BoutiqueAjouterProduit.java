package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.example.projet.bdd.BoutiqueTable;
import com.example.projet.bdd.ProduitTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoutiqueAjouterProduit extends Activity {
    private List<String> selectedItems = new ArrayList<>();
    private List<String> allItems = Arrays.asList("Informatique", "Electronique", "Livre","Animaux","Jeux enfant", "Jeux de sociétés", "Papetterie");
    TextView TVcategories;
    String Categories ="";
    String identifiant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_ajouter_produit);

        BoutiqueTable shop = BoutiqueTable.getInstance();
        ProduitTable produits = ProduitTable.getInstance();

        identifiant = getIntent().getStringExtra("id");

        Button valider = findViewById(R.id.btnValider);
        Button supprimer = findViewById(R.id.btnSupprimer);

        EditText nomProduit = findViewById(R.id.nomProduit);
        TVcategories = findViewById(R.id.TVcategorie);
        EditText reductionProduit = findViewById(R.id.reductionProduit);
        EditText prixProduit = findViewById(R.id.prixProduit);
        EditText descriptionProduit = findViewById(R.id.descriptionProduit);

        TVcategories.setOnClickListener(v -> showCheckboxPopup());

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = nomProduit.getText().toString().trim();
                String reduction = reductionProduit.getText().toString().trim();
                String prix = prixProduit.getText().toString().trim();
                String description = descriptionProduit.getText().toString().trim();
                if(!nom.isEmpty()) {
                    boolean temp = produits.insertProduit(identifiant, nom, Categories, prix, description);
                    if(temp){
                        Toast.makeText(BoutiqueAjouterProduit.this, "nouveau produit mis en ligne", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), BoutiqueMesProduits.class);
                        intent.putExtra("log", identifiant);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(BoutiqueAjouterProduit.this, "échec lors de la mise en ligne du nouveau produit", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoutiqueMesProduits.class);
                intent.putExtra("log", identifiant);
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
