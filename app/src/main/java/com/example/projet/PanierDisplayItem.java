package com.example.projet;

public class PanierDisplayItem {
    private Produit produit;
    private Panier panier;

    public PanierDisplayItem(Produit produit, Panier panier) {
        this.produit = produit;
        this.panier = panier;
    }

    public Produit getProduit() {
        return produit;
    }

    public Panier getPanier() {
        return panier;
    }
}
