package com.example.hairsalon.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.R;
import com.example.hairsalon.model.ProductItem;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ProductItem> mProductList;

    public GridAdapter(Context context, ArrayList<ProductItem> productList) {
        mContext = context;
        mProductList = productList;
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.imageViewProduct = convertView.findViewById(R.id.imageViewProduct);
            holder.textViewProductName = convertView.findViewById(R.id.textViewProductName);
            holder.textViewProductPrice = convertView.findViewById(R.id.textViewProductPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProductItem productItem = mProductList.get(position);
        Log.d("Adapter", "Loading image for position: " + position + ", URL: " + productItem.getImageUrl());

        // Tải hình ảnh từ URL bằng Volley
        ImageRequest imageRequest = new ImageRequest(
                productItem.getImageUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        holder.imageViewProduct.setImageBitmap(response);
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

        Volley.newRequestQueue(mContext).add(imageRequest);

        holder.textViewProductName.setText(productItem.getProductItemName());
        holder.textViewProductPrice.setText(String.valueOf(productItem.getPrice()));

        return convertView;
    }

    static class ViewHolder {
        ImageView imageViewProduct;
        TextView textViewProductName;
        TextView textViewProductPrice;
    }
}
