package com.example.abhi.autotextviewexample;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ABHI on 4/14/2016.
 */
public class ResultActivity extends AppCompatActivity {

    ShareDialog shareDialog;
    CallbackManager callbackManager;
    public static final String PREFS_NAME = "AOP_PREFS";
    SharedPreferences settings;
    private boolean toggleOn = false;
    public  static MenuItem fbitem ;


    private static final String TAG223 = "Result Activity Symbol";
    private static final String TAGarr = "MyArray2";
    private static final String TAG111 = "Mysymb";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        try {
            actionBar.setTitle(MainActivity.jsonObject.getString("Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        //String jsonArray = intent.getStringExtra("jsonArray");

        super.onCreate(savedInstanceState);
        // Get the view from ResultActivity.xml
        setContentView(R.layout.result_activity);

        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        FacebookSdk.sdkInitialize(getApplicationContext());


        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.e("DEBUG", "Hello: " + result.getPostId());
                if (result.getPostId() != null) {
                    Log.e("DEBUG", "SUCESS: " + result.getPostId());
                    Toast.makeText(ResultActivity.this, "Posted successfully",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ResultActivity.this, "Post cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {
                Log.e("DEBUG","Cancelled ");
                Toast.makeText(ResultActivity.this, "Post cancelled",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("DEBUG","Error : +"+error.getMessage());
                Toast.makeText(ResultActivity.this, "Error while posting",
                        Toast.LENGTH_SHORT).show();
            }
        });




        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("CURRENT"));
        tabLayout.addTab(tabLayout.newTab().setText("HISTORICAL"));
        tabLayout.addTab(tabLayout.newTab().setText("NEWS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        fbitem = menu.getItem(0);
        favcheck(menu.getItem(0));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        favcheck(menu.getItem(0));
        return true;
    }

    private void favcheck(MenuItem item) {

        try {
            boolean exist = false;
            String favList = settings.getString("symbol","");
            String[] favArray = favList.split(" ");
            for (int i = 0; i < favArray.length; i++ ){
                if (MainActivity.jsonObject.getString("Symbol").equals(favArray[i])){
                    exist = true;
                }
            }
            if (exist){
                item.setIcon(android.R.drawable.btn_star_big_on);

                toggleOn = true;
            } else {
                toggleOn = false;
                item.setIcon(android.R.drawable.btn_star_big_off);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        JSONObject jsonObject = MainActivity.jsonObject;
        String symbol = null;
        try {
            symbol = jsonObject.get("Symbol").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);

            break;



            case R.id.facebook:try
            {
                String fb = MainActivity.jsonObject.getString("Symbol").toString();
                Log.e("FB",fb);
               /* URI uri = Uri.parse("http://chart.finance.yahoo.com/t?s=aapl&lang=en-US&width=400&height=300");*/
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Current Stock Price of "+MainActivity.jsonObject.getString("Name").toString()+","+MainActivity.jsonObject.getString("LastPrice").toString())
                            .setContentDescription(
                                    "Stock Information of "+MainActivity.jsonObject.getString("Name").toString())
                            .setContentUrl(Uri.parse("http://finance.yahoo.com/q?s="+fb))


                            .setImageUrl(Uri.parse("http://chart.finance.yahoo.com/t?s="+fb+"&lang=en-US&width=200&height=150"))
                            .build();

                    shareDialog.show(linkContent);
                }
                break;
            }
            catch (JSONException e) {
                e.printStackTrace();
                break;
            }
            case R.id.add_fav:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...


                if (toggleOn){
                    Log.d("DEBUG","Remove from favourites");
                    toggleOn = false;
                    boolean done = false;
                    item.setIcon(android.R.drawable.btn_star_big_off);
                    int count = MainActivity.favouriteListArrayAdapter.getCount();
                    for (int i = 0; i < count; i++){
                        FavouriteList remove = MainActivity.favouriteListArrayAdapter.getItem(i);
                        if (remove.getSymbol().equals(symbol)){
                            MainActivity.favouriteListArrayAdapter.remove(remove);
                            MainActivity.listView.setAdapter(MainActivity.favouriteListArrayAdapter);
                            done = true;
                            break;
                        }
                    }
                    String[] symbols = settings.getString("symbol","").split(" ");
                    String list = "";
                    for (int i = 0; i < symbols.length;i++){
                        if (!symbol.equals(symbols[i])){
                            list = list +" "+ symbols[i];
                        }
                    }
                    SharedPreferences.Editor editor = settings.edit();
                    // NEED TO CHANGE THIS CODE
                    editor.putString("symbol",list);
                    editor.commit();
                    return done;

                } else {
                    Log.d("DEBUG","Add to favourites");
                    toggleOn = true;
                    item.setIcon(android.R.drawable.btn_star_big_on);
                    SharedPreferences.Editor editor = settings.edit();

                    String listOfFavs = settings.getString("symbol", "");
                    if (listOfFavs == null) {
                        listOfFavs = "";
                    }
                    try {
                        Log.d("DEBUG", "Symbol : " + symbol);
                        listOfFavs = listOfFavs + " " + symbol;
                        editor.putString("symbol", listOfFavs);
                        editor.commit();
                        String s1 = String.format("%.2f", Float.parseFloat(jsonObject.getString("Change")));
                        String s2 = String.format("%.2f", Float.parseFloat(jsonObject.getString("ChangePercent")));
                        String change =  s1+"("+s2.toString()+"%)";
                        Log.d("DEBUG",change);
                        MainActivity.favouriteListArrayAdapter.add(new FavouriteList(symbol,jsonObject.getString("Name"),jsonObject.getString("LastPrice"),
                                "Marketcap: "+jsonObject.getString("MarketCap"),Double.parseDouble(s1)));

                        MainActivity.listView.setAdapter(MainActivity.favouriteListArrayAdapter);
                        Toast.makeText(ResultActivity.this, "Bookmarked "+ jsonObject.getString("Name"),
                                Toast.LENGTH_SHORT).show();

                        Log.d("DEBUG", "Symbol retrieved:" + settings.getString("symbol", ""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false;
                    }
                    return true;
                }




        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



}
