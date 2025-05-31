package com.example.projet;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentaireAdapter extends RecyclerView.Adapter<CommentaireAdapter.ViewHolder> {

    public interface OnCommentaireAction {
        void onCommentaireDeleted(Commentaire commentaire);
    }

    private final List<Commentaire> commentaireList;
    private final Context context;
    private final OnCommentaireAction action;
    private final ApiService apiService;

    public CommentaireAdapter(Context context, List<Commentaire> commentaireList, OnCommentaireAction action) {
        this.context = context;
        this.commentaireList = commentaireList;
        this.action = action;
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    @NonNull
    @Override
    public CommentaireAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_commentaire, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentaireAdapter.ViewHolder holder, int position) {
        Commentaire commentaire = commentaireList.get(position);

        holder.nomProduit.setText(commentaire.nom_produit);
        holder.noteProduit.setRating(Float.parseFloat(commentaire.note));
        holder.texteCommentaire.setText(commentaire.commentaire);

        ImageHandler.getProduitAndHandleAllImages(apiService, commentaire.login_boutique, commentaire.nom_produit, holder.imageProduit);

        holder.btnModifier.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_modifier_commentaire, null);

            TextView produitTitre = dialogView.findViewById(R.id.produitTitre);
            RatingBar editNote = dialogView.findViewById(R.id.editNote);
            TextView editCommentaire = dialogView.findViewById(R.id.editCommentaire);

            produitTitre.setText(commentaire.nom_produit);
            editNote.setRating(Float.parseFloat(commentaire.note));
            editCommentaire.setText(commentaire.commentaire);

            new AlertDialog.Builder(context)
                    .setTitle("Modifier le commentaire")
                    .setView(dialogView)
                    .setPositiveButton("Enregistrer", (dialog, which) -> {
                        commentaire.note = String.valueOf(editNote.getRating());
                        commentaire.commentaire = editCommentaire.getText().toString();

                        Call<Void> call = apiService.updateCommentaire(commentaire);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Commentaire mis à jour", Toast.LENGTH_SHORT).show();
                                    notifyItemChanged(holder.getAdapterPosition());
                                } else {
                                    Toast.makeText(context, "Erreur de mise à jour", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });


        holder.btnSupprimer.setOnClickListener(v -> {
            Call<Void> call = apiService.deleteCommentaire(commentaire.login_boutique, commentaire.nom_produit, commentaire.idClient);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        commentaireList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        action.onCommentaireDeleted(commentaire);
                        Toast.makeText(context, "Commentaire supprimé", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return commentaireList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomProduit, texteCommentaire;
        RatingBar noteProduit;
        ImageView imageProduit;
        View btnModifier, btnSupprimer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomProduit = itemView.findViewById(R.id.nomProduit);
            noteProduit = itemView.findViewById(R.id.noteProduit);
            texteCommentaire = itemView.findViewById(R.id.texteCommentaire);
            imageProduit = itemView.findViewById(R.id.imageProduit);
            btnModifier = itemView.findViewById(R.id.btnModifier);
            btnSupprimer = itemView.findViewById(R.id.btnSupprimer);
        }
    }
}
