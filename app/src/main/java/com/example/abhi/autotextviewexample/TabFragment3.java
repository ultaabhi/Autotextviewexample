package com.example.abhi.autotextviewexample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABHI on 4/14/2016.
 */
public class TabFragment3 extends Fragment {

    private static final String getsym = "aapl";
    private static final String values1 = "";
    private static final String TAGNews1 = "NEWSFULLArray1";
    private static final String TAGNews2 = "NEWSSeparateArray1";
    private static final String TAGNews11 = "AyncArray1";
    private static final String TAGNews21 = "AyncArray2";
    String [] appendUrl= new String[3];



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        /*try {

            //String temp=sName.replace(" ", "%20");
            URL js = new URL("http://stockapi-env.us-west-1.elasticbeanstalk.com/stocktest.php?newssymbol="+getsym);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            JSONArray jsonArray = jsonResponse.getJSONArray("news");
            Log.e(TAGNews1, String.valueOf(jsonArray));


            for(int i = 0; i < 4; i++){
                JSONObject c = jsonArray.getJSONObject(i);
                String Title = c.getString("Title");
                String Desc = c.getString("Desc");
                String url1 = c.getString("url1");

                // show the values in our logcat
                Log.e(TAGNews2, "Title: " + Title
                        + ", Desc: " + Desc
                        + ", url1: " + url1);

            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }*/

        String Newssymbol = null;
        try {
            Newssymbol = MainActivity.jsonObject.getString("Symbol");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        appendUrl[0] = "http://stockapi-env.us-west-1.elasticbeanstalk.com/stocktest.php?newssymbol=" + Newssymbol;

        new newsTask().execute(appendUrl);

        return inflater.inflate(R.layout.tab_fragment_3, container, false);

    }



    public class newsTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String[] params) {
            HttpURLConnection connection = null;
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);

                }
                String finalJson = buffer.toString();

                return finalJson;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }/*catch (JSONException e){
                e.printStackTrace();
            }*/ finally {
                if (connection != null) {
                    connection.disconnect();
                }

                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return "news";
        }




        @Override
        protected void onPostExecute(String result) {
            //Log.e(TAGNews2,result );
            if(result==null)return;
            super.onPostExecute(result);

            List<NewsDetails> newsList = new ArrayList<>();
            if (result != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    JSONArray jsonArray = jsonResponse.getJSONArray("news");
                    for (int i = 0; i < 4; i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        //Log.e("newsInfo : ", jsonObj.getString("url1") + "  " + jsonObj.getString("Title") + " " + jsonObj.getString("Desc") + " "  + jsonObj.getString("Pub") + " "  + jsonObj.getString("dat"));
                        NewsDetails newsItem = new NewsDetails(jsonObj.getString("url1"), jsonObj.getString("Title"), jsonObj.getString("Desc"), jsonObj.getString("Pub"),jsonObj.getString("dat")  );
                        newsList.add(newsItem);
                    }
                    ListView listView = (ListView) getActivity().findViewById(R.id.NewslistView);
                    NewsListAdapter newsListAdapter = new NewsListAdapter(getActivity(), R.layout.news_layout, newsList);
                    listView.setAdapter(newsListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            /*JSONObject jsonResponse = null;
            try {
                jsonResponse = new JSONObject(result);
                JSONArray jsonArray = jsonResponse.getJSONArray("news");
                Log.e(TAGNews11, String.valueOf(jsonArray));


                for(int i = 0; i < 4; i++){
                    JSONObject c = jsonArray.getJSONObject(i);
                    String Title = c.getString("Title");
                    String Desc = c.getString("Desc");
                    String url1 = c.getString("url1");

                    // show the values in our logcat
                    Log.e(TAGNews21, "Title: " + Title
                            + ", Desc: " + Desc
                            + ", url1: " + url1);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }*/

        }

    }
}
