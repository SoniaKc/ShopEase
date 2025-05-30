package com.example.projet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageToByteaConverter {

    /**
     * Convertit un fichier image en tableau de bytes (BYTEA) - version Android
     * @param imageFile Fichier image à convertir
     * @param compressFormat Format de compression (Bitmap.CompressFormat.JPEG/PNG/WEBP)
     * @param quality Qualité de compression (0-100)
     * @return Tableau de bytes ou null en cas d'erreur
     */
    public static byte[] convertImageToBytea(File imageFile, Bitmap.CompressFormat compressFormat, int quality) {
        try {
            // Lire le fichier en Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile));

            // Compresser en byte[]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(compressFormat, quality, baos);

            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Version simplifiée avec compression JPEG par défaut
     */
    public static byte[] simpleConvertImageToBytea(File imageFile) {
        return convertImageToBytea(imageFile, Bitmap.CompressFormat.JPEG, 70);
    }
}