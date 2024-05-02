package com.example.hairsalon.activity.statistics;

import android.os.Bundle;
import android.util.Log;

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
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SanPham extends AppCompatActivity {

    private PieChart pieChart;
    private List<PieEntry> entries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_san_pham);

        pieChart = findViewById(R.id.pieChart);
        pieChart.getDescription().setEnabled(false); // Tắt mô tả của biểu đồ

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getRevenueFromService();
        getRevenueFromProduct();
    }

    private void getRevenueFromService() {
        ApiService.apiService.getRevenueFromService().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        double totalService = calculateTotalRevenue(responseData.getData());
                        entries.add(new PieEntry((float) totalService, "Service"));
                        Log.i("revenue", "success");
                    } else {
                        Log.e("revenue", "No service revenue data found in response");
                    }
                } else {
                    Log.e("revenue", "API call for service revenue failed with error code: " + response.code());
                }
                // Sau khi đã xử lý xong dữ liệu từ API, cập nhật biểu đồ
                updatePieChart();
            }
            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("error api", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
    private void getRevenueFromProduct() {
        ApiService.apiService.getRevenueFromProduct().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        double totalProduct = calculateTotalRevenue(responseData.getData());
                        entries.add(new PieEntry((float) totalProduct, "Product"));
                    } else {
                        Log.e("revenue", "No product revenue data found in response");
                    }
                } else {
                    Log.e("revenue", "API call for product revenue failed with error code: " + response.code());
                }
                // Sau khi đã xử lý xong dữ liệu từ API, cập nhật biểu đồ
                updatePieChart();
            }
            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("error api", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private double calculateTotalRevenue(List<Map<String, Object>> dataList) {
        double totalRevenue = 0.0;
        for (Map<String, Object> data : dataList) {
            // Lấy giá trị doanh thu từ mục dữ liệu và cộng vào tổng doanh thu
            double revenue = ((Number) data.get("value")).doubleValue();
            totalRevenue += revenue;
        }
        return totalRevenue;
    }

    private void updatePieChart() {
        // Tạo PieDataSet và cập nhật dữ liệu vào biểu đồ
        PieDataSet dataSet = new PieDataSet(entries, "Combined Revenue");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // Vẽ lại biểu đồ để hiển thị dữ liệu mới
    }
}
