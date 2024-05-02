package com.example.hairsalon.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.R;
import com.example.hairsalon.activity.home.BookingFragment;
import com.example.hairsalon.activity.servicehair.DetailServiceHairActivity;
import com.example.hairsalon.model.HairService;

import java.util.List;
import java.util.Map;

public class HairServiceAdapter extends RecyclerView.Adapter<HairServiceAdapter.HairServiceViewHolder> {
    private List<Map<String, Object>> serviceList;
    private Context context;

    public HairServiceAdapter(List<Map<String, Object>> serviceList) {
        //this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public HairServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hair_service, parent, false);
        return new HairServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HairServiceViewHolder holder, int position) {
        if (serviceList != null && !serviceList.isEmpty()) {
            Map<String, Object> currentItem = serviceList.get(position);
            // Tiếp tục xử lý với currentItem
            Log.i("currentItem", currentItem.toString());
            ImageRequest imageRequest = new ImageRequest(
                    (String) currentItem.get("url"),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            holder.imageServiceView.setImageBitmap(response);
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

            Volley.newRequestQueue(holder.itemView.getContext()).add(imageRequest);

            holder.tvServiceName.setText((String) currentItem.get("serviceName"));
            holder.tvServicePrice.setText(String.valueOf(currentItem.get("price")) + " VND");

            holder.imageServiceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Lấy Context từ View được click
                Context context = holder.itemView.getContext();

                // Tạo Intent để chuyển sang DetailProductActivity
                Intent intent = new Intent(context, DetailServiceHairActivity.class);

                // Truyền dữ liệu đi kèm trong Intent
                    double idDouble = (Double) currentItem.get("id");
                    int idInt = Double.valueOf(idDouble).intValue();
                    double price = (Double) currentItem.get("price");
                intent.putExtra("serviceHairId", idInt);
                intent.putExtra("detailName", (String) currentItem.get("serviceName"));
                intent.putExtra("detailPrice", price); // Chuyển giá thành String
                intent.putExtra("detailDescription", (String) currentItem.get("description"));
                intent.putExtra("imageUrl", (String) currentItem.get("url"));
                // Start Activity
                context.startActivity(intent);
                }
            });


            // Xử lý sự kiện khi nhấp vào tên sản phẩm
            holder.tvServiceName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Xử lý sự kiện khi nhấp vào tên sản phẩm ở đây
                }
            });

            // Xử lý sự kiện khi nhấp vào nút mua
            holder.btnBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BookingFragment bookingFragment = new BookingFragment();

                    // Truy cập FragmentManager từ context của ViewHolder
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

                    // Bắt đầu một transaction fragment và thêm BookingFragment vào fragment_container
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, bookingFragment)
                            .addToBackStack(null) // Để có thể quay lại Fragment trước đó
                            .commit();
                }

            });
        } else {
            Log.e("HairServiceAdapter", "Service list is null or empty");
        }

    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }


    public static class HairServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView imageServiceView;
        TextView tvServiceName;
        TextView tvServicePrice;
        Button btnBooking;

        public HairServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            imageServiceView = itemView.findViewById(R.id.serviceimage);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvServicePrice = itemView.findViewById(R.id.tvServicePrice);
            btnBooking = itemView.findViewById(R.id.btnBooking);
        }
    }
}
