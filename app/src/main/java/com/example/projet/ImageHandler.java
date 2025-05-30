package com.example.projet;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

public class ImageHandler {
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
}