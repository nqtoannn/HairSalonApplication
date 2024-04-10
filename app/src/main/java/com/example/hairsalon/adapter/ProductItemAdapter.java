package com.example.hairsalon.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.R;
import com.example.hairsalon.activity.cart.CartActivity;
import com.example.hairsalon.activity.order.PayActivity;
import com.example.hairsalon.activity.product.DetailProductActivity;
import com.example.hairsalon.activity.product.ListProductActivity;
import com.example.hairsalon.activity.shop.HomeShopActivity;
import com.example.hairsalon.model.ProductItem;
import java.util.List;
import java.util.Map;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductItemViewHolder> {
    private List<Map<String, Object>> productList;

    public ProductItemAdapter(List<Map<String, Object>> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ProductItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        Map<String, Object> currentItem = productList.get(position);
        Log.i("currentItem", currentItem.toString());
        ImageRequest imageRequest = new ImageRequest(
                (String) currentItem.get("imageUrl"),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        holder.imageView.setImageBitmap(response);
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

        holder.tvName.setText((String) currentItem.get("productItemName"));
        holder.tvPrice.setText(String.valueOf(currentItem.get("price")) + " VND");

        // Xử lý sự kiện khi nhấp vào hình ảnh
        // Xử lý sự kiện khi nhấp vào hình ảnh
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy Context từ View được click
                Context context = holder.itemView.getContext();

                // Tạo Intent để chuyển sang DetailProductActivity
                Intent intent = new Intent(context, DetailProductActivity.class);

                // Truyền dữ liệu đi kèm trong Intent

                double productId = (Double) currentItem.get("id");
                double price = (Double) currentItem.get("price");
                int productIdInteger = (int) productId;
                intent.putExtra("productItemId", productIdInteger);
                intent.putExtra("detailName", (String) currentItem.get("productItemName"));
                intent.putExtra("detailPrice", price); // Chuyển giá thành String
                intent.putExtra("detailDescription", (String) currentItem.get("description"));
                intent.putExtra("imageUrl", (String) currentItem.get("imageUrl"));


                // Start Activity
                context.startActivity(intent);
            }
        });


        // Xử lý sự kiện khi nhấp vào tên sản phẩm
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện khi nhấp vào tên sản phẩm ở đây
            }
        });

        // Xử lý sự kiện khi nhấp vào nút mua
        holder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.buyButton.getContext();
                Intent intent = new Intent(context, PayActivity.class);

                // Truyền dữ liệu đi kèm trong Intent

                double productId = (Double) currentItem.get("id");
                double price = (Double) currentItem.get("price");
                int productIdInteger = (int) productId;
                intent.putExtra("productItemId", productIdInteger);
                intent.putExtra("detailName", (String) currentItem.get("productItemName"));
                intent.putExtra("detailPrice", price); // Chuyển giá thành String
                intent.putExtra("detailDescription", (String) currentItem.get("description"));
                intent.putExtra("imageUrl", (String) currentItem.get("imageUrl"));
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName;
        TextView tvPrice;
        Button buyButton;

        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            tvName = itemView.findViewById(R.id.tvProductItemName);
            tvPrice = itemView.findViewById(R.id.tvProuctItemPrice);
            buyButton = itemView.findViewById(R.id.buy_button);
        }
    }
}
