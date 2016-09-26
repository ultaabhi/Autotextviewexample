package com.example.abhi.autotextviewexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import com.example.abhi.autotextviewexample.R;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    public  static  JSONObject jsonObject= null;
    private static FavouriteList obj;
    public Button clr;
    public static String autotxt;
    AlertDialog.Builder alertDialog;
    private static final String TAG1 = "Myselection";
    private static final String TAG11 = "MyQuoteArray";
    private static final String TAG2 = "MyQuote";
    public static ListView listView = null;
    public static List<FavouriteList> list = new ArrayList<FavouriteList>();
    public static ArrayAdapter<FavouriteList> favouriteListArrayAdapter;
    public static SharedPreferences setting;
    private boolean validated = true;
    GestureDetector gestureDetector;
    TouchListener onTouchListener;
    public static String aws = "http://stockapi-env.us-west-1.elasticbeanstalk.com/stocktest.php?";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.stock_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);
        TextView favouriteHeader = (TextView)findViewById(R.id.textView2);
        favouriteHeader.setText("Favourite list");

        favouriteListArrayAdapter = new FavListAdapter();
        setting = getSharedPreferences(ResultActivity.PREFS_NAME,MODE_PRIVATE);
        String symbolList = setting.getString("symbol","");
        AsyncFavTable asyncFavTable = new AsyncFavTable();
        asyncFavTable.delegate = this;
        asyncFavTable.execute(symbolList);
        listView.setAdapter(favouriteListArrayAdapter);


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_FLING){
                    Log.d("DEBUG","OK swipe happened");
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        gestureDetector = new GestureDetector(this, new GestureListener());
        onTouchListener = new TouchListener();
        listView.setOnTouchListener(onTouchListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DEBUG","THIS WORKS!!");
                String symbol = setting.getString("symbol","");
                String[] symbols = symbol.split(" ");
                int j = 0;
                for (int i = 0;i < symbols.length;i++) {
                    if (!symbols[i].equals("")){
                        if (position == j){
                            Log.d("DEBUG", "THIS ALSO WORKS IF TSLA=" + symbols[i]);
                            AsyncQuoteRequest asyncQuoteRequest = new AsyncQuoteRequest();
                            asyncQuoteRequest.delegate = MainActivity.this;
                            asyncQuoteRequest.execute(aws,symbols[i]);
                        }
                        j++;
                    }
                }
            }
        });


        final AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        acTextView.setThreshold(3);
        acTextView.setAdapter(new SuggestionAdapter(this,acTextView.getText().toString()));


        acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                String selection = (String)parent.getItemAtPosition(position);
                String[] separated = selection.split(" ");
                acTextView.setText(separated[0]);

            }
        });

        clr = (Button) findViewById(R.id.clear);
        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView txtbox1=(AutoCompleteTextView) findViewById(R.id.autoComplete);
                autotxt=txtbox1.getText().toString();
                txtbox1.setText("");
            }
        });


        ImageButton refreshButton = (ImageButton)findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncFavTable asyncFav = new AsyncFavTable();
                String symbolList = setting.getString("symbol","");
                asyncFav.delegate = MainActivity.this;
                asyncFav.execute(symbolList);
            }
        });

        Switch aSwitch = (Switch)findViewById(R.id.autoRefreshSwitch);
        if (aSwitch != null) {
            aSwitch.setOnCheckedChangeListener(new AutoRefreshListener());
        }


        Button btnSelected=(Button)findViewById(R.id.getquote);

        assert btnSelected != null;
        btnSelected.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String getsym=acTextView.getText().toString();
                Log.e(TAG1, String.valueOf(getsym));
                AutoCompleteTextView txtUserName = (AutoCompleteTextView) findViewById(R.id.autoComplete);
                String strUserName = txtUserName.getText().toString();

                if(TextUtils.isEmpty(strUserName)) {
                    alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setMessage("Please enter a Stock Name/Symbol");
                    alertDialog.setCancelable(true);
                    alertDialog.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                    return;
                } else {
                    try {

                        //String temp=sName.replace(" ", "%20");
                        URL js = new URL("http://stockapi-env.us-west-1.elasticbeanstalk.com/stocktest.php?sym="+getsym);
                        URLConnection jc = js.openConnection();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
                        String line = reader.readLine();
                        jsonObject = new JSONObject(line);
                        //Log.e(TAG11, String.valueOf(jsonObject));
                        String status = jsonObject.getString("Status");
                        if(!status.equals("SUCCESS")){

                            alertDialog = new AlertDialog.Builder(MainActivity.this);
                            alertDialog.setMessage("NO Stock Details");
                            alertDialog.setCancelable(true);
                            alertDialog.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            alertDialog.show();
                            return;

                        }else {
                            // Start NewActivity.class
                            Intent myIntent = new Intent(MainActivity.this,ResultActivity.class);
                            myIntent.putExtra("jsonArray", jsonObject.toString());
                            startActivity(myIntent);
                        }




                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                }







            }
        });


    }


    @Override
    public void processFinish(String output) {

    }

    @Override
    public void callNewIntent(boolean isValid, String message) {

    }

    @Override
    public void processQuote(String output) {

        try {
            Log.d("DEBUG",output);
            if (validated) {
                jsonObject = new JSONObject(output);
                if (!(jsonObject.getString("Status").equals("SUCCESS"))){
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("No Stock Details Available");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    Intent i = new Intent(getApplicationContext(), ResultActivity.class);
                    startActivity(i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setAdapterMethod(){

        listView.setAdapter(favouriteListArrayAdapter);
    }

    private class FavListAdapter extends ArrayAdapter<FavouriteList> {

        public FavListAdapter() {
            super(MainActivity.this, R.layout.favorite_layout,list);
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check for null view
            View favView = convertView;
            if (favView == null){
                favView = getLayoutInflater().inflate(R.layout.favorite_layout,parent,false);
            }

            //find the symbol
            FavouriteList currentFavouriteList = list.get(position);

            //fill the view

            TextView symbol = (TextView) favView.findViewById(R.id.favSymbol);
            symbol.setText(currentFavouriteList.getSymbol());

            TextView company = (TextView) favView.findViewById(R.id.favCompName);
            company.setText(currentFavouriteList.getCompName());

            TextView currentPrice = (TextView) favView.findViewById(R.id.favCurrPrice);

            if (currentFavouriteList.getChangePercent() >= 0) {
                currentPrice.setBackgroundColor(Color.GREEN);
                currentPrice.setText("+"+currentFavouriteList.getChangePercent() + "%");
            }
            else if (currentFavouriteList.getChangePercent() < 0) {
                currentPrice.setBackgroundColor(Color.RED);
                currentPrice.setText(currentFavouriteList.getChangePercent() + "%");
            }

            TextView marketCap = (TextView) favView.findViewById(R.id.favMarketCap);
            marketCap.setText(currentFavouriteList.getMarketCap());

            TextView change = (TextView) favView.findViewById(R.id.favChange);
            change.setText(currentFavouriteList.getCurrentPrice());


            return favView;
        }
    }

    protected class GestureListener extends GestureDetector.SimpleOnGestureListener
    {
        private static final int SWIPE_MIN_DISTANCE = 150;
        private static final int SWIPE_MAX_OFF_PATH = 100;
        private static final int SWIPE_THRESHOLD_VELOCITY = 100;

        private MotionEvent mLastOnDownEvent = null;


        @Override
        public boolean onDown(MotionEvent e)
        {
            mLastOnDownEvent = e;
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            if(e1 == null){
                e1 = mLastOnDownEvent;
            }
            if(e1==null || e2==null){
                return false;
            }
            int id = listView.pointToPosition((int) e1.getX(), (int) e1.getY());
            obj =  (FavouriteList) favouriteListArrayAdapter.getItem((id));
            float dX = e2.getX() - e1.getX();
            float dY = e1.getY() - e2.getY();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure you want to remove "+obj.getCompName()+"?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String selected = obj.getSymbol();
                            favouriteListArrayAdapter.remove(obj);
                            listView.setAdapter(favouriteListArrayAdapter);
                    /*sharedPreference.removeFavorite(getApplicationContext(),id);*/
                            String symbolList = setting.getString("symbol","");
                            Log.d("DEBUG","Initial:"+symbolList);
                            String[] symbols = symbolList.split(" ");
                            symbolList = "";
                            for (int i = 0; i < symbols.length;i++){
                                if (!selected.equals(symbols[i])){
                                    symbolList = symbolList+" "+symbols[i];
                                }
                            }
                            Log.d("DEBUG","Final: "+symbolList);
                            SharedPreferences.Editor editor = setting.edit();
                            editor.putString("symbol",symbolList);
                            editor.commit();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();


            return false;
        }
    }

    protected class TouchListener implements View.OnTouchListener
    {
        @Override
        public boolean onTouch(View v, MotionEvent e)
        {
            if (gestureDetector.onTouchEvent(e)){
                return true;
            }else{
                return false;
            }
        }
    }


}
