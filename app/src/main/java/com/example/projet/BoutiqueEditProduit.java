package com.example.projet;

import android.app.Activity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoutiqueEditProduit extends Activity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private List<String> selectedItems = new ArrayList<>();
    private List<String> allItems = Arrays.asList("Informatique", "Electronique", "Livre","Animaux","Jeux enfant", "Jeux de sociétés", "Papetterie");
    EditText reductionProduit, prixProduit, descriptionProduit;
    TextView TVcategories, nomProduit;
    ImageView imageProduit;
    String Categories ="";
    String identifiant, nom_Produit;
    ApiService apiService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_edit_produit);

        identifiant = getIntent().getStringExtra("id");
        nom_Produit = getIntent().getStringExtra("nomProduit");

        apiService = ApiClient.getClient().create(ApiService.class);

        ImageView photoProfil = findViewById(R.id.profilePhoto);
        ImageHandler.getBoutiqueAndHandleAllImages(apiService, identifiant, photoProfil);

        Button valider = findViewById(R.id.btnValider);
        Button supprimer = findViewById(R.id.btnSupprimer);
        ImageButton btnModifPhoto = findViewById(R.id.BtnModifPhoto);

        nomProduit = findViewById(R.id.nomProduit);
        TVcategories = findViewById(R.id.TVcategorie);
        reductionProduit = findViewById(R.id.reductionProduit);
        prixProduit = findViewById(R.id.prixProduit);
        descriptionProduit = findViewById(R.id.descriptionProduit);
        imageProduit = findViewById(R.id.imageProduit);

        loadProduitDetails();

        btnModifPhoto.setOnClickListener(v -> openImageChooser());

        TVcategories.setOnClickListener(v -> showCheckboxPopup());

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = nomProduit.getText().toString().trim();
                String reduction = reductionProduit.getText().toString().trim();
                String prix = prixProduit.getText().toString().trim();
                String description = descriptionProduit.getText().toString().trim();
                Drawable drawable = imageProduit.getDrawable();

                Bitmap bitmapImage = ((BitmapDrawable) drawable).getBitmap();
                Bitmap resizedBitmap = resizeBitmap(bitmapImage, 800, 800);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);

                if (!nom.isEmpty()) {
                    Produit produit = new Produit();
                    produit.login_boutique = identifiant;
                    produit.nom = nom;
                    produit.categories = Categories;
                    produit.reduction = reduction;
                    produit.prix = prix;
                    produit.description = description;
                    produit.image = Base64.encodeToString(bitmapToByteArray(resizedBitmap), Base64.NO_WRAP);


                    apiService = ApiClient.getClient().create(ApiService.class);
                    Call<Void> call = apiService.updateProduit(produit);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(getApplicationContext(), BoutiqueMesProduits.class);
                                intent.putExtra("id", identifiant);
                                startActivity(intent);
                            } else {
                                Toast.makeText(BoutiqueEditProduit.this, "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
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
                String nom = nomProduit.getText().toString().trim();

                apiService = ApiClient.getClient().create(ApiService.class);
                Call<Void> call = apiService.deleteProduit(identifiant,nom);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), BoutiqueMesProduits.class);
                            intent.putExtra("id", identifiant);
                            startActivity(intent);
                        } else {
                            Toast.makeText(BoutiqueEditProduit.this, "Erreur lors de la supression", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(BoutiqueEditProduit.this, "Échec réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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

    private void loadProduitDetails() {
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
                    ImageHandler.handleProduitImages(produit.image,imageProduit);

                    selectedItems.clear();
                    if (produit.categories != null && !produit.categories.trim().isEmpty()) {
                        selectedItems.addAll(Arrays.asList(produit.categories.split("\\s*,\\s*")));
                    }
                    TVcategories.setText("Catégories : " + produit.categories);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                ImageView imageProduit = findViewById(R.id.imageProduit);
                Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                imageProduit.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float ratioBitmap = (float) width / (float) height;
        float ratioMax = (float) maxWidth / (float) maxHeight;

        int finalWidth = maxWidth;
        int finalHeight = maxHeight;

        if (ratioMax > ratioBitmap) {
            finalWidth = (int) ((float) maxHeight * ratioBitmap);
        } else {
            finalHeight = (int) ((float) maxWidth / ratioBitmap);
        }

        return Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true);
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Sélectionnez une image"), PICK_IMAGE_REQUEST);
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
                TextView textView = view.findViewById(R.id.textViewItem);

                String item = getItem(position);
                checkBox.setChecked(selectedItems.contains(item));
                textView.setText(item);

                checkBox.setOnClickListener(v -> {
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
            checkBox.setChecked(!checkBox.isChecked());

            String item = allItems.get(position);
            if (checkBox.isChecked() && !selectedItems.contains(item)) {
                selectedItems.add(item);
            } else {
                selectedItems.remove(item);
            }
        });
        Button btnValider = popupView.findViewById(R.id.btnValider);
        btnValider.setOnClickListener(v -> {
            updateSelectionText();
            popupWindow.dismiss();
        });

        popupWindow.showAsDropDown(TVcategories);
    }

    private void updateSelectionText() {
        if (selectedItems.isEmpty()) {
            TVcategories.setText("Catégories : Aucune sélection");
        } else {
            Categories = TextUtils.join(", ", selectedItems);
            TVcategories.setText("Catégories : " + Categories);
        }
    }
}
