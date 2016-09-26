package com.example.abhi.autotextviewexample;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ABHI on 5/3/2016.
 */
public class NewsListAdapter extends ArrayAdapter<NewsDetails> {
    private List<NewsDetails> newsDetails;
    public NewsListAdapter(Context context, int resource, List<NewsDetails> objects) {
        super(context, resource, objects);
        newsDetails = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.d("LookUpStockListAdapter", "getView()");
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.news_layout, parent, false);
        }
        NewsDetails newsItem = newsDetails.get(position);

        TextView titleLinkText = (TextView) convertView.findViewById(R.id.titletext);
        titleLinkText.setText(Html.fromHtml("<a href=" + newsItem.getUrl() + "> " + newsItem.getTitle()) );
        titleLinkText.setMovementMethod(LinkMovementMethod.getInstance());
        //titleLinkText.setLinkTextColor(Color.BLACK);

        TextView descText = (TextView) convertView.findViewById(R.id.desctext);
        descText.setText(newsItem.getDescription());

        TextView publisherText = (TextView) convertView.findViewById(R.id.pubtext);
        publisherText.setText("Publisher : " + newsItem.getPublisher());

        TextView dateText = (TextView) convertView.findViewById(R.id.datetext);
        //dateText.setText("Date : " + newsItem.get);
        String dateString = newsItem.getDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = format.parse(dateString);
            SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy, hh:mm:ss a", Locale.US);
            dateText.setText("Date : " + sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            dateText.setText("Date : " + dateString);
        }

        return convertView;
    }
}
