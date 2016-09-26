package com.example.abhi.autotextviewexample;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABHI on 5/5/2016.
 */
public class AsyncFavTable extends AsyncTask<String, String, String> {

    public AsyncResponse delegate = null;
    private List<FavouriteList> favList = new ArrayList<FavouriteList>();
    @Override
    protected void onPostExecute(String s) {
        int count = MainActivity.favouriteListArrayAdapter.getCount();
        if (count > 0){
            for( int i = 0; i < count;){
                Log.d("DEBUG","Removing..");
                if (MainActivity.favouriteListArrayAdapter.getCount() > 0) {
                    MainActivity.favouriteListArrayAdapter.remove(MainActivity.favouriteListArrayAdapter.getItem(i));
                    count = MainActivity.favouriteListArrayAdapter.getCount();
                }
            }
        }
        for (int i = 0; i < favList.size();i++){
            MainActivity.favouriteListArrayAdapter.add(favList.get(i));
        }
        MainActivity.listView.setAdapter(MainActivity.favouriteListArrayAdapter);



        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... params) {

        String list = params[0];
        Log.d("DEBUG",list);
        HttpURLConnection httpURLConnection = null;
        String[] listArr = list.split(" ");
        String url = "http://ec2-52-37-85-242.us-west-2.compute.amazonaws.com?getQuote=";
        String data = "";
        for (int i = 0;i < listArr.length;i++ ){
            if (!listArr[i].equals("")) {
                try {
                    data = "";
                    String urlstring = MainActivity.aws+"sym=" + listArr[i];
                    Log.d("DEBUG", urlstring);
                    URL urls = new URL(urlstring);
                    httpURLConnection = (HttpURLConnection) urls.openConnection();
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    Log.d("DEBUG", "Data receiving..");
                    data = data + " " + bufferedReader.readLine();
                    Log.d("DEBUG", "Data received: " + data);
                    JSONObject jsonObject = new JSONObject(data);

                    String s1 = String.format("%.2f", Float.parseFloat(jsonObject.getString("Change")));
                    String s2 = String.format("%.2f", Float.parseFloat(jsonObject.getString("ChangePercent")));
                    String change =  s1+"("+s2.toString()+"%)";
                    favList.add(new FavouriteList(listArr[i], jsonObject.getString("Name"), "$"+jsonObject.getString("LastPrice"),
                            "Marketcap: " + jsonObject.getString("MarketCap").toString(), Double.parseDouble(s1)));


                } catch (MalformedURLException e) {
                    System.out.println("MalformedException");
                } catch (IOException e) {
                    System.out.println("IOException");
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }

                }
            }
        }


        return "";
    }


}
