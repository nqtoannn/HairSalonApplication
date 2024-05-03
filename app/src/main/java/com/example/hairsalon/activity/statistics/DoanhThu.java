package com.example.hairsalon.activity.statistics;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hairsalon.R;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.ActivityDoanhThuBinding;
import com.example.hairsalon.databinding.ActivityStatisticsBinding;
import com.example.hairsalon.model.ResponseData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Response;
import retrofit2.Callback;

public class DoanhThu extends AppCompatActivity {
    private ActivityDoanhThuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoanhThuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);


        // Xử lý sự kiện khi người dùng nhấn vào nút "Thống kê"
        binding.btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ngày bắt đầu và ngày kết thúc từ TextViews
                String startDate = binding.textViewDateStart.getText().toString();
                String endDate = binding.textViewDateEnd.getText().toString();
                // Kiểm tra xem đã chọn đủ ngày bắt đầu và ngày kết thúc chưa
                if (!startDate.equals("Nhấn để chọn ngày bắt đầu") && !endDate.equals("Nhấn để chọn ngày kết thúc")) {
                    // Đã chọn đủ ngày bắt đầu và ngày kết thúc, thực hiện thống kê dữ liệu ở đây
                    //Log.e("abc",""+startDate+"..a.."+endDate);

                    performStatistics(startDate, endDate);
                } else {
                    // Nếu chưa chọn đủ ngày bắt đầu và ngày kết thúc, hiển thị thông báo cho người dùng
                    Toast.makeText(DoanhThu.this, "Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc!", Toast.LENGTH_SHORT).show();
                }
                // Hiển thị biểu đồ cột
                //performStatistics(startDate, endDate);
            }
        });

        // Xử lý sự kiện khi người dùng chọn ngày bắt đầu
        binding.textViewDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogStart();
            }
        });

        // Xử lý sự kiện khi người dùng chọn ngày kết thúc
        binding.textViewDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogEnd();
            }
        });
    }

    // Phương thức hiển thị DatePickerDialog cho ngày bắt đầu
    private void showDatePickerDialogStart() {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (year < currentYear || (year == currentYear && monthOfYear <= currentMonth)) {
                            String dateString = String.format(Locale.getDefault(), "%d-%02d", year, monthOfYear + 1);
                            binding.textViewDateStart.setText(dateString);
                        } else {
                            Toast.makeText(getApplicationContext(), "Chọn lại năm tháng bắt đầu!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                year, month, 0);
        datePickerDialog.show();
    }

    // Hiển thị dialog chọn ngày kết thúc
    private void showDatePickerDialogEnd() {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int startYear = getYearFromDateString(binding.textViewDateStart.getText().toString());
                        int startMonth = getMonthFromDateString(binding.textViewDateStart.getText().toString());

                        if ((year > startYear || (year == startYear && monthOfYear >= startMonth))
                                && (year < currentYear || (year == currentYear && monthOfYear <= currentMonth))) {
                            String dateString = String.format(Locale.getDefault(), "%d-%02d", year, monthOfYear + 1);
                            binding.textViewDateEnd.setText(dateString);
                        } else {
                            Toast.makeText(getApplicationContext(), "Chọn lại năm tháng kết thúc!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                year, month, 0);
        datePickerDialog.show();
    }

    // Lấy năm từ chuỗi ngày tháng (yyyy-MM)
    private int getYearFromDateString(String dateString) {
        return Integer.parseInt(dateString.substring(0, 4));
    }

    // Lấy tháng từ chuỗi ngày tháng (yyyy-MM)
    private int getMonthFromDateString(String dateString) {
        return Integer.parseInt(dateString.substring(5));
    }

    // Phương thức thực hiện thống kê dữ liệu
    private void performStatistics(String startDate, String endDate) {
        // Tạo danh sách dữ liệu để lưu trữ các mục của biểu đồ cột
        ArrayList<BarEntry> entries = new ArrayList<>();
        Log.e("success","ád");
        // Lấy năm và tháng từ các ngày bắt đầu và kết thúc
        int startYear = getYearFromDateString(startDate);
        int startMonth = getMonthFromDateString(startDate);
        int endYear = getYearFromDateString(endDate);
        int endMonth = getMonthFromDateString(endDate);
        //Log.e("abcdef", ".."+startYear+".."+startMonth+".."+endYear+".."+endMonth);

        // Gọi API để lấy dữ liệu doanh thu của từng tháng trong khoảng thời gian
        for (int year = startYear; year <= endYear; year++) {
            int monthStart = (year == startYear) ? startMonth : 1;
            int monthEnd = (year == endYear) ? endMonth : 12;
            Log.e("abcdef",".."+monthStart+".."+monthEnd);

            for (int month = monthStart; month <= monthEnd; month++) {
                int finalMonth = month;
                Log.e("abcdef",".."+month);
                ApiService.apiService.getRevenueFromServiceByMonth(year, month).enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ResponseData responseData = response.body();
                            Log.e("success","abc");
                            if (responseData.getData() != null && !responseData.getData().isEmpty()) {
                                // Lấy tổng doanh thu của tháng từ dữ liệu trả về
                                double revenueOfMonth = calculateTotalRevenue(responseData.getData());
                                entries.add(new BarEntry((float) finalMonth, (float) revenueOfMonth));
                                Log.e("success","11"+entries);
                                // Hiển thị biểu đồ cột sau khi đã có đủ dữ liệu từ các tháng
                                if (entries.size() == (endMonth - startMonth + 1)) {
                                    showBarChart(entries);
                                    Log.e("success",""+entries);

                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ResponseData> call, Throwable t) {
                        // Xử lý lỗi khi gọi API không thành công
                        Log.e("API Error", "Failed to fetch revenue: " + t.getMessage());
                        t.printStackTrace();
                    }


                });
            }
        }
    }

    // Hiển thị biểu đồ cột với dữ liệu từ danh sách entries
    private void showBarChart(ArrayList<BarEntry> entries) {
        BarDataSet barDataSet = new BarDataSet(entries, "Doanh thu theo tháng");
        barDataSet.setColor(Color.BLUE);

        BarData barData = new BarData(barDataSet);
        binding.barChart.setData(barData);
        binding.barChart.invalidate(); // Refresh biểu đồ
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


}

