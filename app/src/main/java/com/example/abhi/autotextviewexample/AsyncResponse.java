package com.example.abhi.autotextviewexample;

/**
 * Created by ABHI on 5/5/2016.
 */
public interface AsyncResponse {
    public void processFinish(String output);
    public void callNewIntent(boolean isValid, String message);
    public void processQuote(String output);
    public void setAdapterMethod();
}
