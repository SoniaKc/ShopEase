package com.example.projet;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/client/add")
    Call<Void> addClient(@Body Client client);

    @GET("/api/client/get")
    Call<Client> getClient(@Query("login") String login);

    @DELETE("/api/client/delete")
    Call<Void> deleteClient(@Query("login") String login);

    @PUT("/api/client/update")
    Call<Void> updateClient(@Body Client client);

    @POST("/api/produits")
    Call<Void> addProduit(@Body Produit produit);

    @POST("/api/panier")
    Call<Void> addToPanier(@Body Panier item);

    @POST("/api/favoris")
    Call<Void> addFavori(@Body Favoris favori);
}
