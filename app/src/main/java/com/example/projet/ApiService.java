package com.example.projet;

import com.example.projet.bdd.*;

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
    Call<Void> addParametre(@Body Parametre parametre);

    @GET("/api/parametre/get")
    Call<Parametre> getParametre(@Query("id") String id);

    @DELETE("/api/parametre/delete")
    Call<Void> deleteParametre(@Query("id") String id);

    @PUT("/api/parametre/update")
    Call<Void> updateParametre(@Body Parametre parametre);

    // === PAIEMENT ===
    @POST("/api/paiement/add")
    Call<Void> addPaiement(@Body Paiement paiement);

    @GET("/api/paiement/get")
    Call<Paiement> getPaiement(@Query("id") String id);

    @DELETE("/api/paiement/delete")
    Call<Void> deletePaiement(@Query("id") String id);

    @PUT("/api/paiement/update")
    Call<Void> updatePaiement(@Body Paiement paiement);

    // === ADRESSE ===
    @POST("/api/adresse/add")
    Call<Void> addAdresse(@Body Adresse adresse);

    @GET("/api/adresse/get")
    Call<Adresse> getAdresse(@Query("id") String id);

    @DELETE("/api/adresse/delete")
    Call<Void> deleteAdresse(@Query("id") String id);

    @PUT("/api/adresse/update")
    Call<Void> updateAdresse(@Body Adresse adresse);

    // === PRODUIT ===
    @POST("/api/produit/add")
    Call<Void> addProduit(@Body Produit produit);

    @GET("/api/produit/get")
    Call<Produit> getProduit(@Query("id") String id);

    @DELETE("/api/produit/delete")
    Call<Void> deleteProduit(@Query("id") String id);

    @PUT("/api/produit/update")
    Call<Void> updateProduit(@Body Produit produit);
}
