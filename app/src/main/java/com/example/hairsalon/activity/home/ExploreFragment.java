package com.example.hairsalon.activity.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.hairsalon.R;
import com.example.hairsalon.adapter.NewsAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.model.News;
import com.example.hairsalon.model.ProductItem;
import com.example.hairsalon.model.ResponseData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class ExploreFragment extends Fragment {


    private List<Map<String, Object>> newItemList = new ArrayList<>();

    ArrayList<News> dataArrayList = new ArrayList<>();

    NewsAdapter newsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        ApiService.apiService.getAllNews().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        newItemList = responseData.getData();
                        for (Map<String, Object> news : newItemList) {
                            String title = (String) news.get("title");
                            String imageUrl = (String) news.get("imageUrl");
                            String description = (String) news.get("description");
                            News newsAdded = new News(title, description, imageUrl);
                            dataArrayList.add(newsAdded);
                        }
                        NewsAdapter adapter = new NewsAdapter(getContext(), dataArrayList);
                        ListView listView = view.findViewById(R.id.news_listview);
                        listView.setAdapter(adapter);
                    } else {
                        Log.e("Error", "No cart item data found in response");
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


        return view;
    }
}
