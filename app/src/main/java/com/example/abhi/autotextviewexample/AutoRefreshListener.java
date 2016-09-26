package com.example.abhi.autotextviewexample;

import android.util.Log;
import android.widget.CompoundButton;

/**
 * Created by ABHI on 5/5/2016.
 */
public class AutoRefreshListener implements CompoundButton.OnCheckedChangeListener, AsyncResponse {
    public static boolean isCheck = false;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        AutoRefreshListener.isCheck = isChecked;
        if(isCheck) {
            AsyncAutoRefresh asyncFavT = new AsyncAutoRefresh();
            asyncFavT.delegate = this;
            asyncFavT.execute(MainActivity.setting.getString("symbol", ""), "auto");
        }
    }

    @Override
    public void processFinish(String output) {

    }

    @Override
    public void callNewIntent(boolean isValid, String message) {

    }

    @Override
    public void processQuote(String output) {

    }

    @Override
    public void setAdapterMethod() {
        Log.d("DEBUG","AUTORefresh should start now if true: "+isCheck);
        if (isCheck) {
            AsyncAutoRefresh asyncFavT = new AsyncAutoRefresh();
            asyncFavT.delegate = this;
            asyncFavT.execute(MainActivity.setting.getString("symbol", ""), "auto");
        }
    }
}
