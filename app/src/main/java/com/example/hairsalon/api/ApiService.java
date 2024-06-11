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
import retrofit2.http.PUT;
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
    Call<ResponseData> getProductItem(); //done
    @GET("hairservice/findAll")
    Call<ResponseData> getAllServiceHairs(); //done
    @GET("user/getAllCustomer")
    Call<ResponseData> getAllCustomner(); //done
    @GET("order/revenue/getAllRevenueOrderBetween/{startMonth}/{endMonth}/{year}")
    Call<ResponseData> getRevenueFromServiceByMonth( @Path("startMonth") int startMonth,  @Path("endMonth") int endMonth, @Path("year") int year); //done
    @GET("order/revenue/getAllRevenueOrderByYear/{year}")
    Call<ResponseData> getRevenueFromProduct(@Path("year") int year); //chua product
    @GET("appointment/revenue/getAllRevenueServiceByYear/{year}")
    Call<ResponseData> getRevenueFromService(@Path("year") int year); //chua service
    @GET("productItem/search/{productItemName}")
    Call<ResponseData> searchProductItemByName(@Path("productItemName") String productItemName); //chua product
    @GET("user/findById/{customerId}")
    Call<ResponseData> getCustomerByID(@Path("customerId") String customerId); //done
    @DELETE("cart/deleteAllCartByCustomerId/{customerId}")
    Call<ResponseData> deleteAllCartItemsByCartId(@Path("customerId") String customerId); //done
    @DELETE("cart/{cartId}")
    Call<ResponseData> deleteCartById(@Path("cartId") String cartId);
    @GET("order/getAllOrdersByCustomerId/{customerId}")
    Call<ResponseData> getAllOrderByCustomerId(@Path("customerId") String customerId); //done
    @GET("order/getAllOrders")
    Call<ResponseData> getAllOrder(); //done
    @Multipart
    @POST("hairservice/uploadImageServiceHair") //done
    Call<Void> uploadServiceFile(@Part MultipartBody.Part file,
                          @Query("namePath") String namePath,
                          @Query("serviceHairId") String serviceHairId);
    @Multipart
    @POST("productItem/uploadImageProductItem") //chua
    Call<Void> uploadFile(@Part MultipartBody.Part file,
                          @Query("namePath") String namePath,
                          @Query("productItemId") String productItemId);
    @POST("auth/register")
    Call<Void> registerUser(@Body User user); //done
    @POST("auth/authenticate")
    Call<ResponseAuthData> authenticateUser(@Body AuthenticationRequest request); //done
    @GET("hairservice/search/{name}")
    Call<ResponseServiceData> getHairService(@Path("name") String name);//done
    @GET("appointment/salon/findAll")
    Call<ResponseData> getAllSalons(); //done
    @GET("hairservice/news/findAll")
    Call<ResponseData> getAllNews(); //done
    @GET("user/getAllEmployees")
    Call<ResponseData> getAllEmployees(); //done
//    @GET("management/employee/findById/{employeeId}")
    @GET("user/findById/{customerId}")
    Call<ResponseData> getEmployeeById(@Path("customerId") String customerId); //done // dung chung vs ctm o tren
    @GET("hairservice/findAll")
    Call<ResponseData> getAllHairService(); //done
    @GET("appointment/findByCustomerId/{customerId}")
    Call<ResponseData> getAllAppointmentByCustomerId(@Path("customerId") String customerId); //done
    @PUT("auth/updateStatusUser")
    Call<ResponseData> updateUserStatus(@Body JsonObject json); //done
    @PUT("auth/updateUserProfile")
    Call<ResponseData> updateUserProfile(@Body JsonObject json); //done
    @POST("auth/addEmployee")
    Call<ResponseAuthData> addNewEmployee(@Body JsonObject json); //done
    //@GET("management/customer/findById/{customerId}")
    @GET("user/findById/{customerId}")
    Call<ResponseData> getCustomerById(@Path("customerId") String customerId); //chua user
    @GET("appointment/findWaitingByEmployeeId/{employeeId}")
    Call<ResponseData> getAllAppointmentByEmployeeId(@Path("employeeId") String employeeId); //done
    @GET("appointment/findDoneByEmployeeId/{employeeId}")
    Call<ResponseData> getAllAppointmentDoneByEmployeeId(@Path("employeeId") String employeeId); //done

//    @GET("customer/getCartByCustomerId/{customerId}")
//    Call<String> getCartIdByCustomerId(@Path("customerId") String customerId); //chua order

//    @GET("customer/review/findAllReviewByServiceId/{serviceId}")
//    Call<ResponseData> findAllReviewByServiceId(@Path("serviceId") String serviceId); //chua order
    @GET("cart/{customerId}")
    Call<ResponseData> getAllCartItemsByCartId(@Path("customerId") String customerId); //done nhung chua sua tu cart ve userid
}
