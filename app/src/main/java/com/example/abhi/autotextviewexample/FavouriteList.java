package com.example.abhi.autotextviewexample;

/**
 * Created by ABHI on 5/5/2016.
 */
public class FavouriteList {

    private String symbol;
    private String compName;
    private String currentPrice;
    private String marketCap;
    private double changePercent;

    public FavouriteList(String symbol,String compName, String currentPrice,String marketCap,double changePercent){
        this.symbol = symbol;
        this.compName = compName;
        this.currentPrice = currentPrice;
        this.marketCap = marketCap;
        this.changePercent = changePercent;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompName() {
        return compName;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public double getChangePercent() {
        return changePercent;
    }
}
