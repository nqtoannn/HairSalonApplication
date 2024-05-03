package com.example.hairsalon.api;

import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.model.AuthenticationRequest;
import com.example.hairsalon.model.ResponseAuthData;
import com.example.hairsalon.model.ResponseData;
import com.example.hairsalon.model.ResponseServiceData;
import com.example.hairsalon.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl(Constant.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("productItem/findAll")
    Call<ResponseData> getProductItem();

    @GET("services/findAll")
    Call<ResponseData> getAllServiceHairs();
    @GET("management/customer/findAll")
    Call<ResponseData> getAllCustomner();

    @GET("productItem/search/{productItemName}")
    Call<ResponseData> searchProductItemByName(@Path("productItemName") String productItemName);

    @GET("customer/findAllCartItems/{cartId}")
    Call<ResponseData> getAllCartItemsByCartId(@Path("cartId") Integer cartId);

    @GET("customer/findById/{customerId}")
    Call<ResponseData> getCustomerByID(@Path("customerId") Integer customerId);

    @DELETE("customer/deleteAllCartItemByCartId/{cartId}")
    Call<Void> deleteAllCartItemsByCartId(@Path("cartId") Integer cartId);

    @GET("orders/getAllOrdersByCustomerId/{customerId}")
    Call<ResponseData> getAllOrderByCustomerId(@Path("customerId") Integer customerId);
    @Multipart
    @POST("management/hairService/uploadImageServiceHair")
    Call<Void> uploadServiceFile(@Part MultipartBody.Part file,
                          @Query("namePath") String namePath,
                          @Query("serviceHairId") String serviceHairId);

    @Multipart
    @POST("management/productItem/uploadImageProductItem")
    Call<Void> uploadFile(@Part MultipartBody.Part file,
                          @Query("namePath") String namePath,
                          @Query("productItemId") String productItemId);

    @POST("auth/register")
    Call<Void> registerUser(@Body User user);

    @POST("auth/authenticate")
    Call<ResponseAuthData> authenticateUser(@Body AuthenticationRequest request);

    @GET("services/search/{name}")
    Call<ResponseServiceData> getHairService(@Path("name") String name);

    @GET("salon/findAll")
    Call<ResponseData> getAllSalons();
    @GET("news/getAll")
    Call<ResponseData> getAllNews();
    @GET("management/employee/findAll")
    Call<ResponseData> getAllEmployees();
    @GET("management/employee/findById/{employeeId}")
    Call<ResponseData> getEmployeeById(@Path("employeeId") Integer employeeId);

    @GET("services/findAll")
    Call<ResponseData> getAllHairService();

    @GET("customer/findAllAppointmentByCustomerId/{customerId}")
    Call<ResponseData> getAllAppointmentByCustomerId(@Path("customerId") Integer customerId);


    @POST("management/employee/updateStatusUser")
    Call<ResponseData> updateUserStatus(@Body JsonObject json);

    @POST("auth/addEmployee")
    Call<ResponseAuthData> addNewEmployee(@Body JsonObject json);

    @GET("management/customer/findById/{customerId}")
    Call<ResponseData> getCustomerById(@Path("customerId") Integer customerId);


}
