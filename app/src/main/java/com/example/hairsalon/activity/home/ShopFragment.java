package com.example.hairsalon.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.cart.CartActivity;
import com.example.hairsalon.activity.product.ListProductActivity;
import com.example.hairsalon.adapter.ProductItemAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.FragmentShopBinding;
import com.example.hairsalon.model.ResponseData;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopFragment extends Fragment {

    private FragmentShopBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewCheapProduct;
    private List<Map<String, Object>> productItemList;

    public ShopFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = binding.recyclerViewProducts;
        recyclerViewCheapProduct = binding.recyclerViewProductsCheap;

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManagerCheapProduct = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCheapProduct.setLayoutManager(layoutManagerCheapProduct);

        getAllCartItemsAndUpdateCount();

        ApiService.apiService.getProductItem().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        productItemList = responseData.getData();
                        ProductItemAdapter adapter = new ProductItemAdapter(productItemList);
                        recyclerView.setAdapter(adapter);
                        recyclerViewCheapProduct.setAdapter(adapter);
                    } else {
                        Log.e("Error", "No product data found in response");
                    }
                } else {
                    Log.e("Error", "API call failed with error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("Error", "API call failed: " + t.getMessage());
            }
        });

        binding.cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        binding.searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String searchQuery = binding.searchBar.getText().toString();
                    Intent intent = new Intent(requireContext(), ListProductActivity.class);
                    intent.putExtra("searchQuery", searchQuery);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void getAllCartItemsAndUpdateCount() {
        ApiService.apiService.getAllCartItemsByCartId(1).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        List<Map<String, Object>> cartItemList = responseData.getData();
                        int totalNumber = 0;
                        for (Map<String, Object> cartItem : cartItemList) {
                            totalNumber += ((Number) cartItem.get("quantity")).intValue();
                        }
                        updateCartCount(totalNumber);
                    } else {
                        updateCartCount(0);
                        Log.e("Error", "No cart item data found in response");
                    }
                } else {
                    updateCartCount(0);
                    Log.e("Error", "API call failed with error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                updateCartCount(0);
                Log.e("Error", "API call failed: " + t.getMessage());
            }
        });
    }

    private void updateCartCount(int cartItemCount) {
        binding.textCartCount.setText(String.valueOf(cartItemCount));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
