package com.example.projet;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageHandler {
    public static void getClientAndHandleAllImages(ApiService apiService, String identifiant, ImageView... imageViews) {
        apiService.getClient(identifiant).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Client client = response.body();

                    if (client.image == null || client.image.isEmpty()) {
                        for (ImageView imageView : imageViews) {
                            imageView.setImageResource(R.drawable.account);
                        }
                        return;
                    }

                    try {
                        byte[] decodedBytes = Base64.decode(client.image, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                        for (ImageView imageView : imageViews) {
                            if (imageView != null) {
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        for (ImageView imageView : imageViews) {
                            imageView.setImageResource(R.drawable.account);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                //Toast.makeText(ClientMentionsLegales.this, "Erreur serveur", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public static void getBoutiqueAndHandleAllImages(ApiService apiService, String identifiant, ImageView... imageViews) {
        apiService.getBoutique(identifiant).enqueue(new Callback<Boutique>() {
            @Override
            public void onResponse(Call<Boutique> call, Response<Boutique> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boutique boutique = response.body();

                    if (boutique.image == null || boutique.image.isEmpty()) {
                        for (ImageView imageView : imageViews) {
                            imageView.setImageResource(R.drawable.account);
                        }
                        return;
                    }

                    try {
                        byte[] decodedBytes = Base64.decode(boutique.image, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                        for (ImageView imageView : imageViews) {
                            if (imageView != null) {
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        for (ImageView imageView : imageViews) {
                            imageView.setImageResource(R.drawable.account);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Boutique> call, Throwable t) {
                //Toast.makeText(ClientMentionsLegales.this, "Erreur serveur", Toast.LENGTH_SHORT).show();
            }
        });

    }



    public static void handleAllImages(String base64Image, ImageView... imageViews) {
        if (base64Image == null || base64Image.isEmpty()) {
            for (ImageView imageView : imageViews) {
                imageView.setImageResource(R.drawable.account);
            }
            return;
        }

        try {
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            for (ImageView imageView : imageViews) {
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            for (ImageView imageView : imageViews) {
                imageView.setImageResource(R.drawable.account);
            }
        }
    }

    public static void handleProduitImages(String base64Image, ImageView... imageViews) {
        if (base64Image == null || base64Image.isEmpty()) {
            for (ImageView imageView : imageViews) {
                imageView.setImageResource(R.drawable.vente);
            }
            return;
        }

        try {
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            for (ImageView imageView : imageViews) {
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            for (ImageView imageView : imageViews) {
                imageView.setImageResource(R.drawable.account);
            }
        }
    }

}