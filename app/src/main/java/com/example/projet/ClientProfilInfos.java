package com.example.projet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientProfilInfos extends Activity {
    private static final String PROFILE_IMAGE_KEY = "profile_image_";
    String identifiant;
    ApiService apiService;
    Client currentClient;
    ImageView profilePhoto;
    private static final int PICK_IMAGE_REQUEST = 1;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_profil_infos);

        sharedPreferences = getSharedPreferences("profile_prefs", MODE_PRIVATE);
        apiService = ApiClient.getClient().create(ApiService.class);
        identifiant = getIntent().getStringExtra("id");

        // Initialisation des vues
        TextView nom = findViewById(R.id.nom);
        TextView prenom = findViewById(R.id.prenom);
        TextView email = findViewById(R.id.email);
        TextView tel = findViewById(R.id.tel);
        TextView identifiantView = findViewById(R.id.identifiant);
        TextView mdp = findViewById(R.id.mdp);
        profilePhoto = findViewById(R.id.photo);

        // Charger la photo de profil si elle existe
        loadProfileImage();

        // Appel API pour récupérer les données client
        apiService.getClient(identifiant).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentClient = response.body();

                    nom.setText("Nom : " + currentClient.nom);
                    prenom.setText("Prénom : " + currentClient.prenom);
                    email.setText("E-mail : " + currentClient.email);
                    tel.setText("Téléphone : " + currentClient.telephone);
                    identifiantView.setText("Identifiant : " + currentClient.login);
                    mdp.setText("Mot de Passe : ********");

                } else {
                    Toast.makeText(ClientProfilInfos.this, "Client introuvable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(ClientProfilInfos.this, "Erreur serveur", Toast.LENGTH_SHORT).show();
            }
        });

        // Boutons de modification
        ImageButton btnModifNom = findViewById(R.id.BtnModifNom);
        ImageButton btnModifPrenom = findViewById(R.id.BtnModifPrenom);
        ImageButton btnModifEmail = findViewById(R.id.BtnModifEmail);
        ImageButton btnModifTel = findViewById(R.id.BtnModifTel);
        ImageButton btnModifMdp = findViewById(R.id.BtnModifMdp);
        Button btnSupprimer = findViewById(R.id.btnSupprimer);
        ImageButton btnModifPhoto = findViewById(R.id.BtnModifPhoto);

        btnModifPhoto.setOnClickListener(v -> openImageChooser());

        btnModifNom.setOnClickListener(b -> modif("nom", (id, val) -> {
            currentClient.nom = val;
            updateClient(currentClient, () -> recreate());
            return true;
        }));

        btnModifPrenom.setOnClickListener(b -> modif("prenom", (id, val) -> {
            currentClient.prenom = val;
            updateClient(currentClient, () -> recreate());
            return true;
        }));

        btnModifEmail.setOnClickListener(b -> modif("email", (id, val) -> {
            currentClient.email = val;
            updateClient(currentClient, () -> recreate());
            return true;
        }));

        btnModifTel.setOnClickListener(b -> modif("téléphone", (id, val) -> {
            currentClient.telephone = val;
            updateClient(currentClient, () -> recreate());
            return true;
        }));


        btnModifMdp.setOnClickListener(b -> modif("mot de passe", (id, val) -> {
            currentClient.password = val;
            updateClient(currentClient, () -> recreate());
            return true;
        }));

        btnSupprimer.setOnClickListener(v -> {
            new AlertDialog.Builder(ClientProfilInfos.this)
                    .setTitle("Confirmation")
                    .setMessage("Voulez-vous vraiment supprimer votre compte client ? Cette action est irréversible.")
                    .setPositiveButton("Supprimer", (dialog, which) -> deleteClient(identifiant))
                    .setNegativeButton("Annuler", null)
                    .show();
        });

        // Navigation
        setupNavigation();
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Sélectionnez une image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri == null) {
                Toast.makeText(this, "Erreur: URI d'image invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                Bitmap resizedBitmap = resizeBitmap(bitmap, 800, 800);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                Log.e("IMAGE", imageBytes.toString());

                currentClient.image = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                profilePhoto.setImageBitmap(resizedBitmap);
                saveProfileImage(resizedBitmap);

                updateClient(currentClient, null);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void saveProfileImage(Bitmap bitmap) {
        try {
            File file = new File(getFilesDir(), "profile_" + identifiant + ".png");
            try (FileOutputStream out = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, out);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PROFILE_IMAGE_KEY + identifiant, file.getAbsolutePath());
                editor.apply();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la sauvegarde", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfileImage() {
        String imagePath = sharedPreferences.getString(PROFILE_IMAGE_KEY + identifiant, null);
        if (imagePath != null) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                profilePhoto.setImageBitmap(bitmap);
            }
        }
    }

    private void setupNavigation() {
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

    public interface ClientModifier {
        boolean appliquer(String identifiant, String nouvelleValeur);
    }

    public void modif(String param, ClientModifier fonction) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.modif, null);

        EditText editText = dialogView.findViewById(R.id.EditModif);
        TextView textView = dialogView.findViewById(R.id.TVmodif);

        textView.setText("Entrez un nouveau " + param + " :");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        builder.setPositiveButton(R.string.Confirmer, (dialog, id) -> {
            String nouvelleValeur = editText.getText().toString().trim();
            if (!nouvelleValeur.isEmpty()) {
                boolean success = fonction.appliquer(identifiant, nouvelleValeur);
                if (success) {
                    Toast.makeText(this, param + " modifié avec succès", Toast.LENGTH_SHORT).show();
                    recreate();
                } else {
                    Toast.makeText(this, "Échec de la modification", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Le champ ne peut pas être vide", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.Retour, null);
        builder.create().show();
    }

    private void updateClient(Client client, Runnable onSuccess) {
        apiService.updateClient(client).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ClientProfilInfos.this, "Modification réussie", Toast.LENGTH_SHORT).show();
                    if (onSuccess != null) onSuccess.run();
                } else {
                    Toast.makeText(ClientProfilInfos.this, "Échec de la modification", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClientProfilInfos.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteClient(String login) {
        apiService.deleteClient(login).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ClientProfilInfos.this, "Compte supprimé", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ClientProfilInfos.this, Connexion.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ClientProfilInfos.this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClientProfilInfos.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}