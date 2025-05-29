package com.example.projet;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Query;

public interface ApiService {

    // === CLIENT ===
    @POST("/api/client/add")
    Call<Void> addClient(@Body Client client);

    @GET("/api/client/get")
    Call<Client> getClient(@Query("login") String login);

    @DELETE("/api/client/delete")
    Call<Void> deleteClient(@Query("login") String login);

    @PUT("/api/client/update")
    Call<Void> updateClient(@Body Client client);

    // === BOUTIQUE ===
    @POST("/api/boutique/add")
    Call<Void> addBoutique(@Body Boutique boutique);

    @GET("/api/boutique/get")
    Call<Boutique> getBoutique(@Query("login") String login);

    @DELETE("/api/boutique/delete")
    Call<Void> deleteBoutique(@Query("login") String login);

    @PUT("/api/boutique/update")
    Call<Void> updateBoutique(@Body Boutique boutique);

    // === PARAMETRE ===
    @POST("/api/parametre/add")
    Call<Void> addParametre(@Body Parametre parametre); // Equivalent à AddParametre handler

    @GET("/api/parametre/get")
    Call<Parametre> getParametre(@Query("login") String login, @Query("type") String type); // Equivalent à GetParametre handler

    @DELETE("/api/parametre/delete")
    Call<Void> deleteParametre(@Query("login") String login, @Query("type") String type); // Equivalent à DeleteParametre handler

    @PUT("/api/parametre/update")
    Call<Void> updateParametre(@Body Parametre parametre); // Equivalent à UpdateParametre handler

    // === PAIEMENT ===
    // Ajouter une méthode de paiement
    @POST("api/paiement/add")
    Call<Void> addPaiement(@Body Paiement paiement);

    // Récupérer une méthode de paiement
    @GET("api/paiement/get")
    Call<Paiement> getPaiement(@Query("login") String login, @Query("nom_carte") String nomCarte);

    // Supprimer une méthode de paiement
    @DELETE("api/paiement/delete")
    Call<Void> deletePaiement(@Query("login") String login, @Query("nom_carte") String nomCarte);

    // Mettre à jour une méthode de paiement
    @PUT("api/paiement/update")
    Call<Void> updatePaiement(@Body Paiement paiement);

    // Récupérer toutes les méthodes de paiement
    @GET("api/paiement/getAll")
    Call<List<Paiement>> getAllPaiement(@Query("login") String login);

    // === ADRESSE ===
    @POST("api/adresse/add")
    Call<Void> addAdresse(@Body Adresse adresse);

    @GET("api/adresse/get")
    Call<Adresse> getAdresse(
            @Query("login") String login,
            @Query("nom_adresse") String nomAdresse
    );

    @GET("api/adresse/getAll")
    Call<List<Adresse>> getAllAdresse(@Query("login") String login);

    @DELETE("api/adresse/delete")
    Call<Void> deleteAdresse(
            @Query("login") String login,
            @Query("nom_adresse") String nomAdresse
    );

    @PUT("api/adresse/update")
    Call<Void> updateAdresse(@Body Adresse adresse);

    // === PRODUIT ===
    @POST("api/produit/add")
    Call<Void> addProduit(@Body Produit produit);

    @GET("api/produit/get")
    Call<Produit> getProduit(
            @Query("login_boutique") String loginBoutique,
            @Query("nom") String nom
    );

    @DELETE("/api/produit/delete")
    Call<Void> deleteProduit(@Query("login_boutique") String loginBoutique, @Query("nom") String nom);

    @PUT("/api/produit/update")
    Call<Void> updateProduit(@Body Produit produit);


    // Récupérer tous les produits
    @GET("api/produit/getAll")
    Call<List<Produit>> getAllProduits(@Query("login_boutique") String loginBoutique);

    @POST("/api/commentaire/add")
    Call<Void> addCommentaire(@Body Commentaire commentaire); // Ajout d'un commentaire

    @GET("/api/commentaire/getByProduit")
    Call<List<Commentaire>> getCommentairesByProduit(
            @Query("login_boutique") String loginBoutique,
            @Query("nom_produit") String nomProduit
    ); // Récupération des commentaires d'un produit

    @GET("/api/commentaire/getByClient")
    Call<List<Commentaire>> getCommentairesByClient(
            @Query("idClient") String idClient
    ); // Récupération des commentaires d'un client

    @DELETE("/api/commentaire/delete")
    Call<Void> deleteCommentaire(
            @Query("login_boutique") String loginBoutique,
            @Query("nom_produit") String nomProduit,
            @Query("idClient") String idClient
    ); // Suppression d'un commentaire

    @PUT("/api/commentaire/update")
    Call<Void> updateCommentaire(@Body Commentaire commentaire); // Mise à jour d'un commentaire


    // === COMMENTAIRE ===
    @POST("api/commentaire/add")
    Call<Void> addCommentaire(@Body Commentaire commentaire);

    @GET("api/commentaire/getByProduit")
    Call<List<Commentaire>> getCommentairesByProduit(
            @Query("login_boutique") String loginBoutique,
            @Query("nom") String nom
    );

    @GET("api/commentaire/getByClient")
    Call<List<Commentaire>> getCommentairesByClient(@Query("idClient") String idClient);

    @DELETE("/api/commentaire/delete")
    Call<Void> deleteCommentaire(
            @Query("login_boutique") String loginBoutique,
            @Query("nom") String nom,
            @Query("idClient") String idClient
    );

    @PUT("/api/commentaire/update")
    Call<Void> updateCommentaire(@Body Commentaire commentaire);


}
