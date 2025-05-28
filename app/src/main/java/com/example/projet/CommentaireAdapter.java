package com.example.projet;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentaireAdapter extends ArrayAdapter<Commentaire> {

    public CommentaireAdapter(Context context, List<Commentaire> commentaires) {
        super(context, 0, commentaires);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Commentaire commentaire = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_commentaire, parent, false);
        }

        TextView nomProduit = convertView.findViewById(R.id.nomProduit);
        RatingBar noteProduit = convertView.findViewById(R.id.noteProduit);
        TextView texteCommentaire = convertView.findViewById(R.id.texteCommentaire);
        ImageView imageProduit = convertView.findViewById(R.id.imageProduit);

        nomProduit.setText(commentaire.nom_produit);
        noteProduit.setRating(Float.parseFloat(commentaire.note));
        texteCommentaire.setText(commentaire.commentaire);
        imageProduit.setImageResource(R.drawable.img1); // Image statique

        Button btnModifier = convertView.findViewById(R.id.btnModifier);
        Button btnSupprimer = convertView.findViewById(R.id.btnSupprimer);

        // Tu peux implémenter btnModifier plus tard si nécessaire
        btnModifier.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_modifier_commentaire, null);

            TextView produitTitre = dialogView.findViewById(R.id.produitTitre);
            RatingBar editNote = dialogView.findViewById(R.id.editNote);
            TextView editCommentaire = dialogView.findViewById(R.id.editCommentaire);

            produitTitre.setText(commentaire.nom_produit);
            editNote.setRating(Float.parseFloat(commentaire.note));
            editCommentaire.setText(commentaire.commentaire);

            new AlertDialog.Builder(getContext())
                    .setTitle("Modifier le commentaire")
                    .setView(dialogView)
                    .setPositiveButton("Enregistrer", (dialog, which) -> {
                        commentaire.note = String.valueOf(editNote.getRating());
                        commentaire.commentaire = editCommentaire.getText().toString();

                        ApiService apiService = ApiClient.getClient().create(ApiService.class);
                        Call<Void> call = apiService.updateCommentaire(commentaire);

                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getContext(), "Commentaire mis à jour", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getContext(), "Erreur de mise à jour", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });

        btnSupprimer.setOnClickListener(v -> {
            deleteCommentaire(commentaire.login_boutique, commentaire.nom_produit, commentaire.idClient, position);
        });

        return convertView;
    }

    private void deleteCommentaire(String login_boutique, String nom_produit, String idClient, int position) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.deleteCommentaire(login_boutique, nom_produit, idClient);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Commentaire supprimé", Toast.LENGTH_SHORT).show();
                    // Supprimer de la liste et rafraîchir
                    remove(getItem(position));
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
