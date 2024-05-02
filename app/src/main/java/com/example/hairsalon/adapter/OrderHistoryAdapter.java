package com.example.hairsalon.adapter;

import static androidx.core.content.ContextCompat.startActivity;

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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.R;
import com.example.hairsalon.activity.order.OrderDetail;
import com.example.hairsalon.model.Order;
import com.example.hairsalon.model.OrderItem;

import java.util.List;

public class OrderHistoryAdapter extends ArrayAdapter<Order> {

    private Context mContext;

    public OrderHistoryAdapter(Context context, List<Order> objects) {
        super(context, 0, objects);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.order_history_item, parent, false);

            holder = new ViewHolder();
            holder.productImageView = view.findViewById(R.id.imageView);
            holder.textProductName = view.findViewById(R.id.textProductName);
            holder.textPrice = view.findViewById(R.id.textPrice);
            holder.textQuantity = view.findViewById(R.id.textQuantity);
            holder.textOrderDate = view.findViewById(R.id.textOrderDate);
            holder.textStatus = view.findViewById(R.id.textStatus);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Order order = getItem(position);

        if (order != null) {
            OrderItem orderItem = order.getOrderItems().get(0);

            holder.textProductName.setText(orderItem.getProductItemName());
            holder.textPrice.setText("Giá: " + orderItem.getPrice() + " VND");
            holder.textQuantity.setText("Số lượng: " + orderItem.getQuantity());
            holder.textOrderDate.setText("Đặt ngày: " + order.getOrderDate());
            switch (order.getOrderStatus()) {
                case "PROCESSING":
                    holder.textStatus.setText("Đơn hàng đang chờ xác nhận");
                    break;
                case "ACCEPTED":
                    holder.textStatus.setText("Người bán đang chuẩn bị hàng");
                    break;
                case "REJECTED":
                    holder.textStatus.setText("Đơn hàng đã được hủy bởi khách hàng");
                    break;
                case "DELIVERY":
                    holder.textStatus.setText("Đơn hàng được bàn giao cho đơn vị vận chuyển");
                    break;
                case "SUCCESS":
                    holder.textStatus.setText("Đơn hàng đã được giao thành công");
                    break;
                default:
                    holder.textStatus.setText("Đã xảy ra lỗi ở đây");

            }

            // Load image using Volley
            ImageRequest imageRequest = new ImageRequest(
                    orderItem.getProductItemUrl(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            holder.productImageView.setImageBitmap(response);
                            Log.d("Volley", "Image loaded successfully");
                        }
                    },
                    0,
                    0,
                    ImageView.ScaleType.CENTER_CROP,
                    Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley", "Failed to load image", error);
                        }
                    }
            );


            // Add ImageRequest to Volley's request queue to load the image
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(imageRequest);
        }

        holder.productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderItem orderItem = order.getOrderItems().get(0);
                Intent intent = new Intent(mContext, OrderDetail.class);
                intent.putExtra("orderId", order.getId());
                intent.putExtra("nameProduct", orderItem.getProductItemName());
                intent.putExtra("price", orderItem.getPrice());
                intent.putExtra("address", "KTX Cỏ May, Khu phố 6, Linh Trung, Thủ Đức");
                intent.putExtra("imageUrl", orderItem.getProductItemUrl());
                intent.putExtra("status", order.getOrderStatus());
                mContext.startActivity(intent); // Corrected line
            }
        });


        return view;
    }

    static class ViewHolder {
        ImageView productImageView;
        TextView textProductName;
        TextView textPrice;
        TextView textQuantity;
        TextView textOrderDate;
        TextView textStatus;

    }
}
