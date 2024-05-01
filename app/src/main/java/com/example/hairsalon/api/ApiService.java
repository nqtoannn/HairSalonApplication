package com.example.hairsalon.api;

import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.model.AuthenticationRequest;
import com.example.hairsalon.model.ResponseAuthData;
import com.example.hairsalon.model.ResponseData;
import com.example.hairsalon.model.ResponseServiceData;
import com.example.hairsalon.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl(Constant.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("productItem/findAll")
    Call<ResponseData> getProductItem();

    @GET("users/findAll")
    Call<ResponseData> getUser();

    @GET("productItem/search/{productItemName}")
    Call<ResponseData> searchProductItemByName(@Path("productItemName") String productItemName);

    @GET("customer/findAllCartItems/{cartId}")
    Call<ResponseData> getAllCartItemsByCartId(@Path("cartId") Integer cartId);

    @GET("appointments/{customerId}")
    Call<ResponseData> getAllAppointmentByCustomerId(@Path("customerId") Integer customerId);


    @DELETE("customer/deleteAllCartItemByCartId/{cartId}")
    Call<Void> deleteAllCartItemsByCartId(@Path("cartId") Integer cartId);

    @GET("orders/getAllOrdersByCustomerId/{customerId}")
    Call<ResponseData> getAllOrderByCustomerId(@Path("customerId") Integer customerId);

    @POST("auth/register")
    Call<Void> registerUser(@Body User user);

    @POST("auth/authenticate")
    Call<ResponseAuthData> authenticateUser(@Body AuthenticationRequest request);


    @GET("services/search/{name}")
    Call<ResponseServiceData> getHairService(@Path("name") String name);

    @GET("salon/findAll")
    Call<ResponseData> getAllSalons();
    @GET("users/employee/findAll")
    Call<ResponseData> getAllEmployees();

    @GET("services/findAll")
    Call<ResponseData> getAllHairService();

    @GET("/management/customer/findById/{customerId}")
    Call<ResponseData> getCustomerByID(@Path("customerId") Integer customerId);

}
