package com.example.hairsalon.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.R;
import com.example.hairsalon.model.News;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, ArrayList<News> newsList) {
        super(context, 0, newsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.new_items, parent, false);
        }

        News currentNewsItem = getItem(position);
        ImageView newsImageView = listItemView.findViewById(R.id.news_image);

        ImageRequest imageRequest = new ImageRequest(
                currentNewsItem.getImageUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        newsImageView.setImageBitmap(response);
                        Log.d("Volley", "Image loaded successfully");
                    }
                },
                0,
                0,
                ImageView.ScaleType.CENTER_INSIDE,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Failed to load image", error);
                    }
                }
        );

        Volley.newRequestQueue(getContext()).add(imageRequest);

        TextView newsTitleView = listItemView.findViewById(R.id.news_title);
        newsTitleView.setText(currentNewsItem.getTitle());

        TextView newsContentView = listItemView.findViewById(R.id.news_content);
        newsContentView.setText(currentNewsItem.getDescription());

        return listItemView;
    }
}

