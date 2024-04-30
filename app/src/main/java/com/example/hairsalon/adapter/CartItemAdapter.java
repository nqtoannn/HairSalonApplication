package com.example.hairsalon.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.R;
import com.example.hairsalon.activity.cart.CartActivity;
import com.example.hairsalon.activity.order.PayActivity;
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.model.CartItem;
import com.example.hairsalon.utils.Utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartItemAdapter extends ArrayAdapter<CartItem> {

    private Context mContext;
    private List<CartItem> mCartItems;

    public CartItemAdapter(Context context, ArrayList<CartItem> cartItems) {
        super(context, 0, cartItems);
        mContext = context;
        mCartItems = cartItems;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        // Lấy sản phẩm tại vị trí position
        CartItem cartItem = mCartItems.get(position);

        // Kiểm tra nếu convertView không được sử dụng lại, inflate layout mới
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_cart_item, parent, false);

            // Tạo ViewHolder mới
            holder = new ViewHolder();
            holder.productImageView = convertView.findViewById(R.id.product_image);
            holder.productNameTextView = convertView.findViewById(R.id.product_name);
            holder.productPriceTextView = convertView.findViewById(R.id.product_price);
            holder.productQuantityTextView = convertView.findViewById(R.id.product_quantity);
            holder.deleteIcon = convertView.findViewById(R.id.delete_icon);
            holder.xSeparatorTextView = convertView.findViewById(R.id.x_separator);

            // Lưu ViewHolder vào convertView
            convertView.setTag(holder);
        } else {
            // ViewHolder đã được lưu trong convertView
            holder = (ViewHolder) convertView.getTag();
        }

        // Gắn sự kiện lắng nghe cho deleteIcon
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện xóa item ở vị trí position
                showDeleteConfirmationDialog(position);
            }
        });

        // Binding dữ liệu từ đối tượng CartItem vào các thành phần UI
        if (cartItem.getProductItemName() != null && mContext instanceof CartActivity) {
            holder.xSeparatorTextView.setVisibility(View.VISIBLE);
            holder.deleteIcon.setVisibility(View.VISIBLE);
            holder.productQuantityTextView.setVisibility(View.VISIBLE);
        }
        else if (cartItem.getProductItemName() != null && mContext instanceof PayActivity) {
            holder.xSeparatorTextView.setVisibility(View.VISIBLE);
            holder.deleteIcon.setVisibility(View.GONE);
            holder.productQuantityTextView.setVisibility(View.VISIBLE);
        }
        else {
            holder.xSeparatorTextView.setVisibility(View.GONE);
            holder.deleteIcon.setVisibility(View.GONE);
            holder.productQuantityTextView.setVisibility(View.GONE);
        }
        holder.productNameTextView.setText(cartItem.getProductItemName());
        holder.productPriceTextView.setText(Utils.formatPrice(cartItem.getPrice()));

        holder.productQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));

        // Tạo ImageRequest để tải hình ảnh từ URL và hiển thị lên ImageView
        ImageRequest imageRequest = new ImageRequest(
                cartItem.getImageUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        holder.productImageView.setImageBitmap(response);
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

        // Thêm ImageRequest vào hàng đợi của Volley để tải hình ảnh
        Volley.newRequestQueue(mContext).add(imageRequest);

        return convertView;
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm "  + mCartItems.get(position).getProductItemName() +   " này?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý xóa sản phẩm ở vị trí đã xác định
                int itemId = mCartItems.get(position).getId();
                System.out.println("ItemId"+ itemId);
                Log.i("ItemId", String.valueOf(itemId));
                // Xử lý xóa sản phẩm ở vị trí đã xác định
                mCartItems.remove(position);
                removeCartItem(itemId);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng dialog nếu người dùng chọn hủy
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // ViewHolder pattern để tăng hiệu suất khi cuộn danh sách
    static class ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView productQuantityTextView;
        ImageView deleteIcon;
        TextView xSeparatorTextView;
    }

    private void removeCartItem(Integer cartItemId) {
        String apiUrl = Constant.baseUrl + "customer/deleteCartItem/" + String.valueOf(cartItemId);

        // Tạo yêu cầu xóa mục khỏi giỏ hàng
        StringRequest request = new StringRequest(Request.Method.DELETE, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        calculateAndUpdateTotalPrice();
                        Log.d("RemoveCartItemResponse", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý lỗi khi gửi yêu cầu
                        Log.e("RemoveCartItemError", apiUrl, error);
                    }
                });
        Volley.newRequestQueue(mContext).add(request);
    }

    private void calculateAndUpdateTotalPrice() {
        double totalPrice = 0;
        for (CartItem item : mCartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        TextView totalPriceTextView = ((CartActivity)mContext).findViewById(R.id.totalPrice);
        totalPriceTextView.setText(Utils.formatPrice(totalPrice));
    }

}
