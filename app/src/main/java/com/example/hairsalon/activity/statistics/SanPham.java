package com.example.hairsalon.activity.statistics;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hairsalon.R;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.model.ResponseData;
import com.example.hairsalon.model.User;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        Button btnExportPDFSP = findViewById(R.id.btnExportPDFSP);
        btnExportPDFSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportChartToPdf();
            }
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

    private void exportChartToPdf() {
        // Tạo một Bitmap từ PieChart
        Bitmap chartBitmap = pieChart.getChartBitmap();
        final String fullName ;

        if (chartBitmap != null) {
            // Lưu bitmap thành tệp PDF
            int scaledWidth = chartBitmap.getWidth() / 2; // Giảm kích thước chiều rộng đi 50%
            int scaledHeight = chartBitmap.getHeight() / 2; // Giảm kích thước chiều cao đi 50%

            // Tạo một Bitmap mới với kích thước đã được giảm
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(chartBitmap, scaledWidth, scaledHeight, true);
            try {
                // Tạo tệp PDF trong thư mục Documents trên bộ nhớ ngoài

                File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SoSanhDoanhTHu.pdf");
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
                String customerId = sharedPreferences.getString("userId", "");

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

                                    Toast.makeText(SanPham.this,"PDF exported successfull", Toast.LENGTH_LONG);
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



//                Log.i("PDF successfully", "PDF exported successfully");
//                Toast.makeText(this, "PDF exported successfully", Toast.LENGTH_SHORT).show();
//
//                // Log đường dẫn đến tệp PDF đã lưu
//                Log.d("File Path PDF", pdfFile.getAbsolutePath());

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("PDF Error", "Error exporting PDF: " + e.getMessage());
                Toast.makeText(this, "Error exporting PDF", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("PDF Error", "Chart bitmap is null, unable to export PDF");
            Toast.makeText(this, "Unable to export PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
