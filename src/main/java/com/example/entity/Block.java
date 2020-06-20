package com.example.entity;

public class Block {

    String number;
    String hash;
    String difficulty;
    Transaction[] transactions;

    public String getNumber() {
        return number;
    }

    public Block setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public Block setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Block setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public Transaction[] getTransactions() {
        return transactions;
    }

    public Block setTransactions(Transaction[] transactions) {
        this.transactions = transactions;
        return this;
    }
}
