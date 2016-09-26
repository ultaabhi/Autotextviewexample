package com.example.abhi.autotextviewexample;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ABHI on 5/5/2016.
 */
public class AsyncRequest extends AsyncTask<String, String, String> {
    public AsyncResponse delegate = null;
    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0]+"term="+params[1];
        HttpURLConnection httpURLConnection = null;
        String data = "";
        try {
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Log.d("DEBUG","Data receiving..");
            data = data+" "+ bufferedReader.readLine();
            Log.d("DEBUG","Data received: "+data);

        } catch (MalformedURLException e) {
            System.out.println("MalformedException");
        } catch (IOException e) {
            System.out.println("IOException");
        } finally {

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

}
