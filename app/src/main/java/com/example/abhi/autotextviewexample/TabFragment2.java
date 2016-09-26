package com.example.abhi.autotextviewexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;

/**
 * Created by ABHI on 4/14/2016.
 */
public class TabFragment2 extends Fragment {
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.tab_fragment_2, container, false);
        WebView webview = (WebView) v.findViewById(R.id.webView);
        webview.loadUrl("file:///android_asset/high.html");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
            /*public void onReceviederror(WebView web,int errorcode,String desc,String failurl)
            {
                Toast.makeText(getContext(),"no"+desc,Toast.LENGTH_SHORT).show();
            }*/

                try {
                    view.loadUrl("javascript:highfunc('" + MainActivity.jsonObject.getString("Symbol") + "');");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        });
        return v;
    }

}
