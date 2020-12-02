package com.company;

public class Card {
    //Card
    public String cardName;
    public String cardID;
    public String cmc;
    public String type;
    public String subtype;
    public String text;
    public int power;
    public int toughness;

    //Set
    public String set;
    public int number;

    //That's a big boy
    Card (String cardName, String cardID, String cmc, String type, String subtype, String text, int power, int toughness, String set, int number) {

        //Card
        this.cardName = cardName;
        this.cardID = cardID;
        this.cmc = cmc;
        this.type = type;
        this.subtype = subtype;
        this.text = text;
        this.power = power;
        this.toughness = toughness;

        //Set
        this.set = set;
        this.number = number;
    }
}
