package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientParametres extends Activity {
    TextView Notifs;
    private List<String> selectedItems = new ArrayList<>();
    private List<String> allItems = Arrays.asList("push", "email", "sms");
    String identifiant;
    ApiService apiService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_params);

        apiService = ApiClient.getClient().create(ApiService.class);
        identifiant = getIntent().getStringExtra("id");

        chargerParametres();

        TextView Langue = findViewById(R.id.rowLangue);
        Spinner spinnerLangue = findViewById(R.id.spinnerLangue);
        List<String> optionsLangue = Arrays.asList("Français", "Anglais");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, optionsLangue);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLangue.setAdapter(adapter);

        Langue.setOnClickListener(v -> {
            if(spinnerLangue.getVisibility() == View.VISIBLE) {
                spinnerLangue.setVisibility(View.GONE);
            } else {
                spinnerLangue.setVisibility(View.VISIBLE);
                spinnerLangue.performClick();
            }
        });

        spinnerLangue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                Langue.setText(selection);
                spinnerLangue.setVisibility(View.GONE);
                enregistrerParametres();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerLangue.setVisibility(View.GONE);
            }
        });

        TextView Cookies = findViewById(R.id.rowCookies);
        Spinner spinnerCookies = findViewById(R.id.spinnerCookies);

        List<String> optionsCookies = Arrays.asList("Accepter", "Refuser");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, optionsCookies);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCookies.setAdapter(adapter2);

        Cookies.setOnClickListener(v -> {
            if(spinnerCookies.getVisibility() == View.VISIBLE) {
                spinnerCookies.setVisibility(View.GONE);
            } else {
                spinnerCookies.setVisibility(View.VISIBLE);
                spinnerCookies.performClick();
            }
        });

        spinnerCookies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                Cookies.setText(selection);
                spinnerCookies.setVisibility(View.GONE);
                enregistrerParametres();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerCookies.setVisibility(View.GONE);
            }
        });

        Notifs = findViewById(R.id.rowNotifications);
        Notifs.setOnClickListener(v -> showCheckboxPopup());

        // TOP NAV
        ImageView navCart = findViewById(R.id.cartIcon);
        navCart.setOnClickListener(v -> {
            Intent i = new Intent(this, ClientPanier.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

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

        ListView listView = popupView.findViewById(R.id.checkBoxListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_checkbox, R.id.textViewItem, allItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CheckBox checkBox = view.findViewById(R.id.checkBoxItem);
                checkBox.setChecked(selectedItems.contains(allItems.get(position)));

                // Ajout d'un écouteur direct sur la CheckBox
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    String item = allItems.get(position);
                    if (isChecked && !selectedItems.contains(item)) {
                        selectedItems.add(item);
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
            checkBox.toggle(); // Cela déclenchera le OnCheckedChangeListener
        });

        Button btnValider = popupView.findViewById(R.id.btnValider);
        btnValider.setOnClickListener(v -> {
            updateSelectionText();
            enregistrerParametres();
            popupWindow.dismiss();
        });

        popupWindow.showAsDropDown(Notifs);
    }

    private void updateSelectionText() {
        String prefix = "Notifs : ";
        if (selectedItems.isEmpty()) {
            Notifs.setText(prefix + "Aucune sélection");
        } else {
            Notifs.setText(prefix + TextUtils.join(", ", selectedItems));
        }
    }

    private void enregistrerParametres() {
        Parametre param = new Parametre();
        param.login = identifiant;

        // Nettoyer les préfixes "Langue :" et "Cookies :" si présents
        String langueText = ((TextView) findViewById(R.id.rowLangue)).getText().toString();
        param.langue = langueText.replace("Langue :", "").trim();

        String cookiesText = ((TextView) findViewById(R.id.rowCookies)).getText().toString();
        param.cookies = cookiesText.replace("Cookies :", "").trim();

        param.notifications = TextUtils.join(", ", selectedItems);
        param.type = "client";

        Log.d("PARAMS_SAVE", "Saving params: " + param.langue + " | " + param.cookies + " | " + param.notifications);

        Call<Void> call = apiService.updateParametre(param);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ClientParametres.this, "Paramètres mis à jour", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ClientParametres.this, "Paramètres non mis à jour", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClientParametres.this, "Erreur serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chargerParametres() {
        Call<Parametre> call = apiService.getParametre(identifiant, "client");
        call.enqueue(new Callback<Parametre>() {
            @Override
            public void onResponse(Call<Parametre> call, Response<Parametre> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Parametre param = response.body();
                    Log.d("PARAMS_LOAD", "Loaded params: " + param.langue + " | " + param.cookies + " | " + param.notifications);

                    TextView langueView = findViewById(R.id.rowLangue);
                    TextView cookiesView = findViewById(R.id.rowCookies);
                    Notifs = findViewById(R.id.rowNotifications);

                    // Ajout des préfixes seulement à l'affichage
                    langueView.setText("Langue :" + param.langue);
                    cookiesView.setText("Cookies :" + param.cookies);

                    // Nettoyage des notifications avant traitement
                    String notifications = param.notifications;
                    if (notifications != null && notifications.startsWith("Notifs :")) {
                        notifications = notifications.replace("Notifs :", "").trim();
                    }

                    selectedItems.clear();
                    if (notifications != null && !notifications.isEmpty() && !notifications.equals("Aucune sélection")) {
                        selectedItems.addAll(Arrays.asList(notifications.split("\\s*,\\s*")));
                    }

                    updateSelectionText();
                } else {
                    Toast.makeText(ClientParametres.this, "Impossible de charger les paramètres", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Parametre> call, Throwable t) {
                Toast.makeText(ClientParametres.this, "Erreur de chargement : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
