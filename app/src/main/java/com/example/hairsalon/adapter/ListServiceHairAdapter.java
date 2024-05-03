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
import com.example.hairsalon.activity.servicehair.UpdateServiceHairActivity;
import com.example.hairsalon.model.ProductItem;
import com.example.hairsalon.model.ServiceHair;

import java.util.ArrayList;

public class ListServiceHairAdapter extends ArrayAdapter<ServiceHair> {

    private ArrayList<ServiceHair> serviceHairList;
    private Context mContext;

    public ListServiceHairAdapter(Context context, ArrayList<ServiceHair> serviceHairList) {
        super(context, 0, serviceHairList);
        this.mContext = context;
        this.serviceHairList = serviceHairList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.service_hair_item, parent, false);
        }

        // Lấy ra item hiện tại trong danh sách sản phẩm
        ServiceHair currentItem = serviceHairList.get(position);

        // Ánh xạ các view từ layout XML
        ImageView serviceImage = listItem.findViewById(R.id.service_image);
        TextView serviceName = listItem.findViewById(R.id.service_name);
        TextView servicePrice = listItem.findViewById(R.id.servicePrice);
        ImageView editIcon = listItem.findViewById(R.id.edit_service_hair);

        // Đặt dữ liệu cho các view
        ImageRequest imageRequest = new ImageRequest(
                currentItem.getUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        serviceImage.setImageBitmap(response);
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
        serviceName.setText(currentItem.getServiceName());
        servicePrice.setText(String.valueOf(currentItem.getPrice()));

        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UpdateServiceHairActivity.class);

                // Thêm dữ liệu vào Intent
                intent.putExtra("serviceHairId", currentItem.getId());
                intent.putExtra("serviceName", currentItem.getServiceName());
                intent.putExtra("detailPrice", currentItem.getPrice());
                intent.putExtra("description", currentItem.getDescription());
                intent.putExtra("imageUrl", currentItem.getUrl());

                // Khởi chạy Intent
                mContext.startActivity(intent);
            }
        });

        // Trả về view đã được tạo
        return listItem;
    }
}
