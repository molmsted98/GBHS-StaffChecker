package com.grandblanchs.staffchecker;


public class Item {

    private int number;
    private String hardcoded;
    private String web;

    public Item() {
        this.number = 0;
        this.hardcoded = "";
        this.web = "";
    }

    public Item(int number, String hardcoded, String web) {
        this.number = number;
        this.hardcoded = hardcoded;
        this.web = web;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getHardcoded() {
        return hardcoded;
    }

    public void setHardcoded(String hardcoded) {
        this.hardcoded = hardcoded;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }
}
