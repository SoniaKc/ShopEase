package com.example.projet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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

public class BoutiqueProfilInfos extends Activity {
    String identifiant;
    TextView Nom, Email, Tel, Id, Mdp, FormeJuridique, Siret, SiegeSocial, PaysEnregistrement;
    ApiService apiService;
    Boutique currentBoutique;
    ImageView profilePhoto, mainProfilePhoto;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_profil_infos);

        apiService = ApiClient.getClient().create(ApiService.class);
        identifiant = getIntent().getStringExtra("id");

        Nom = findViewById(R.id.nom);
        Email = findViewById(R.id.email);
        Tel = findViewById(R.id.tel);
        Id = findViewById(R.id.id);
        Mdp = findViewById(R.id.mdp);
        FormeJuridique = findViewById(R.id.formeJuridique);
        Siret = findViewById(R.id.siret);
        SiegeSocial = findViewById(R.id.siegeSocial);
        PaysEnregistrement = findViewById(R.id.paysEnregitrement);
        profilePhoto = findViewById(R.id.profilePhoto);
        mainProfilePhoto = findViewById(R.id.mainProfilePhoto);


        apiService.getBoutique(identifiant).enqueue(new Callback<Boutique>() {
            @Override
            public void onResponse(Call<Boutique> call, Response<Boutique> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentBoutique = response.body();
                    updateUIFromBoutique(currentBoutique);
                } else {
                    Toast.makeText(BoutiqueProfilInfos.this, "Boutique introuvable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boutique> call, Throwable t) {
                Toast.makeText(BoutiqueProfilInfos.this, "Erreur serveur", Toast.LENGTH_SHORT).show();
            }
        });


        ImageButton BtnModifNom = findViewById(R.id.BtnModifNom);
        ImageButton BtnModifEmail = findViewById(R.id.BtnModifEmail);
        ImageButton BtnModifTel = findViewById(R.id.BtnModifTel);
        ImageButton BtnModifMdp = findViewById(R.id.BtnModifMdp);

        ImageButton BtnModifFormeJuridique = findViewById(R.id.BtnModifFormeJuridique);
        ImageButton BtnModifSiret = findViewById(R.id.BtnModifSiret);
        ImageButton BtnModifSiegeSocial = findViewById(R.id.BtnModifSiegeSocial);
        ImageButton BtnModifPaysEnregistrement = findViewById(R.id.BtnModifPaysEnregistrement);
        ImageButton BtnModifIban = findViewById(R.id.BtnModifIban);
        ImageButton btnModifPhoto = findViewById(R.id.BtnModifPhoto);

        Button btnSupprimer = findViewById(R.id.btnSupprimer);

        btnModifPhoto.setOnClickListener(v -> openImageChooser());

        BtnModifNom.setOnClickListener(b -> modif("nom", (id, val) -> {
            currentBoutique.nom = val;
            updateBoutique(currentBoutique, () -> updateUIFromBoutique(currentBoutique));
            return true;
        }));
        BtnModifEmail.setOnClickListener(b -> modif("email", (id, val) -> {
            currentBoutique.email = val;
            updateBoutique(currentBoutique, () -> updateUIFromBoutique(currentBoutique));
            return true;
        }));
        BtnModifTel.setOnClickListener(b -> modif("téléphone", (id, val) -> {
            currentBoutique.telephone = val;
            updateBoutique(currentBoutique, () -> updateUIFromBoutique(currentBoutique));
            return true;
        }));

        BtnModifMdp.setOnClickListener(b -> modif("mot de passe", (id, val) -> {
            currentBoutique.password = val;
            updateBoutique(currentBoutique, () -> updateUIFromBoutique(currentBoutique));
            return true;
        }));
        BtnModifFormeJuridique.setOnClickListener(b -> modif("forme juridique", (id, val) -> {
            currentBoutique.forme_juridique = val;
            updateBoutique(currentBoutique, () -> updateUIFromBoutique(currentBoutique));
            return true;
        }));
        BtnModifSiret.setOnClickListener(b -> modif("SIRET", (id, val) -> {
            currentBoutique.siret = val;
            updateBoutique(currentBoutique, () -> updateUIFromBoutique(currentBoutique));
            return true;
        }));
        BtnModifSiegeSocial.setOnClickListener(b -> modif("siège social", (id, val) -> {
            currentBoutique.siege_social = val;
            updateBoutique(currentBoutique, () -> updateUIFromBoutique(currentBoutique));
            return true;
        }));
        BtnModifPaysEnregistrement.setOnClickListener(b -> modif("pays d'enregistrement", (id, val) -> {
            currentBoutique.pays_enregistrement = val;
            updateBoutique(currentBoutique, () -> updateUIFromBoutique(currentBoutique));
            return true;
        }));
        BtnModifIban.setOnClickListener(b -> modif("IBAN", (id, val) -> {
            currentBoutique.iban = val;
            updateBoutique(currentBoutique, () -> updateUIFromBoutique(currentBoutique));
            return true;
        }));

        btnSupprimer.setOnClickListener(v -> {
            new AlertDialog.Builder(BoutiqueProfilInfos.this)
                    .setTitle("Confirmation")
                    .setMessage("Voulez-vous vraiment supprimer votre compte boutique ? Cette action est irréversible.")
                    .setPositiveButton("Supprimer", (dialog, which) -> supprimerCompte())
                    .setNegativeButton("Annuler", null)
                    .show();
        });

        setupNavigation();
    }

    private void setupNavigation() {
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

    public interface BoutiqueModifier {
        boolean appliquer(String identifiant, String nouvelleValeur);
    }


    public void modif(String param, BoutiqueModifier fonction) {
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
                    updateBoutique(currentBoutique, () -> {
                        updateUIFromBoutique(currentBoutique);
                        Toast.makeText(this, param + " modifié avec succès", Toast.LENGTH_SHORT).show();
                    });
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

    private void updateBoutique(Boutique boutique, Runnable onSuccess) {
        apiService.updateBoutique(boutique).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BoutiqueProfilInfos.this, "Modification réussie", Toast.LENGTH_SHORT).show();
                    if (onSuccess != null) onSuccess.run();
                } else {
                    Toast.makeText(BoutiqueProfilInfos.this, "Échec de la modification", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(BoutiqueProfilInfos.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void supprimerCompte() {
        apiService.deleteBoutique(identifiant).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BoutiqueProfilInfos.this, "Compte supprimé.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BoutiqueProfilInfos.this, Connexion.class); // ou MainActivity
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(BoutiqueProfilInfos.this, "Erreur lors de la suppression.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(BoutiqueProfilInfos.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Sélectionnez une image"), PICK_IMAGE_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                Bitmap resizedBitmap = resizeBitmap(bitmap, 800, 800);
                Bitmap circularBitmap = getCircularBitmap(resizedBitmap);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                circularBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);

                currentBoutique.image = Base64.encodeToString(bitmapToByteArray(circularBitmap), Base64.NO_WRAP);
                updateBoutique(currentBoutique, () -> updateUIFromBoutique(currentBoutique));

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

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, size, size);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(Color.WHITE);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);

        return output;
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void updateUIFromBoutique(Boutique boutique) {
        Nom.setText("Nom : " + currentBoutique.nom);
        Email.setText("Email : " + currentBoutique.email);
        Tel.setText("Tel : " + currentBoutique.telephone);
        Id.setText("Identifiant : " + currentBoutique.login);
        Mdp.setText("Mot de passe : " + "********");
        FormeJuridique.setText("Forme Juridique : " + currentBoutique.forme_juridique);
        Siret.setText("Siret : " + currentBoutique.siret);
        SiegeSocial.setText("Siege Social : " + currentBoutique.siege_social);
        PaysEnregistrement.setText("Pays d' Enregitrement : " + currentBoutique.pays_enregistrement);

        ImageHandler.handleAllImages(boutique.image, mainProfilePhoto, profilePhoto);
    }

}
