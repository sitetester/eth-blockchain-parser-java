package com.example.entity;

public class Transaction {

    String hash;
    String blockNumber;
    String from;
    String to;

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getHash() {
        return hash;
    }

    public Transaction setHash(String hash) {
        this.hash = hash;
        return this;
    }
}
