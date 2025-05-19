package com.example.projet;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientParametres extends Activity {
    TextView Notifs;
    private List<String> selectedItems = new ArrayList<>();
    private List<String> allItems = Arrays.asList("push", "email", "sms");
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_params);
        
        String identifiant = getIntent().getStringExtra("id");

        TextView Langue = findViewById(R.id.rowLangue);
        Spinner spinnerLangue = findViewById(R.id.spinnerLangue);
        List<String> optionsLangue = Arrays.asList("Français", "Anglais");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, optionsLangue);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLangue.setAdapter(adapter);

        // Gérer le clic sur le TextView
        Langue.setOnClickListener(v -> {
            if(spinnerLangue.getVisibility() == View.VISIBLE) {
                spinnerLangue.setVisibility(View.GONE);
            } else {
                spinnerLangue.setVisibility(View.VISIBLE);
                spinnerLangue.performClick(); // Ouvre le dropdown
            }
        });

        // Gérer la sélection dans le Spinner
        spinnerLangue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                Langue.setText(selection);
                spinnerLangue.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerLangue.setVisibility(View.GONE);
            }
        });


        TextView Cookies = findViewById(R.id.rowCookies);
        Spinner spinnerCookies = findViewById(R.id.spinnerCookies);

        List<String> optionsCookies = Arrays.asList("", "Option 2", "Option 3", "Option 4");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, optionsCookies);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCookies.setAdapter(adapter2);

        // Gérer le clic sur le TextView
        Cookies.setOnClickListener(v -> {
            if(spinnerCookies.getVisibility() == View.VISIBLE) {
                spinnerCookies.setVisibility(View.GONE);
            } else {
                spinnerCookies.setVisibility(View.VISIBLE);
                spinnerCookies.performClick(); // Ouvre le dropdown
            }
        });

        // Gérer la sélection dans le Spinner
        spinnerCookies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                Cookies.setText(selection);
                spinnerCookies.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerCookies.setVisibility(View.GONE);
            }
        });

        Notifs = findViewById(R.id.rowNotifications);
        Notifs.setOnClickListener(v -> showCheckboxPopup());
        
        
        // TOP NAVIGATION BAR
        ImageView navCart = findViewById(R.id.cartIcon);

        navCart.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientPanier.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        // BOTTOM NAVIGATION BAR
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navFavorites = findViewById(R.id.navFavorites);
        LinearLayout navProfile2 = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientAccueil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navFavorites.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientFavoris.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navProfile2.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientProfilAcceuil.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

    }

    private void showCheckboxPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_checkbox_list, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                Notifs.getWidth(),
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
        popupWindow.showAsDropDown(Notifs);
    }

    private void updateSelectionText() {
        if (selectedItems.isEmpty()) {
            Notifs.setText("Aucune sélection");
        } else {
            Notifs.setText(TextUtils.join(", ", selectedItems));
        }
    }


}
