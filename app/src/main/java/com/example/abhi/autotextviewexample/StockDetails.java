package com.example.abhi.autotextviewexample;

/**
 * Created by ABHI on 5/3/2016.
 */
public class StockDetails {

    private String title;
    private String text;
    private int iconID;

    public StockDetails(String title,String text,int iconID)
    {
        super();
        this.title=title;
        this.text=text;
        this.iconID=iconID;

    }

    public int getIconID() {
        return iconID;
    }

    public String getText() {

        return text;
    }

    public String getTitle() {

        return title;
    }
}
