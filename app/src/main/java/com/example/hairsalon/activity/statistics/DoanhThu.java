package com.example.hairsalon.activity.statistics;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.hairsalon.model.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        // Xử lý sự kiện khi người dùng chọn ngày kết thúc
        binding.textViewDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogEnd();
            }
        });

        binding.btnExportPDFDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportChartToPdf();
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

    private void exportChartToPdf() {
        // Tạo một Bitmap từ PieChart
        Bitmap chartBitmap = binding.barChart.getChartBitmap();

        if (chartBitmap != null) {
            // Lưu bitmap thành tệp PDF
            int scaledWidth = chartBitmap.getWidth() / 2; // Giảm kích thước chiều rộng đi 50%
            int scaledHeight = chartBitmap.getHeight() / 2; // Giảm kích thước chiều cao đi 50%

            // Tạo một Bitmap mới với kích thước đã được giảm
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(chartBitmap, scaledWidth, scaledHeight, true);
            try {
                File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "BaoCaoDoanhThu.pdf");
                FileOutputStream outputStream = new FileOutputStream(pdfFile);

                // Tạo Document của iTextPDF và PdfWriter để ghi vào tệp PDF
                com.itextpdf.text.Document document = new com.itextpdf.text.Document();
                PdfWriter.getInstance(document, outputStream);


                // Chuyển đổi Bitmap thành Image của iTextPDF
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Image image = Image.getInstance(byteArray);

                // Thêm ngày xuất file là ngày hiện tại vào tài liệu PDF
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String currentDate = sdf.format(new Date());

                // Thêm nội dung văn bản vào tài liệu PDF

                SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                Integer customerId = sharedPreferences.getInt("userId", -1);
                ApiService.apiService.getCustomerById(customerId).enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ResponseData responseData = response.body();
                            List<Map<String, Object>> customers = responseData.getData();

                            if (customers != null && !customers.isEmpty()) {
                                // Lấy thông tin của khách hàng đầu tiên (nếu có)
                                Map<String, Object> customer = customers.get(0);
                                String name = (String) customer.get("fullName");
                                Log.d("Customer FullName", "Full Name: " + name);
                                try {
                                    document.open();
                                    document.add(new com.itextpdf.text.Paragraph("--------------"));
                                    document.add(new com.itextpdf.text.Paragraph("Thong ke ve so sanh doanh thu giua San pham và Dich vu"));
                                    document.add(new Paragraph("Nhan vien: " + name));
                                    document.add(new com.itextpdf.text.Paragraph("Ngay xuat file: " + currentDate));
                                    document.add(new com.itextpdf.text.Paragraph("--------------"));
                                    document.add(image);
                                    document.close();

                                    Toast.makeText(DoanhThu.this,"PDF exported successfull", Toast.LENGTH_LONG);
                                } catch (DocumentException e) {
                                    Log.e("abcd error",""+ e);
                                    throw new RuntimeException(e);
                                }
                            } else {
                                Log.d("Customer Info", "No customer data found");
                            }
                        } else {
                            Log.d("API Error", "Failed to get customer data");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {

                    }
                });
//                Log.i("PDF", "PDF exported successfully");
//                Toast.makeText(this, "PDF exported successfully", Toast.LENGTH_SHORT).show();
//
//                // Log đường dẫn đến tệp PDF đã lưu
//                Log.d("File Path PDF", pdfFile.getAbsolutePath());

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("PDF", "Error exporting PDF: " + e.getMessage());
                Toast.makeText(this, "Error exporting PDF", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("PDF", "Chart bitmap is null, unable to export PDF");
            Toast.makeText(this, "Unable to export PDF", Toast.LENGTH_SHORT).show();
        }
    }
}

