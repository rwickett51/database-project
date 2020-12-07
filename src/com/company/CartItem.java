package com.company;

public class CartItem {
    //Cart
    public String username;
    public String cartID;
    public int quantity;

    //Card
    public String cardName;
    public String cardID;
    public String cmc;
    public String type;
    public String subtype;
    public String text;
    public int power;
    public int toughness;

    //Listing
    public String price;
    public String listingID;

    //That's a big boy
    CartItem (String username, String cartID, int quantity, String cardName, String cardID, String cmc, String type, String subtype, String text, int power, int toughness, String price, String listingID) {
        //Cart
        this.username = username;
        this.cartID = cartID;
        this.quantity = quantity;

        //Card
        this.cardName = cardName;
        this.cardID = cardID;
        this.cmc = cmc;
        this.type = type;
        this.subtype = subtype;
        this.text = text;
        this.power = power;
        this.toughness = toughness;

        //Listing
        this.price = price;
        this.listingID = listingID;
    }



}
