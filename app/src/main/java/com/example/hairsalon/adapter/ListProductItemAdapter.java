package com.example.hairsalon.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.R;
import com.example.hairsalon.activity.product.AddProductItemActivity;
import com.example.hairsalon.activity.product.UpdateProductItemActivity;
import com.example.hairsalon.model.ProductItem;

import java.util.ArrayList;

public class ListProductItemAdapter extends ArrayAdapter<ProductItem> {

    private ArrayList<ProductItem> productList;
    private Context mContext;

    public ListProductItemAdapter(Context context, ArrayList<ProductItem> productList) {
        super(context, 0, productList);
        this.mContext = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.product_item, parent, false);
        }

        // Lấy ra item hiện tại trong danh sách sản phẩm
        ProductItem currentItem = productList.get(position);

        // Ánh xạ các view từ layout XML
        ImageView productImage = listItem.findViewById(R.id.product_image);
        TextView productName = listItem.findViewById(R.id.product_name);
        TextView productQuantity = listItem.findViewById(R.id.product_quantity);
        TextView productPrice = listItem.findViewById(R.id.product_price);
        ImageView editIcon = listItem.findViewById(R.id.edit_product_item);

        // Đặt dữ liệu cho các view
        ImageRequest imageRequest = new ImageRequest(
                currentItem.getImageUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        productImage.setImageBitmap(response);
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
        productName.setText(currentItem.getProductItemName());
        productQuantity.setText(String.valueOf(currentItem.getQuantityInStock()));
        productPrice.setText(String.valueOf(currentItem.getPrice()));

        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UpdateProductItemActivity.class);

                // Thêm dữ liệu vào Intent
                intent.putExtra("productItemName", currentItem.getProductItemName());
                intent.putExtra("detailPrice", currentItem.getPrice());
                intent.putExtra("description", currentItem.getDescription());
                intent.putExtra("imageUrl", currentItem.getImageUrl());
                intent.putExtra("productItemId", currentItem.getId());
                intent.putExtra("quantity", currentItem.getQuantityInStock());

                // Khởi chạy Intent
                mContext.startActivity(intent);
            }
        });

        // Trả về view đã được tạo
        return listItem;
    }
}
