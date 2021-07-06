package com.example.neotimingtest.sdk;

import io.neow3j.types.Hash160;

import java.io.Serializable;
import java.math.BigInteger;

public class Token implements Serializable{

    private Hash160 owner;

    private String name;

    private BigInteger expiration;

    private  boolean isOnSale;

    private String genre;

    private BigInteger hp;

    private BigInteger attack;

    private  BigInteger defense;

    public Token(Hash160 owner, String name, BigInteger expiration, boolean isOnSale,String genre,BigInteger hp,BigInteger attack, BigInteger defense) {
        this.owner = owner;
        this.name = name;
        this.expiration = expiration;
        this.isOnSale = isOnSale;
        this.genre = genre;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
    }

    public Token() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getExpiration() {
        return expiration;
    }

    public void setExpiration(BigInteger expiration) {
        this.expiration = expiration;
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public void setOnSale(boolean onSale) {
        isOnSale = onSale;
    }


    public Hash160 getOwner() {
        return owner;
    }

    public void setOwner(Hash160 owner) {
        this.owner = owner;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public BigInteger getHp() {
        return hp;
    }

    public void setHp(BigInteger hp) {
        this.hp = hp;
    }

    public BigInteger getAttack() {
        return attack;
    }

    public void setAttack(BigInteger attack) {
        this.attack = attack;
    }

    public BigInteger getDefense() {
        return defense;
    }

    public void setDefense(BigInteger defense) {
        this.defense = defense;
    }
}
