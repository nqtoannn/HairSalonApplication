package com.example.hairsalon.activity.statistics;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hairsalon.R;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.model.ResponseData;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SanPham extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_san_pham);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        PieChart pieChart = findViewById(R.id.pieChart);
        pieChart.getDescription().setEnabled(false); // Tắt mô tả của biểu đồ
        List<PieEntry> entries = new ArrayList<>();




        // Gọi API getRevenueFromProduct
        ApiService.apiService.getRevenueFromProduct().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ResponseData responseData = response.body();
                if (response.isSuccessful()) {
                    ResponseData revenueData = response.body();
                    entries.add(new PieEntry(revenueData.getData(), "Product"));
                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    PieData data = new PieData(dataSet);
                    pieChart.setData(data);
                    pieChart.invalidate(); // Refresh biểu đồ
                }
                else {
                    // Xử lý khi không thành công
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                // Xử lý khi gọi API thất bại
            }
        });

        // Gọi API getRevenueFromService
        ApiService.apiService.getRevenueFromService().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData revenueData = response.body();
                    entries.add(new PieEntry(revenueData.getAmount(), "Service"));
                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    PieData data = new PieData(dataSet);
                    pieChart.setData(data);
                    pieChart.invalidate(); // Refresh biểu đồ
                }
                else {
                    // Xử lý khi không thành công
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                // Xử lý khi gọi API thất bại
            }
        });
    }
}