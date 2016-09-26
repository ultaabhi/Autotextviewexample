package com.example.abhi.autotextviewexample;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by ABHI on 4/14/2016.
 */
public class TabFragment1 extends Fragment {
    private static final String TAG1 = "MyTABBBEDNAMEEEEEEEE";
    View rootview;
    public static JSONObject jsonObject;
    ImageView imageViewYahoo;
    PhotoViewAttacher mAttacher;
    ListView listView1;
    public String ur = null;



    private List<StockDetails> mystocks = new ArrayList<StockDetails>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.tab_fragment_1, container, false);
        listView1 = (ListView)rootview.findViewById(R.id.listView1);
        populateStocklist();
        populateListView();


        return rootview;

    }


    private void populateStocklist() {
        String [] temp= new String[15];
        try {

            temp[0] = MainActivity.jsonObject.getString("Name");
            temp[1] = MainActivity.jsonObject.getString("Symbol");
            temp[2] = MainActivity.jsonObject.getString("LastPrice");
            temp[3] = MainActivity.jsonObject.getString("Change");
            temp[4] = MainActivity.jsonObject.getString("ChangePercent");

            String s1 = String.format("%.2f", Float.parseFloat(MainActivity.jsonObject.getString("Change")));
            String s2 = String.format("%.2f", Float.parseFloat(MainActivity.jsonObject.getString("ChangePercent")));

            temp[13] =  s1+"("+s2.toString()+"%)";

            temp[5] = MainActivity.jsonObject.getString("Timestamp");
            temp[6] = MainActivity.jsonObject.getString("MarketCap");
            temp[7] = MainActivity.jsonObject.getString("Volume");
            temp[8] = MainActivity.jsonObject.getString("ChangeYTD");
            temp[9] = MainActivity.jsonObject.getString("ChangePercentYTD");
            temp[10] = MainActivity.jsonObject.getString("High");
            temp[11] = MainActivity.jsonObject.getString("Low");
            temp[12] = MainActivity.jsonObject.getString("Open");

            String c1=String.format("%.2f", Float.parseFloat(MainActivity.jsonObject.getString("ChangeYTD")));
            String c2=String.format("%.2f", Float.parseFloat(MainActivity.jsonObject.getString("ChangePercentYTD")));
            temp[14] = c1+"("+c2.toString()+"%)";

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mystocks.add(new StockDetails("NAME",temp[0],0));
        mystocks.add(new StockDetails("SYMBOL",temp[1],0));
        mystocks.add(new StockDetails("LASTPRICE",temp[2],0));

        try {
            if(Float.parseFloat(MainActivity.jsonObject.getString("Change"))< 0)
            {
                mystocks.add(new StockDetails("CHANGE",temp[13],R.mipmap.ic_arrow_down));

            }
            else if (Float.parseFloat(MainActivity.jsonObject.getString("Change"))> 0)
            {
                mystocks.add(new StockDetails("CHANGE",temp[13],R.mipmap.ic_arrow_up));
            }
            else {
                mystocks.add(new StockDetails("CHANGE",temp[13],0));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mystocks.add(new StockDetails("TIMESTAMP",temp[5],0));
        mystocks.add(new StockDetails("MARKETCAP", temp[6], 0));


        /*String vol = temp[9]+*/
        mystocks.add(new StockDetails("VOLUME",temp[7],0));

        try {
            if(Float.parseFloat(MainActivity.jsonObject.getString("ChangePercentYTD"))< 0)
            {
                mystocks.add(new StockDetails("ChangePercentYTD",temp[14],R.mipmap.ic_arrow_down));

            }
            else if (Float.parseFloat(MainActivity.jsonObject.getString("ChangePercentYTD"))> 0)
            {
                mystocks.add(new StockDetails("ChangePercentYTD",temp[14],R.mipmap.ic_arrow_up));
            }
            else {
                mystocks.add(new StockDetails("ChangePercentYTD",temp[14],0));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        mystocks.add(new StockDetails("HIGH",temp[10],0));
        mystocks.add(new StockDetails("LOW",temp[11],0));
        mystocks.add(new StockDetails("OPEN",temp[12],0));

    }
    @TargetApi(17)
    private void populateListView() {
        ArrayAdapter<StockDetails> adapter = new MyListAdapter();
        ListView list = (ListView)rootview.findViewById(R.id.listView1);
        TextView textView = new TextView(getContext());
        textView.setHeight(175);
        textView.setTextSize(25);
        //textView.setTextColor(Color.BLACK);
        //textView.setPadding(0,0,0,25);
        textView.setTypeface(null, Typeface.BOLD);

        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setText("Stock Details");
        list.addHeaderView(textView);
        Bitmap bit = null;
        String ur = null;
        imageViewYahoo = new ImageView(getContext());

        try {
            ur="http://chart.finance.yahoo.com/t?s="+MainActivity.jsonObject.getString("Symbol")+"&lang=en-US&width=1250&height=750";

        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;
            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {

                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {

                bmImage.setImageBitmap(result);

            }
        }
        new  DownloadImageTask(imageViewYahoo) .execute(ur);
       /* imageViewYahoo.setImageBitmap(bit);*/

        /*imageViewYahoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*Dialog builder =
                        new Dialog(getContext()).
                                setMessage("Message above the image").
                                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).
                                setView();
                builder.create().show();
                *//*
                loadpop(v);
            }
        });*/

        TextView t = new TextView(getContext());
        t.setText("Today's Stock Activity");
        t.setTextSize(20);
        list.addFooterView(t);
        list.addFooterView(imageViewYahoo);
        mAttacher = new PhotoViewAttacher(imageViewYahoo);
        list.setAdapter(adapter);
        //mAttacher.update();

    }

    private void loadpop(View view)
    {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, null);

        PopupWindow popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        WebView web = (WebView)layout.findViewById(R.id.fullimage);

        //mAttacher = new PhotoViewAttacher(imageViewYahoo);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDisplayZoomControls(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setDefaultFontSize(10);
        web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        web.setScrollbarFadingEnabled(true);
        web.loadUrl(ur);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.setBackgroundDrawable(new AnimationDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }


    private class MyListAdapter extends ArrayAdapter<StockDetails> {
        public MyListAdapter() {
            super(getActivity(),R.layout.current_layout,R.id.textView1,mystocks);
        }
        @Override
        public View getView(int position,View convertView,ViewGroup parent)
        {   LayoutInflater mInflater;
            Context context=getContext();
            mInflater = LayoutInflater.from(context);
            View itemView = convertView;
            if(itemView == null)
            {   itemView = mInflater.inflate(R.layout.current_layout, null);
                /*itemView.mI.inflate(R.layout.details_layout,parent,false);*/
            }

            StockDetails currentStock = mystocks.get(position);

            // Fill the view
            ImageView imageView = (ImageView)itemView.findViewById(R.id.imageView1);
            imageView.setImageResource(currentStock.getIconID());

            // Make:
            TextView makeText = (TextView) itemView.findViewById(R.id.textView1);
            makeText.setText(currentStock.getTitle());

            // Year:
            TextView yearText = (TextView) itemView.findViewById(R.id.textView2);
            yearText.setText("" + currentStock.getText());




            return  itemView;
        }
    }






}

