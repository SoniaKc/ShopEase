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

public class BoutiqueParametres extends Activity {
    TextView Notifs;
    private List<String> selectedItems = new ArrayList<>();
    private List<String> allItems = Arrays.asList("push", "email", "sms");
    String identifiant;
    ApiService apiService;

    Spinner spinnerLangue;
    Spinner spinnerCookies;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_params);

        apiService = ApiClient.getClient().create(ApiService.class);
        identifiant = getIntent().getStringExtra("id");

        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageHandler.getBoutiqueAndHandleAllImages(apiService, identifiant, photoProfil);

        Notifs = findViewById(R.id.rowNotifications);
        Notifs.setOnClickListener(v -> showCheckboxPopup());

        spinnerLangue = findViewById(R.id.spinnerLangue);
        ArrayAdapter<String> adapterLangue = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Français", "Anglais"});
        adapterLangue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLangue.setAdapter(adapterLangue);

        spinnerLangue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isFirst = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirst) {
                    isFirst = false;
                    return;
                }
                enregistrerParametres();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerCookies = findViewById(R.id.spinnerCookies);
        ArrayAdapter<String> adapterCookies = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Accepter", "Refuser"});
        adapterCookies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCookies.setAdapter(adapterCookies);

        spinnerCookies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isFirst = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirst) {
                    isFirst = false;
                    return;
                }
                enregistrerParametres();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        chargerParametres();

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navVentes = findViewById(R.id.navVentes);
        LinearLayout navProfile2 = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueMesProduits.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navVentes.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueHistoriqueVentes.class);
            i.putExtra("id", identifiant);
            startActivity(i);
        });

        navProfile2.setOnClickListener(v -> {
            Intent i = new Intent(this, BoutiqueProfilAccueil.class);
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
            checkBox.toggle();
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

        param.langue = spinnerLangue.getSelectedItem().toString();
        param.cookies = spinnerCookies.getSelectedItem().toString();
        param.notifications = TextUtils.join(", ", selectedItems);
        param.type = "boutique";

        Call<Void> call = apiService.updateParametre(param);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                } else {
                    Toast.makeText(BoutiqueParametres.this, "Paramètres non mis à jour", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(BoutiqueParametres.this, "Erreur serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chargerParametres() {
        Call<Parametre> call = apiService.getParametre(identifiant, "boutique");
        call.enqueue(new Callback<Parametre>() {
            @Override
            public void onResponse(Call<Parametre> call, Response<Parametre> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Parametre param = response.body();

                    if (param.langue != null) {
                        int posLangue = ((ArrayAdapter<String>)spinnerLangue.getAdapter()).getPosition(param.langue);
                        if (posLangue >= 0) spinnerLangue.setSelection(posLangue);
                    }

                    if (param.cookies != null) {
                        int posCookies = ((ArrayAdapter<String>)spinnerCookies.getAdapter()).getPosition(param.cookies);
                        if (posCookies >= 0) spinnerCookies.setSelection(posCookies);
                    }

                    String notifications = param.notifications;
                    if (notifications != null && notifications.startsWith("Notifs :")) {
                        notifications = notifications.replace("Notifs :", "").trim();
                    }

                    selectedItems.clear();
                    if (notifications != null && !notifications.isEmpty() && !notifications.equals("Aucune sélection")) {
                        selectedItems.addAll(Arrays.asList(notifications.split("\\s*,\\s*")));
                    }

                    updateSelectionText();
                }
            }
            @Override
            public void onFailure(Call<Parametre> call, Throwable t) {
                Toast.makeText(BoutiqueParametres.this, "Erreur de chargement : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
